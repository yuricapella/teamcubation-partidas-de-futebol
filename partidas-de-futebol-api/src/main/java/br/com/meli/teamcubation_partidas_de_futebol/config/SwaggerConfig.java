package br.com.meli.teamcubation_partidas_de_futebol.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Partidas de Futebol")
                        .version("v1.0.0")
                        .description("Documentação da API de partidas de futebol")
                        .contact(new Contact().name("Yuri Capella").email("email@email.com"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Veja mais sobre o projeto")
                        .url("https://github.com/yuricapella/teamcubation-partidas-de-futebol"));
    }
}
