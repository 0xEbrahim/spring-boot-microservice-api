package com._xibrahim.loans.repository;

import com._xibrahim.loans.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

    boolean existsByLoanNumber(String loanNumber);

    List<Loan> findByMobileNumber(String mobileNumber);
}
