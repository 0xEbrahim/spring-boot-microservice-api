package com._xibrahim.cards.controller;

import com._xibrahim.cards.constant.CardConstant;
import com._xibrahim.cards.dto.CardDto;
import com._xibrahim.cards.dto.CardResponseDto;
import com._xibrahim.cards.dto.ResponseDto;
import com._xibrahim.cards.service.ICardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Tag(name = "Cards", description = "CRUD operations for customer cards.")
public class CardController {

    private final ICardService cardService;

    @Operation(summary = "Create card", description = "Creates a card and generates a unique card number.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Card creation payload.",
            content = @Content(
                    schema = @Schema(implementation = CardDto.class),
                    examples = @ExampleObject(value = "{\"mobileNumber\":\"01012345678\",\"cardType\":\"Credit\",\"totalLimit\":100000,\"amountUsed\":25000}")
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Card created successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/cards")
    public ResponseEntity<ResponseDto<CardResponseDto>> createCard(@Valid @RequestBody CardDto cardDto) {
        CardResponseDto card = cardService.createCard(cardDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(CardConstant.STATUS_201, CardConstant.MESSAGE_201, card));
    }

    @Operation(summary = "Fetch card", description = "Returns one card by card id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card returned successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card id", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/cards/{cardId}")
    public ResponseEntity<ResponseDto<CardResponseDto>> fetchCard(
            @Parameter(description = "Card id.", example = "1", required = true)
            @PathVariable @Positive(message = "Card id must be positive") Integer cardId) {
        CardResponseDto card = cardService.fetchCard(cardId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(CardConstant.STATUS_200, CardConstant.MESSAGE_200, card));
    }

    @Operation(summary = "Fetch cards", description = "Returns all cards.")
    @ApiResponse(responseCode = "200", description = "Cards returned successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/cards")
    public ResponseEntity<ResponseDto<List<CardResponseDto>>> fetchCards() {
        List<CardResponseDto> cards = cardService.fetchCards();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(CardConstant.STATUS_200, CardConstant.MESSAGE_200, cards));
    }

    @Operation(summary = "Fetch cards by mobile number", description = "Returns cards linked to a mobile number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cards returned successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid mobile number", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/cards/mobile/{mobileNumber}")
    public ResponseEntity<ResponseDto<List<CardResponseDto>>> fetchCardsByMobileNumber(
            @Parameter(description = "Customer mobile number.", example = "01012345678", required = true)
            @PathVariable @Pattern(regexp = "^[0-9]{10,15}$", message = "Mobile number must contain 10 to 15 digits") String mobileNumber) {
        List<CardResponseDto> cards = cardService.fetchCardsByMobileNumber(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(CardConstant.STATUS_200, CardConstant.MESSAGE_200, cards));
    }

    @Operation(summary = "Update card", description = "Replaces a card's editable fields and recalculates available amount.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Card update payload.",
            content = @Content(
                    schema = @Schema(implementation = CardDto.class),
                    examples = @ExampleObject(value = "{\"mobileNumber\":\"01012345678\",\"cardType\":\"Credit\",\"totalLimit\":120000,\"amountUsed\":30000}")
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card updated successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PutMapping("/cards/{cardId}")
    public ResponseEntity<ResponseDto<CardResponseDto>> updateCard(
            @Parameter(description = "Card id.", example = "1", required = true)
            @PathVariable @Positive(message = "Card id must be positive") Integer cardId,
            @Valid @RequestBody CardDto cardDto) {
        CardResponseDto card = cardService.updateCard(cardId, cardDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(CardConstant.STATUS_200, CardConstant.MESSAGE_200, card));
    }

    @Operation(summary = "Delete card", description = "Deletes one card by card id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card deleted successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card id", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<ResponseDto<Void>> deleteCard(
            @Parameter(description = "Card id.", example = "1", required = true)
            @PathVariable @Positive(message = "Card id must be positive") Integer cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(CardConstant.STATUS_200, CardConstant.MESSAGE_DELETE, null));
    }
}
