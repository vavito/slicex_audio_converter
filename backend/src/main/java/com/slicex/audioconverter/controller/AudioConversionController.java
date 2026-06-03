package com.slicex.audioconverter.controller;

import com.slicex.audioconverter.domain.AudioFormat;
import com.slicex.audioconverter.dto.AudioConversionJobResponse;
import com.slicex.audioconverter.dto.AudioConversionRequest;
import com.slicex.audioconverter.service.AudioFileValidationService;
import com.slicex.audioconverter.service.queue.AudioConversionQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/audio/conversions")
@RequiredArgsConstructor
public class AudioConversionController {

    private final AudioConversionQueueService queueService;
    private final AudioFileValidationService validationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AudioConversionJobResponse> convert(
            @RequestParam("file") MultipartFile file,
            @RequestParam("nomeDoArquivoOriginal") String nomeDoArquivoOriginal,
            @RequestParam("formatoPreConversao") AudioFormat formatoPreConversao,
            @RequestParam("formatoPosConversao") AudioFormat formatoPosConversao
    ) throws IOException {
        AudioConversionRequest request = new AudioConversionRequest(
                nomeDoArquivoOriginal,
                formatoPreConversao,
                formatoPosConversao
        );

        validationService.validate(file, formatoPreConversao);

        File inputFile = File.createTempFile("input-", "-" + file.getOriginalFilename());
        file.transferTo(inputFile);

        return ResponseEntity
                .accepted()
                .body(queueService.enqueue(inputFile, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AudioConversionJobResponse> getStatus(@PathVariable String id) {
        return ResponseEntity.ok(queueService.getStatus(id));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable String id) {
        File outputFile = queueService.getOutputFile(id);

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