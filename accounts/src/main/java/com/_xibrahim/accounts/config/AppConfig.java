package com._xibrahim.accounts.config;

import com._xibrahim.accounts.dto.AccountResponseDto;
import com._xibrahim.accounts.entity.Account;
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
                title = "Accounts Microservice API",
                version = "v1",
                description = "REST API documentation for customer and account management.",
                contact = @Contact(name = "Accounts API Support", email = "support@accounts.local"),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")
        ),
        servers = {
                @Server(url = "http://localhost:5000", description = "Local development server")
        }
)
public class AppConfig {

    @Bean
    public ModelMapper createModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Account.class, AccountResponseDto.class)
                .addMappings(mapper -> mapper.map(source -> source.getCustomer().getName(), AccountResponseDto::setCustomerName));
        return modelMapper;
    }
}
