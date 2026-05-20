package com.slicex.audioconverter.mapper;

import com.slicex.audioconverter.domain.AudioConversion;
import com.slicex.audioconverter.domain.exception.DomainException;
import com.slicex.audioconverter.dto.AudioConversionRequest;
import com.slicex.audioconverter.dto.AudioConversionResponse;
import org.springframework.stereotype.Component;

@Component
public class AudioConversionMapper {
    public AudioConversion toEntity(AudioConversionRequest audioConversionRequest) {
        if (audioConversionRequest == null) {
            throw new DomainException("Os dados de conversão são obrigatórios.");
        };

        return AudioConversion.create(
                audioConversionRequest.nomeDoArquivoOriginal(),
                audioConversionRequest.formatoPreConversao(),
                audioConversionRequest.formatoPosConversao()
        );
    }

    public AudioConversionResponse toResponseDTO(AudioConversion audioConversion) {
        if (audioConversion == null) return null;

        return new AudioConversionResponse(
                audioConversion.getNomeDoArquivoOriginal(),
                audioConversion.getFormatoPreConversao(),
                audioConversion.getFormatoPosConversao(),
                audioConversion.getStatusDaConversao()
        );
    }
}
