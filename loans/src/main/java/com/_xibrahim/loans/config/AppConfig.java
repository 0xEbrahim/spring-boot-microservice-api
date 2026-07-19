package com._xibrahim.loans.config;

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
                title = "Loans Microservice API",
                version = "v1",
                description = "REST API documentation for loan management.",
                contact = @Contact(name = "Loans API Support", email = "support@loans.local"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")
        ),
        servers = {
                @Server(url = "http://localhost:7000", description = "Local development server")
        }
)
public class AppConfig {

    @Bean
    public ModelMapper createModelMapper() {
        return new ModelMapper();
    }
}
