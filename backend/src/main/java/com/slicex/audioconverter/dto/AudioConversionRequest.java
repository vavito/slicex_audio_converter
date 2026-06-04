package com.slicex.audioconverter.dto;

import com.slicex.audioconverter.domain.AudioFormat;
import io.swagger.v3.oas.annotations.media.Schema;

public record AudioConversionRequest(
        @Schema(description = "Nome original do arquivo enviado") String nomeDoArquivoOriginal,
        @Schema(description = "Formato do áudio antes da conversão") AudioFormat formatoPreConversao,
        @Schema(description = "Formato desejado após conversão") AudioFormat formatoPosConversao
        ) {
}
