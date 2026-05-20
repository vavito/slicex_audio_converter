package com.slicex.audioconverter.domain;

import com.slicex.audioconverter.domain.exception.DomainException;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AudioConversion {
    private String nomeDoArquivoOriginal;
    private AudioFormat formatoPreConversao;
    private AudioFormat formatoPosConversao;
    private AudioConversionStatus statusDaConversao;

    public static AudioConversion create(
            String nomeDoArquivoOriginal,
            AudioFormat formatoPreConversao,
            AudioFormat formatoPosConversao
    ) {
        if (nomeDoArquivoOriginal == null || nomeDoArquivoOriginal.isBlank()) {
            throw new DomainException("O nome do arquivo original é obrigatório.");
        }

        if (formatoPreConversao == null) {
            throw new DomainException("O formato pré-conversão é obrigatório.");
        }

        if (formatoPosConversao == null) {
            throw new DomainException("O formato pós-conversão é obrigatório.");
        }

        if (formatoPreConversao == formatoPosConversao) {
            throw new DomainException("O formato de origem e destino não podem ser iguais.");
        }

        AudioConversion audioConversion = new AudioConversion();
        audioConversion.nomeDoArquivoOriginal = nomeDoArquivoOriginal;
        audioConversion.formatoPreConversao = formatoPreConversao;
        audioConversion.formatoPosConversao = formatoPosConversao;
        audioConversion.statusDaConversao = AudioConversionStatus.PENDENTE;

        return audioConversion;
    }

    public void mudarFormatoPreConversao(AudioFormat formatoPreConversao) {
        if (formatoPreConversao == null) {
            throw new DomainException("O formato pré-conversão é obrigatório");
        }
        else if (formatoPreConversao == this.formatoPosConversao) {
            throw new DomainException("O formato de origem e destino não podem ser iguais.");
        }
        this.formatoPreConversao = formatoPreConversao;
    }

    public void mudarFormatoPosConversao(AudioFormat formatoPosConversao) {
        if (formatoPosConversao == null) {
            throw new DomainException("O formato pós-conversão é obrigatório");
        }
        else if (formatoPosConversao == this.formatoPreConversao) {
            throw new DomainException("O formato de origem e destino não podem ser iguais.");
        }
        this.formatoPosConversao = formatoPosConversao;
    }

    public void iniciarProcessamento() {
        this.statusDaConversao = AudioConversionStatus.EM_PROCESSAMENTO;
    }

    public void concluir() {
        this.statusDaConversao = AudioConversionStatus.CONCLUIDO;
    }

    public void falhar() {
        this.statusDaConversao = AudioConversionStatus.FALHOU;
    }
}
