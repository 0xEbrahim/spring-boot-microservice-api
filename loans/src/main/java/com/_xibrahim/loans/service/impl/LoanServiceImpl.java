package com._xibrahim.loans.service.impl;

import com._xibrahim.loans.dto.LoanDto;
import com._xibrahim.loans.dto.LoanResponseDto;
import com._xibrahim.loans.entity.Loan;
import com._xibrahim.loans.exception.NotFoundException;
import com._xibrahim.loans.mapper.ApiMapper;
import com._xibrahim.loans.repository.LoanRepository;
import com._xibrahim.loans.service.ILoanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements ILoanService {

    private final LoanRepository loanRepository;
    private final ApiMapper apiMapper;

    @Override
    @Transactional
    public LoanResponseDto createLoan(LoanDto loanDto) {
        Loan loan = apiMapper.transformFromDto(loanDto, Loan.class);
        loan.setLoanNumber(generateLoanNumber());
        applyOutstandingAmount(loan);

        Loan savedLoan = loanRepository.save(loan);
        return apiMapper.transformToDto(savedLoan, LoanResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanResponseDto fetchLoan(Integer loanId) {
        Loan loan = getLoanById(loanId);
        return apiMapper.transformToDto(loan, LoanResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanResponseDto> fetchLoans() {
        return loanRepository.findAll()
                .stream()
                .map(loan -> apiMapper.transformToDto(loan, LoanResponseDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanResponseDto> fetchLoansByMobileNumber(String mobileNumber) {
        List<Loan> loans = loanRepository.findByMobileNumber(mobileNumber);
        if (loans.isEmpty()) {
            throw new NotFoundException("Loan not found with mobile number: " + mobileNumber);
        }

        return loans.stream()
                .map(loan -> apiMapper.transformToDto(loan, LoanResponseDto.class))
                .toList();
    }

    @Override
    @Transactional
    public LoanResponseDto updateLoan(Integer loanId, LoanDto loanDto) {
        Loan loan = getLoanById(loanId);
        loan.setMobileNumber(loanDto.getMobileNumber());
        loan.setLoanType(loanDto.getLoanType());
        loan.setTotalLoan(loanDto.getTotalLoan());
        loan.setAmountPaid(loanDto.getAmountPaid());
        applyOutstandingAmount(loan);

        Loan savedLoan = loanRepository.save(loan);
        return apiMapper.transformToDto(savedLoan, LoanResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteLoan(Integer loanId) {
        Loan loan = getLoanById(loanId);
        loanRepository.delete(loan);
    }

    private Loan getLoanById(Integer loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found with id: " + loanId));
    }

    private void applyOutstandingAmount(Loan loan) {
        loan.setOutstandingAmount(loan.getTotalLoan() - loan.getAmountPaid());
    }

    private String generateLoanNumber() {
        String loanNumber;
        do {
            loanNumber = String.valueOf(ThreadLocalRandom.current().nextLong(1_000_000_000_000L, 10_000_000_000_000L));
        } while (loanRepository.existsByLoanNumber(loanNumber));
        return loanNumber;
    }
}
