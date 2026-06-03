package com.slicex.audioconverter.service;

import com.slicex.audioconverter.config.AudioUploadConfig;
import com.slicex.audioconverter.domain.AudioFormat;
import com.slicex.audioconverter.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AudioFileValidationService {

    private final AudioUploadConfig audioUploadConfig;

    public void validate(MultipartFile file, AudioFormat formatoPreConversao) {
        if (file == null || file.isEmpty()) {
            throw new DomainException("O arquivo de áudio é obrigatório.");
        }

        if (file.getSize() > audioUploadConfig.getMaxFileSizeInBytes()) {
            throw new DomainException("O arquivo enviado excede o tamanho máximo permitido.");
        }

        AudioFormat formatoDoArquivo = obterFormatoDoArquivo(file);

        if (formatoDoArquivo != formatoPreConversao) {
            throw new DomainException("O formato informado não corresponde ao arquivo enviado.");
        }
    }

    private AudioFormat obterFormatoDoArquivo(MultipartFile file) {
        String nomeArquivo = file.getOriginalFilename();

        if (nomeArquivo == null || !nomeArquivo.contains(".")) {
            throw new DomainException("Arquivo sem extensão válida.");
        }

        String extensao = nomeArquivo
                .substring(nomeArquivo.lastIndexOf(".") + 1)
                .toUpperCase();

        try {
            return AudioFormat.valueOf(extensao);
        } catch (IllegalArgumentException ex) {
            throw new DomainException("Formato de arquivo não suportado. Envie apenas MP3 ou WAV.");
        }
    }
}