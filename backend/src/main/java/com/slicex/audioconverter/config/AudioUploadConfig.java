package com.slicex.audioconverter.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AudioUploadConfig {

    @Value("${app.audio.max-file-size-mb}")
    private Long maxFileSizeMb;

    public long getMaxFileSizeInBytes() {
        return maxFileSizeMb * 1024 * 1024;
    }
}