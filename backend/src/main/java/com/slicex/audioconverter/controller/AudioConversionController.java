package com.slicex.audioconverter.controller;

import com.slicex.audioconverter.domain.AudioFormat;
import com.slicex.audioconverter.dto.AudioConversionJobResponse;
import com.slicex.audioconverter.dto.AudioConversionRequest;
import com.slicex.audioconverter.service.AudioFileValidationService;
import com.slicex.audioconverter.service.queue.AudioConversionQueueService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "Audio Conversion", description = "Endpoints para envio, consulta e download de conversões de áudio.")
public class AudioConversionController {

    private final AudioConversionQueueService queueService;
    private final AudioFileValidationService validationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Inicia conversão assíncrona",
            description = "Recebe um arquivo de áudio e parâmetros de formato; retorna um jobId (UUID). " +
                    "O processamento é assíncrono: consulte o status com o jobId; quando CONCLUIDO o arquivo convertido estará disponível para download."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Job aceito para processamento"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida", content = @Content(schema = @Schema(implementation = com.slicex.audioconverter.controller.exception.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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
        @Operation(summary = "Consulta status da conversão", description = "Consulta o status do job de conversão identificado por jobId (UUID).")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Status retornado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Job não encontrado", content = @Content(schema = @Schema(implementation = com.slicex.audioconverter.controller.exception.ErrorResponse.class)))
        })
        public ResponseEntity<AudioConversionJobResponse> getStatus(@PathVariable String id) {
                return ResponseEntity.ok(queueService.getStatus(id));
        }

    @GetMapping("/{id}/download")
    @Operation(summary = "Baixar arquivo convertido", description = "Baixa o arquivo convertido quando o job estiver em estado CONCLUIDO.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Arquivo retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Arquivo não encontrado para o job informado", content = @Content(schema = @Schema(implementation = com.slicex.audioconverter.controller.exception.ErrorResponse.class)))
    })
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