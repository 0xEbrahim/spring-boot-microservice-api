package com._xibrahim.loans.service;

import com._xibrahim.loans.dto.LoanDto;
import com._xibrahim.loans.dto.LoanResponseDto;

import java.util.List;

public interface ILoanService {

    LoanResponseDto createLoan(LoanDto loanDto);

    LoanResponseDto fetchLoan(Integer loanId);

    List<LoanResponseDto> fetchLoans();

    List<LoanResponseDto> fetchLoansByMobileNumber(String mobileNumber);

    LoanResponseDto updateLoan(Integer loanId, LoanDto loanDto);

    void deleteLoan(Integer loanId);
}
