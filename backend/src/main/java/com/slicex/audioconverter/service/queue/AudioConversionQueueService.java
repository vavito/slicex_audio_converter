package com.slicex.audioconverter.service.queue;

import com.slicex.audioconverter.domain.AudioConversion;
import com.slicex.audioconverter.domain.AudioConversionStatus;
import com.slicex.audioconverter.domain.exception.DomainException;
import com.slicex.audioconverter.dto.AudioConversionJobResponse;
import com.slicex.audioconverter.dto.AudioConversionRequest;
import com.slicex.audioconverter.external.FfmpegAudioConverter;
import com.slicex.audioconverter.mapper.AudioConversionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class AudioConversionQueueService {

    private final AudioConversionMapper mapper;
    private final FfmpegAudioConverter ffmpegAudioConverter;

    private final BlockingQueue<ConversionJob> queue = new LinkedBlockingQueue<>();
    private final Map<String, ConversionJob> jobs = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void startWorker() {
        executor.submit(() -> {
            while (true) {
                ConversionJob job = queue.take();
                processJob(job);
            }
        });
    }

    public AudioConversionJobResponse enqueue(File inputFile, AudioConversionRequest request) {
        String id = UUID.randomUUID().toString();

        ConversionJob job = new ConversionJob(id, inputFile, request);

        jobs.put(id, job);
        queue.add(job);

        return new AudioConversionJobResponse(
                id,
                job.getStatus(),
                "Conversão adicionada à fila."
        );
    }

    public AudioConversionJobResponse getStatus(String id) {
        ConversionJob job = getJobOrThrow(id);

        return new AudioConversionJobResponse(
                job.getId(),
                job.getStatus(),
                job.getErrorMessage()
        );
    }

    public File getOutputFile(String id) {
        ConversionJob job = getJobOrThrow(id);

        if (job.getStatus() != AudioConversionStatus.CONCLUIDO) {
            throw new DomainException("A conversão ainda não foi concluída.");
        }

        return job.getOutputFile();
    }

    private void processJob(ConversionJob job) {
        AudioConversion audioConversion = mapper.toEntity(job.getRequest());

        try {
            audioConversion.iniciarProcessamento();
            job.setStatus(AudioConversionStatus.EM_PROCESSAMENTO);

            File outputFile = ffmpegAudioConverter.convert(
                    job.getInputFile(),
                    audioConversion.getFormatoPosConversao(),
                    audioConversion.getNomeDoArquivoOriginal()
            );

            audioConversion.concluir();
            job.setOutputFile(outputFile);
            job.setStatus(AudioConversionStatus.CONCLUIDO);

        } catch (Exception ex) {
            job.setStatus(AudioConversionStatus.FALHOU);
            job.setErrorMessage(ex.getMessage());

        } finally {
            if (job.getInputFile() != null && job.getInputFile().exists()) {
                job.getInputFile().delete();
            }
        }
    }

    private ConversionJob getJobOrThrow(String id) {
        ConversionJob job = jobs.get(id);

        if (job == null) {
            throw new DomainException("Conversão não encontrada.");
        }

        return job;
    }
}