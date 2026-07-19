package com._xibrahim.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Customer", description = "Customer payload used for creating, updating, and returning customer records.")
public class CustomerDto {
    @Schema(description = "Unique customer identifier.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Schema(description = "Customer full name.", example = "Ibrahim Elsayed", maxLength = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    @Schema(description = "Unique customer email address.", example = "ibrahim@example.com", maxLength = 150)
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Mobile number must contain 10 to 15 digits and may start with +")
    @Schema(description = "Customer mobile number. It must contain 10 to 15 digits and may start with +.", example = "+201001112222")
    private String mobileNumber;
}
