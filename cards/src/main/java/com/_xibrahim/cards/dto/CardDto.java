package com._xibrahim.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "CardRequest", description = "Card payload used for creating and updating card records.")
public class CardDto {

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Mobile number must contain 10 to 15 digits")
    @Schema(description = "Customer mobile number linked to the card.", example = "01012345678", minLength = 10, maxLength = 15)
    private String mobileNumber;

    @NotBlank(message = "Card type is required")
    @Size(max = 100, message = "Card type must not exceed 100 characters")
    @Schema(description = "Card type.", example = "Credit", maxLength = 100)
    private String cardType;

    @NotNull(message = "Total limit is required")
    @Positive(message = "Total limit must be positive")
    @Schema(description = "Total spending limit assigned to the card.", example = "100000")
    private Integer totalLimit;

    @NotNull(message = "Amount used is required")
    @PositiveOrZero(message = "Amount used must be zero or positive")
    @Schema(description = "Amount already used from the card limit.", example = "25000")
    private Integer amountUsed;

    @AssertTrue(message = "Amount used must not exceed total limit")
    @Schema(hidden = true)
    public boolean isAmountUsedWithinTotalLimit() {
        return totalLimit == null || amountUsed == null || amountUsed <= totalLimit;
    }
}
