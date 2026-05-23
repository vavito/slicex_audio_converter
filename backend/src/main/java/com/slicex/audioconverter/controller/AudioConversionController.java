package com.slicex.audioconverter.controller;

import com.slicex.audioconverter.domain.AudioFormat;
import com.slicex.audioconverter.dto.AudioConversionRequest;
import com.slicex.audioconverter.service.AudioConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/audio/conversions")
@RequiredArgsConstructor
public class AudioConversionController {

    private final AudioConversionService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> convert(
            @RequestParam("file") MultipartFile file,
            @RequestParam("nomeDoArquivoOriginal") String nomeDoArquivoOriginal,
            @RequestParam("formatoPreConversao") AudioFormat formatoPreConversao,
            @RequestParam("formatoPosConversao") AudioFormat formatoPosConversao
    ) {
        AudioConversionRequest request = new AudioConversionRequest(
                nomeDoArquivoOriginal,
                formatoPreConversao,
                formatoPosConversao
        );

        File outputFile = service.convert(file, request);

        Resource resource = new FileSystemResource(outputFile);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + outputFile.getName() + "\""
                )
                .body(resource);
    }
}