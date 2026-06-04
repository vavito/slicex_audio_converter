package com.slicex.audioconverter.service.queue;

import com.slicex.audioconverter.domain.AudioConversion;
import com.slicex.audioconverter.domain.AudioConversionStatus;
import com.slicex.audioconverter.domain.AudioFormat;
import com.slicex.audioconverter.domain.exception.DomainException;
import com.slicex.audioconverter.dto.AudioConversionJobResponse;
import com.slicex.audioconverter.dto.AudioConversionRequest;
import com.slicex.audioconverter.external.FfmpegAudioConverter;
import com.slicex.audioconverter.mapper.AudioConversionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AudioConversionQueueServiceTest {

    @Mock
    private AudioConversionMapper mapper;

    @Mock
    private FfmpegAudioConverter ffmpegAudioConverter;

    @Test
    void deveCriarNovoJobRetornandoJobId() throws Exception {
        AudioConversionQueueService service = new AudioConversionQueueService(mapper, ffmpegAudioConverter);
        File inputFile = Files.createTempFile("job-test", ".mp3").toFile();
        AudioConversionRequest request = new AudioConversionRequest("musica.mp3", AudioFormat.MP3, AudioFormat.WAV);

        AudioConversionJobResponse response = service.enqueue(inputFile, request);

        assertNotNull(response.id());
        assertEquals(AudioConversionStatus.PENDENTE, response.status());
        assertEquals("Conversão adicionada à fila.", response.message());

        inputFile.delete();
    }

    @Test
    void deveRetornarStatusQuandoJobExistir() throws Exception {
        AudioConversionQueueService service = new AudioConversionQueueService(mapper, ffmpegAudioConverter);
        File inputFile = Files.createTempFile("job-test", ".mp3").toFile();
        AudioConversionRequest request = new AudioConversionRequest("musica.mp3", AudioFormat.MP3, AudioFormat.WAV);

        AudioConversionJobResponse enqueue = service.enqueue(inputFile, request);
        AudioConversionJobResponse status = service.getStatus(enqueue.id());

        assertEquals(enqueue.id(), status.id());
        assertEquals(AudioConversionStatus.PENDENTE, status.status());
        assertNull(status.message());

        inputFile.delete();
    }

    @Test
    void deveRetornarErroQuandoJobNaoExistir() {
        AudioConversionQueueService service = new AudioConversionQueueService(mapper, ffmpegAudioConverter);

        DomainException exception = assertThrows(DomainException.class, () -> service.getStatus("nao-existe"));

        assertEquals("Conversão não encontrada.", exception.getMessage());
    }

    @Test
    void deveMarcarJobComoFalhouQuandoFfmpegFalha() throws Exception {
        AudioConversionQueueService service = new AudioConversionQueueService(mapper, ffmpegAudioConverter);
        File inputFile = Files.createTempFile("job-test", ".mp3").toFile();
        AudioConversionRequest request = new AudioConversionRequest("musica.mp3", AudioFormat.MP3, AudioFormat.WAV);
        ConversionJob job = new ConversionJob("job-id", inputFile, request);
        AudioConversion conversion = AudioConversion.create("musica.mp3", AudioFormat.MP3, AudioFormat.WAV);

        when(mapper.toEntity(request)).thenReturn(conversion);
        when(ffmpegAudioConverter.convert(any(File.class), eq(AudioFormat.WAV), eq("musica.mp3")))
                .thenThrow(new DomainException("Erro ao converter o áudio."));

        Method processJob = AudioConversionQueueService.class.getDeclaredMethod("processJob", ConversionJob.class);
        processJob.setAccessible(true);
        processJob.invoke(service, job);

        assertEquals(AudioConversionStatus.FALHOU, job.getStatus());
        assertEquals("Erro ao converter o áudio.", job.getErrorMessage());
        verify(ffmpegAudioConverter, times(1)).convert(any(File.class), eq(AudioFormat.WAV), eq("musica.mp3"));
        inputFile.delete();
    }
}
