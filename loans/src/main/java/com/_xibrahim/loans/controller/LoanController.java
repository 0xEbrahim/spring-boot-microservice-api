package com._xibrahim.loans.controller;

import com._xibrahim.loans.constant.LoanConstant;
import com._xibrahim.loans.dto.LoanDto;
import com._xibrahim.loans.dto.LoanResponseDto;
import com._xibrahim.loans.dto.ResponseDto;
import com._xibrahim.loans.service.ILoanService;
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
@Tag(name = "Loans", description = "CRUD operations for customer loans.")
public class LoanController {

    private final ILoanService loanService;

    @Operation(summary = "Create loan", description = "Creates a loan and generates a unique loan number.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Loan creation payload.",
            content = @Content(
                    schema = @Schema(implementation = LoanDto.class),
                    examples = @ExampleObject(value = "{\"mobileNumber\":\"01012345678\",\"loanType\":\"Home\",\"totalLoan\":500000,\"amountPaid\":125000}")
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Loan created successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/loans")
    public ResponseEntity<ResponseDto<LoanResponseDto>> createLoan(@Valid @RequestBody LoanDto loanDto) {
        LoanResponseDto loan = loanService.createLoan(loanDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(LoanConstant.STATUS_201, LoanConstant.MESSAGE_201, loan));
    }

    @Operation(summary = "Fetch loan", description = "Returns one loan by loan id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loan returned successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid loan id", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/loans/{loanId}")
    public ResponseEntity<ResponseDto<LoanResponseDto>> fetchLoan(
            @Parameter(description = "Loan id.", example = "1", required = true)
            @PathVariable @Positive(message = "Loan id must be positive") Integer loanId) {
        LoanResponseDto loan = loanService.fetchLoan(loanId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(LoanConstant.STATUS_200, LoanConstant.MESSAGE_200, loan));
    }

    @Operation(summary = "Fetch loans", description = "Returns all loans.")
    @ApiResponse(responseCode = "200", description = "Loans returned successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    @GetMapping("/loans")
    public ResponseEntity<ResponseDto<List<LoanResponseDto>>> fetchLoans() {
        List<LoanResponseDto> loans = loanService.fetchLoans();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(LoanConstant.STATUS_200, LoanConstant.MESSAGE_200, loans));
    }

    @Operation(summary = "Fetch loans by mobile number", description = "Returns loans linked to a mobile number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loans returned successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid mobile number", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/loans/mobile/{mobileNumber}")
    public ResponseEntity<ResponseDto<List<LoanResponseDto>>> fetchLoansByMobileNumber(
            @Parameter(description = "Customer mobile number.", example = "01012345678", required = true)
            @PathVariable @Pattern(regexp = "^[0-9]{10,15}$", message = "Mobile number must contain 10 to 15 digits") String mobileNumber) {
        List<LoanResponseDto> loans = loanService.fetchLoansByMobileNumber(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(LoanConstant.STATUS_200, LoanConstant.MESSAGE_200, loans));
    }

    @Operation(summary = "Update loan", description = "Replaces a loan's editable fields and recalculates outstanding amount.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Loan update payload.",
            content = @Content(
                    schema = @Schema(implementation = LoanDto.class),
                    examples = @ExampleObject(value = "{\"mobileNumber\":\"01012345678\",\"loanType\":\"Home\",\"totalLoan\":500000,\"amountPaid\":150000}")
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loan updated successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PutMapping("/loans/{loanId}")
    public ResponseEntity<ResponseDto<LoanResponseDto>> updateLoan(
            @Parameter(description = "Loan id.", example = "1", required = true)
            @PathVariable @Positive(message = "Loan id must be positive") Integer loanId,
            @Valid @RequestBody LoanDto loanDto) {
        LoanResponseDto loan = loanService.updateLoan(loanId, loanDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(LoanConstant.STATUS_200, LoanConstant.MESSAGE_200, loan));
    }

    @Operation(summary = "Delete loan", description = "Deletes one loan by loan id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loan deleted successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid loan id", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @DeleteMapping("/loans/{loanId}")
    public ResponseEntity<ResponseDto<Void>> deleteLoan(
            @Parameter(description = "Loan id.", example = "1", required = true)
            @PathVariable @Positive(message = "Loan id must be positive") Integer loanId) {
        loanService.deleteLoan(loanId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(LoanConstant.STATUS_200, LoanConstant.MESSAGE_DELETE, null));
    }
}
