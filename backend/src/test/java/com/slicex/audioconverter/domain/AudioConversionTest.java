package com.slicex.audioconverter.domain;

import com.slicex.audioconverter.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AudioConversionTest {

    @Test
    void deveCriarConversaoComStatusPendente() {
        AudioConversion conversion = AudioConversion.create("musica.mp3", AudioFormat.MP3, AudioFormat.WAV);

        assertNotNull(conversion);
        assertEquals(AudioConversionStatus.PENDENTE, conversion.getStatusDaConversao());
    }

    @Test
    void deveMarcarConversaoComoEmProcessamento() {
        AudioConversion conversion = AudioConversion.create("musica.mp3", AudioFormat.MP3, AudioFormat.WAV);

        conversion.iniciarProcessamento();

        assertEquals(AudioConversionStatus.EM_PROCESSAMENTO, conversion.getStatusDaConversao());
    }

    @Test
    void deveConcluirConversaoAposProcessamento() {
        AudioConversion conversion = AudioConversion.create("musica.mp3", AudioFormat.MP3, AudioFormat.WAV);

        conversion.iniciarProcessamento();
        conversion.concluir();

        assertEquals(AudioConversionStatus.CONCLUIDO, conversion.getStatusDaConversao());
    }

    @Test
    void deveMarcarConversaoComoFALHOU() {
        AudioConversion conversion = AudioConversion.create("musica.mp3", AudioFormat.MP3, AudioFormat.WAV);

        conversion.falhar();

        assertEquals(AudioConversionStatus.FALHOU, conversion.getStatusDaConversao());
    }

    @Test
    void deveLancarAoFalharConversaoQuandoJaEstiverConcluida() {
        AudioConversion conversion = AudioConversion.create("musica.mp3", AudioFormat.MP3, AudioFormat.WAV);

        conversion.iniciarProcessamento();
        conversion.concluir();

        DomainException exception = assertThrows(DomainException.class, conversion::falhar);

        assertEquals("A conversão só pode falhar se não estiver concluída.", exception.getMessage());
    }
}
