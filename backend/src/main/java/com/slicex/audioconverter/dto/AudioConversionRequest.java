package com.slicex.audioconverter.dto;

import com.slicex.audioconverter.domain.AudioConversionStatus;
import com.slicex.audioconverter.domain.AudioFormat;

public record AudioConversionRequest(
        String nomeDoArquivoOriginal,
        AudioFormat formatoPreConversao,
        AudioFormat formatoPosConversao
        ) {
}
