package com.devsuperior.dsmovie.config;

import io.swagger.v3.oas.annotations.OpenAPI31;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class OpenAPIConfig {

    @Bean
    public OpenAPI dsmovieAPI(){
        return new OpenAPI()
                .info(new Info()
                    .title("DSMovie API")
                    .description("DSMovie Reference Project")
                    .version("v0.0.1")
                    .license(new License()
                        .name("Apache 2.0")
                        .url("https://github.com/7E0n4Rd0/dsmovie-ref")
                    )
                );
    }
}
