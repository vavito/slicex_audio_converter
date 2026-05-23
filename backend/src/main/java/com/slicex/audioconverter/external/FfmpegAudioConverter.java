package com.slicex.audioconverter.external;

import com.slicex.audioconverter.domain.AudioFormat;
import com.slicex.audioconverter.domain.exception.DomainException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class FfmpegAudioConverter {

    public File convert(File inputFile, AudioFormat formatoDestino) {
        try {
            File outputFile = Files.createTempFile(
                    "converted-",
                    "." + formatoDestino.name().toLowerCase()
            ).toFile();

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
}