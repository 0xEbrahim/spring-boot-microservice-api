package com._xibrahim.accounts.controller;

import com._xibrahim.accounts.constant.AccountConstant;
import com._xibrahim.accounts.dto.AccountDto;
import com._xibrahim.accounts.dto.AccountResponseDto;
import com._xibrahim.accounts.dto.ResponseDto;
import com._xibrahim.accounts.service.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Accounts", description = "CRUD operations for bank accounts owned by customers.")
public class AccountController {

    private final IAccountService accountService;

    @Operation(summary = "Create account", description = "Creates a new account for an existing customer. A customer can own multiple accounts.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Account creation payload.",
            content = @Content(
                    schema = @Schema(implementation = AccountDto.class),
                    examples = @ExampleObject(value = "{\"type\":\"Savings\",\"branchAddress\":\"123 Main Street, New York\"}")
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":true,\"statusCode\":\"201\",\"statusMsg\":\"Account created successfully\",\"data\":{\"number\":1234567890,\"type\":\"Savings\",\"branchAddress\":\"123 Main Street, New York\",\"customerName\":\"Ibrahim Elsayed\"},\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"400\",\"statusMsg\":\"Validation failed\",\"data\":{\"type\":\"Account type is required\"},\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"404\",\"statusMsg\":\"Customer not found with id: 1\",\"data\":null,\"timestamp\":\"2026-07-19T15:30:00\"}")))
    })
    @PostMapping("/customers/{customerId}/accounts")
    public ResponseEntity<ResponseDto<AccountResponseDto>> createAccount(
            @Parameter(description = "Customer id that will own the new account.", example = "1", required = true)
            @PathVariable @Positive(message = "Customer id must be positive") Long customerId,
            @Valid @RequestBody AccountDto accountDto) {
        AccountResponseDto account = accountService.createAccount(customerId, accountDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(AccountConstant.STATUS_201, AccountConstant.MESSAGE_201, account));
    }

    @Operation(summary = "Fetch account", description = "Returns one account by account number, enriched with the owning customer's name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account returned successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":true,\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\",\"data\":{\"number\":1234567890,\"type\":\"Savings\",\"branchAddress\":\"123 Main Street, New York\",\"customerName\":\"Ibrahim Elsayed\"},\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid account number", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"400\",\"statusMsg\":\"Validation failed\",\"data\":{\"fetchAccount.number\":\"Account number must be positive\"},\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"404\",\"statusMsg\":\"Account not found with number: 1234567890\",\"data\":null,\"timestamp\":\"2026-07-19T15:30:00\"}")))
    })
    @GetMapping("/accounts/{number}")
    public ResponseEntity<ResponseDto<AccountResponseDto>> fetchAccount(
            @Parameter(description = "Account number.", example = "1234567890", required = true)
            @PathVariable @Positive(message = "Account number must be positive") Long number) {
        AccountResponseDto account = accountService.fetchAccount(number);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(AccountConstant.STATUS_200, AccountConstant.MESSAGE_200, account));
    }

    @Operation(summary = "Fetch accounts", description = "Returns all accounts, each enriched with the owning customer's name.")
    @ApiResponse(responseCode = "200", description = "Accounts returned successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":true,\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\",\"data\":[{\"number\":1234567890,\"type\":\"Savings\",\"branchAddress\":\"123 Main Street, New York\",\"customerName\":\"Ibrahim Elsayed\"}],\"timestamp\":\"2026-07-19T15:30:00\"}")))
    @GetMapping("/accounts")
    public ResponseEntity<ResponseDto<List<AccountResponseDto>>> fetchAccounts() {
        List<AccountResponseDto> accounts = accountService.fetchAccounts();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(AccountConstant.STATUS_200, AccountConstant.MESSAGE_200, accounts));
    }

    @Operation(summary = "Update account", description = "Replaces an account's editable fields and can move the account to another existing customer.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Account update payload.",
            content = @Content(
                    schema = @Schema(implementation = AccountDto.class),
                    examples = @ExampleObject(value = "{\"type\":\"Current\",\"branchAddress\":\"456 Nile Street, Cairo\"}")
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account updated successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":true,\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\",\"data\":{\"number\":1234567890,\"type\":\"Current\",\"branchAddress\":\"456 Nile Street, Cairo\",\"customerName\":\"Ibrahim Elsayed\"},\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"400\",\"statusMsg\":\"Validation failed\",\"data\":{\"branchAddress\":\"Branch address is required\"},\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Account or customer not found", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"404\",\"statusMsg\":\"Account not found with number: 1234567890\",\"data\":null,\"timestamp\":\"2026-07-19T15:30:00\"}")))
    })
    @PutMapping("/customers/{customerId}/accounts/{number}")
    public ResponseEntity<ResponseDto<AccountResponseDto>> updateAccount(
            @Parameter(description = "Customer id that will own the account.", example = "1", required = true)
            @PathVariable @Positive(message = "Customer id must be positive") Long customerId,
            @Parameter(description = "Account number.", example = "1234567890", required = true)
            @PathVariable @Positive(message = "Account number must be positive") Long number,
            @Valid @RequestBody AccountDto accountDto) {
        AccountResponseDto account = accountService.updateAccount(customerId, number, accountDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(AccountConstant.STATUS_200, AccountConstant.MESSAGE_200, account));
    }

    @Operation(summary = "Delete account", description = "Deletes one account by account number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account deleted successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":true,\"statusCode\":\"200\",\"statusMsg\":\"Account deleted successfully\",\"data\":null,\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid account number", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"400\",\"statusMsg\":\"Validation failed\",\"data\":{\"deleteAccount.number\":\"Account number must be positive\"},\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"404\",\"statusMsg\":\"Account not found with number: 1234567890\",\"data\":null,\"timestamp\":\"2026-07-19T15:30:00\"}")))
    })
    @DeleteMapping("/accounts/{number}")
    public ResponseEntity<ResponseDto<Void>> deleteAccount(
            @Parameter(description = "Account number.", example = "1234567890", required = true)
            @PathVariable @Positive(message = "Account number must be positive") Long number) {
        accountService.deleteAccount(number);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(AccountConstant.STATUS_200, AccountConstant.MESSAGE_DELETE, null));
    }
}
