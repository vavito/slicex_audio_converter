package com.slicex.audioconverter.dto;

import com.slicex.audioconverter.domain.AudioConversionStatus;

public record AudioConversionJobResponse(
        String id,
        AudioConversionStatus status,
        String message
) {
}