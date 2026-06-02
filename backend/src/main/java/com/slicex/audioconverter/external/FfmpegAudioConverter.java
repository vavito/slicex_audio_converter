package com.slicex.audioconverter.external;

import lombok.extern.slf4j.Slf4j;
import com.slicex.audioconverter.domain.AudioFormat;
import com.slicex.audioconverter.domain.exception.DomainException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class FfmpegAudioConverter {

    public File convert(File inputFile, AudioFormat formatoDestino, String nomeDoArquivoOriginal) {
        try {
            validarNomeDoArquivo(nomeDoArquivoOriginal);

            String extensaoDestino = formatoDestino.name().toLowerCase();
            String nomeSemExtensao = nomeDoArquivoOriginal.substring(
                    0,
                    nomeDoArquivoOriginal.lastIndexOf(".")
            );

            File outputFile = new File(
                    inputFile.getParentFile(),
                    nomeSemExtensao + "-converted." + extensaoDestino
            );

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ffmpeg",
                    "-y",
                    "-i",
                    inputFile.getAbsolutePath(),
                    outputFile.getAbsolutePath()
            );

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            boolean finished = process.waitFor(2, TimeUnit.MINUTES);

            String logs = new String(process.getInputStream().readAllBytes());

            if (!finished) {
                process.destroyForcibly();
                log.error("Timeout ao executar FFmpeg. Logs: {}", logs);
                throw new DomainException("A conversão demorou demais e foi cancelada.");
            }

            int exitCode = process.exitValue();

            if (exitCode != 0 || !outputFile.exists()) {
                log.error("Erro ao executar o FFmpeg. Exit code: {}. Logs: {}", exitCode, logs);
                throw new DomainException("Erro ao converter o áudio com FFmpeg.");
            }

            return outputFile;

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DomainException("Não foi possível executar o FFmpeg.");
        }
    }

    private void validarNomeDoArquivo(String nomeDoArquivoOriginal) {
        if (nomeDoArquivoOriginal == null || nomeDoArquivoOriginal.isBlank()) {
            throw new DomainException("O nome do arquivo original é obrigatório.");
        }

        if (!nomeDoArquivoOriginal.contains(".")) {
            throw new DomainException("Nome do arquivo original inválido.");
        }
    }
}