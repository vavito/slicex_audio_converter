package com.slicex.audioconverter.service.queue;

import com.slicex.audioconverter.domain.AudioConversionStatus;
import com.slicex.audioconverter.dto.AudioConversionRequest;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class ConversionJob {
    private String id;
    private AudioConversionStatus status;
    private File inputFile;
    private File outputFile;
    private AudioConversionRequest request;
    private String errorMessage;

    public ConversionJob(String id, File inputFile, AudioConversionRequest request) {
        this.id = id;
        this.inputFile = inputFile;
        this.request = request;
        this.status = AudioConversionStatus.PENDENTE;
    }
}