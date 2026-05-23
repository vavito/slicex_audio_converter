package com.slicex.audioconverter.service;

import com.slicex.audioconverter.config.AudioUploadConfig;
import com.slicex.audioconverter.domain.AudioConversion;
import com.slicex.audioconverter.domain.exception.DomainException;
import com.slicex.audioconverter.dto.AudioConversionRequest;
import com.slicex.audioconverter.external.FfmpegAudioConverter;
import com.slicex.audioconverter.mapper.AudioConversionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AudioConversionService {

    private final AudioConversionMapper mapper;
    private final AudioUploadConfig audioUploadConfig;
    private final FfmpegAudioConverter ffmpegAudioConverter;

    public File convert(MultipartFile file, AudioConversionRequest request) {
        if (file == null || file.isEmpty()) {
            throw new DomainException("O arquivo de áudio é obrigatório.");
        }

        if (file.getSize() > audioUploadConfig.getMaxFileSizeInBytes()) {
            throw new DomainException("O arquivo enviado excede o tamanho máximo permitido.");
        }

        AudioConversion audioConversion = mapper.toEntity(request);
        audioConversion.iniciarProcessamento();

        try {
            File inputFile = File.createTempFile("input-", "-" + file.getOriginalFilename());
            file.transferTo(inputFile);

            File outputFile = ffmpegAudioConverter.convert(
                    inputFile,
                    audioConversion.getFormatoPosConversao()
            );

            audioConversion.concluir();

            return outputFile;

        } catch (IOException ex) {
            audioConversion.falhar();
            throw new DomainException("Erro ao preparar o arquivo para conversão.");

        } catch (Exception ex) {
            audioConversion.falhar();
            throw new DomainException("Erro ao converter o áudio.");
        }
    }
}