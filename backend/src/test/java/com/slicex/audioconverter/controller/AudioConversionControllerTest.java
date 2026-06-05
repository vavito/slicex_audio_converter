package com.slicex.audioconverter.controller;

import com.slicex.audioconverter.dto.AudioConversionJobResponse;
import com.slicex.audioconverter.domain.AudioConversionStatus;
import com.slicex.audioconverter.domain.exception.DomainException;
import com.slicex.audioconverter.service.AudioFileValidationService;
import com.slicex.audioconverter.service.queue.AudioConversionQueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AudioConversionControllerTest {

    private MockMvc mockMvc;
    private AudioConversionQueueService queueService;
    private AudioFileValidationService validationService;

    @BeforeEach
    void setup() {
        queueService = mock(AudioConversionQueueService.class);
        validationService = mock(AudioFileValidationService.class);
        AudioConversionController controller = new AudioConversionController(queueService, validationService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new com.slicex.audioconverter.controller.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    void deveRetornarAcceptedEJobIdAoCriarConversao() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "musica.mp3",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "conteudo".getBytes()
        );

        AudioConversionJobResponse response = new AudioConversionJobResponse(
                "job-id",
                AudioConversionStatus.PENDENTE,
                "Conversão adicionada à fila."
        );

        when(queueService.enqueue(any(), any())).thenReturn(response);

        mockMvc.perform(multipart("/api/audio/conversions")
                        .file(file)
                        .param("nomeDoArquivoOriginal", "musica.mp3")
                        .param("formatoPreConversao", "MP3")
                        .param("formatoPosConversao", "WAV")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value("job-id"))
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    void deveRetornarStatusQuandoJobExistir() throws Exception {
        AudioConversionJobResponse response = new AudioConversionJobResponse(
                "job-id",
                AudioConversionStatus.EM_PROCESSAMENTO,
                "Em processamento"
        );

        when(queueService.getStatus("job-id")).thenReturn(response);

        mockMvc.perform(get("/api/audio/conversions/job-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("job-id"))
                .andExpect(jsonPath("$.status").value("EM_PROCESSAMENTO"));
    }

    @Test
    void deveRetornarBadRequestQuandoJobNaoExistir() throws Exception {
        when(queueService.getStatus("wrong-id"))
                .thenThrow(new DomainException("Conversão não encontrada."));

        mockMvc.perform(get("/api/audio/conversions/wrong-id"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Conversão não encontrada."));
    }

    @Test
    void deveRetornarBadRequestQuandoArquivoInvalido() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "musica.mp3",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "conteudo".getBytes()
        );

        doThrow(new DomainException("O arquivo de áudio é obrigatório."))
                .when(validationService).validate(any(), any());

        mockMvc.perform(multipart("/api/audio/conversions")
                        .file(file)
                        .param("nomeDoArquivoOriginal", "musica.mp3")
                        .param("formatoPreConversao", "MP3")
                        .param("formatoPosConversao", "WAV")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O arquivo de áudio é obrigatório."));
    }
}
