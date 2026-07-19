package com._xibrahim.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "AccountRequest", description = "Account payload used for creating and updating account records.")
public class AccountDto {
    @NotBlank(message = "Account type is required")
    @Size(max = 50, message = "Account type must not exceed 50 characters")
    @Schema(description = "Account type.", example = "Savings", maxLength = 50)
    private String type;

    @NotBlank(message = "Branch address is required")
    @Size(max = 255, message = "Branch address must not exceed 255 characters")
    @Schema(description = "Branch address attached to the account.", example = "123 Main Street, New York", maxLength = 255)
    private String branchAddress;
}
