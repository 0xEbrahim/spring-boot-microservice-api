package com._xibrahim.loans.controller;

import com._xibrahim.loans.constant.LoanConstant;
import com._xibrahim.loans.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<ResponseDto<Map<String, String>>> home() {
        Map<String, String> links = new LinkedHashMap<>();
        links.put("loans", "/api/loans");
        links.put("swagger", "/swagger-ui.html");
        links.put("h2Console", "/h2-console");

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDto.success(LoanConstant.STATUS_200, "Loans service is running", links));
    }
}
