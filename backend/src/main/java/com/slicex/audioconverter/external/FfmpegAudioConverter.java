package com.slicex.audioconverter.external;

import com.slicex.audioconverter.domain.AudioFormat;
import com.slicex.audioconverter.domain.exception.DomainException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

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
            int exitCode = process.waitFor();

            if (exitCode != 0 || !outputFile.exists()) {
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