package com._xibrahim.cards.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Cards Microservice API",
                version = "v1",
                description = "REST API documentation for card management.",
                contact = @Contact(name = "Cards API Support", email = "support@cards.local"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")
        ),
        servers = {
                @Server(url = "http://localhost:6000", description = "Local development server")
        }
)
public class AppConfig {

    @Bean
    public ModelMapper createModelMapper() {
        return new ModelMapper();
    }
}
