package com.slicex.audioconverter.dto;

import com.slicex.audioconverter.domain.AudioConversionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record AudioConversionJobResponse(
        @Schema(description = "Identificador do job (UUID)") String id,
        @Schema(description = "Status do job: PENDENTE, EM_PROCESSAMENTO, CONCLUIDO, FALHOU") AudioConversionStatus status,
        @Schema(description = "Mensagem de status ou erro (quando aplicável)") String message
) {
}