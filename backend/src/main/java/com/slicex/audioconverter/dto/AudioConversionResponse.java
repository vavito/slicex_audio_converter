package com.slicex.audioconverter.dto;

import com.slicex.audioconverter.domain.AudioConversionStatus;
import com.slicex.audioconverter.domain.AudioFormat;

public record AudioConversionResponse(
        String nomeDoArquivoOriginal,
        AudioFormat formatoPreConversao,
        AudioFormat formatoPosConversao,
        AudioConversionStatus statusDaConversao
        ) {
}
