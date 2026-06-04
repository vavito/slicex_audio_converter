package com.slicex.audioconverter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Slicex Audio Converter API")
                        .version("1.0.0")
                        .description("API REST para conversão assíncrona de arquivos de áudio usando FFmpeg nativo no servidor."));
    }

}
