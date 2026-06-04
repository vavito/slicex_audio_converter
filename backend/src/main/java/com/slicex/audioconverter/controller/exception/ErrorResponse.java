package com.slicex.audioconverter.controller.exception;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponse(
        @Schema(description = "Timestamp da ocorrência do erro") LocalDateTime timestamp,
        @Schema(description = "Código HTTP retornado") int status,
        @Schema(description = "Descrição curta do erro") String error,
        @Schema(description = "Mensagem detalhada do erro") String message
) {
}