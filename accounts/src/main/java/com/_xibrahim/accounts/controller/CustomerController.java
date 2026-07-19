package com._xibrahim.accounts.controller;

import com._xibrahim.accounts.constant.CustomerConstant;
import com._xibrahim.accounts.dto.CustomerDto;
import com._xibrahim.accounts.dto.ResponseDto;
import com._xibrahim.accounts.service.ICustomerService;
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
@RequestMapping(path = "api/customers", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Tag(name = "Customers", description = "CRUD operations for customer records.")
public class CustomerController {

    private final ICustomerService customerService;

    @Operation(summary = "Create customer", description = "Creates a new customer after validating the request payload and enforcing unique email addresses.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Customer creation payload.",
            content = @Content(
                    schema = @Schema(implementation = CustomerDto.class),
                    examples = @ExampleObject(value = "{\"name\":\"Ibrahim Elsayed\",\"email\":\"ibrahim@example.com\",\"mobileNumber\":\"+201001112222\"}")
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":true,\"statusCode\":\"201\",\"statusMsg\":\"Customer created successfully\",\"data\":{\"id\":1,\"name\":\"Ibrahim Elsayed\",\"email\":\"ibrahim@example.com\",\"mobileNumber\":\"+201001112222\"},\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"400\",\"statusMsg\":\"Validation failed\",\"data\":{\"email\":\"Email must be valid\"},\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "409", description = "Customer email already exists", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"409\",\"statusMsg\":\"Customer already exists with email: ibrahim@example.com\",\"data\":null,\"timestamp\":\"2026-07-19T15:30:00\"}")))
    })
    @PostMapping
    public ResponseEntity<ResponseDto<CustomerDto>> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        CustomerDto customer = customerService.createCustomer(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(CustomerConstant.STATUS_201, CustomerConstant.MESSAGE_201, customer));
    }

    @Operation(summary = "Fetch customer", description = "Returns one customer by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer returned successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":true,\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\",\"data\":{\"id\":1,\"name\":\"Ibrahim Elsayed\",\"email\":\"ibrahim@example.com\",\"mobileNumber\":\"+201001112222\"},\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid customer id", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"400\",\"statusMsg\":\"Validation failed\",\"data\":{\"fetchCustomer.id\":\"Customer id must be positive\"},\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"404\",\"statusMsg\":\"Customer not found with id: 1\",\"data\":null,\"timestamp\":\"2026-07-19T15:30:00\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<CustomerDto>> fetchCustomer(
            @Parameter(description = "Customer id.", example = "1", required = true)
            @PathVariable @Positive(message = "Customer id must be positive") Long id) {
        CustomerDto customer = customerService.fetchCustomer(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(CustomerConstant.STATUS_200, CustomerConstant.MESSAGE_200, customer));
    }

    @Operation(summary = "Fetch customers", description = "Returns all customers.")
    @ApiResponse(responseCode = "200", description = "Customers returned successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":true,\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\",\"data\":[{\"id\":1,\"name\":\"Ibrahim Elsayed\",\"email\":\"ibrahim@example.com\",\"mobileNumber\":\"+201001112222\"}],\"timestamp\":\"2026-07-19T15:30:00\"}")))
    @GetMapping
    public ResponseEntity<ResponseDto<List<CustomerDto>>> fetchCustomers() {
        List<CustomerDto> customers = customerService.fetchCustomers();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(CustomerConstant.STATUS_200, CustomerConstant.MESSAGE_200, customers));
    }

    @Operation(summary = "Update customer", description = "Replaces a customer's editable fields after validating the request payload.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Customer update payload.",
            content = @Content(
                    schema = @Schema(implementation = CustomerDto.class),
                    examples = @ExampleObject(value = "{\"name\":\"Ibrahim Elsayed\",\"email\":\"ibrahim.updated@example.com\",\"mobileNumber\":\"+201001113333\"}")
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":true,\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\",\"data\":{\"id\":1,\"name\":\"Ibrahim Elsayed\",\"email\":\"ibrahim.updated@example.com\",\"mobileNumber\":\"+201001113333\"},\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"400\",\"statusMsg\":\"Validation failed\",\"data\":{\"mobileNumber\":\"Mobile number must contain 10 to 15 digits and may start with +\"},\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"404\",\"statusMsg\":\"Customer not found with id: 1\",\"data\":null,\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "409", description = "Customer email already exists", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"409\",\"statusMsg\":\"Customer already exists with email: ibrahim.updated@example.com\",\"data\":null,\"timestamp\":\"2026-07-19T15:30:00\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<CustomerDto>> updateCustomer(
            @Parameter(description = "Customer id.", example = "1", required = true)
            @PathVariable @Positive(message = "Customer id must be positive") Long id,
            @Valid @RequestBody CustomerDto customerDto) {
        CustomerDto customer = customerService.updateCustomer(id, customerDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(CustomerConstant.STATUS_200, CustomerConstant.MESSAGE_200, customer));
    }

    @Operation(summary = "Delete customer", description = "Deletes one customer by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer deleted successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":true,\"statusCode\":\"200\",\"statusMsg\":\"Customer deleted successfully\",\"data\":null,\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid customer id", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"400\",\"statusMsg\":\"Validation failed\",\"data\":{\"deleteCustomer.id\":\"Customer id must be positive\"},\"timestamp\":\"2026-07-19T15:30:00\"}"))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(schema = @Schema(implementation = ResponseDto.class), examples = @ExampleObject(value = "{\"success\":false,\"statusCode\":\"404\",\"statusMsg\":\"Customer not found with id: 1\",\"data\":null,\"timestamp\":\"2026-07-19T15:30:00\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteCustomer(
            @Parameter(description = "Customer id.", example = "1", required = true)
            @PathVariable @Positive(message = "Customer id must be positive") Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(CustomerConstant.STATUS_200, CustomerConstant.MESSAGE_DELETE, null));
    }
}
