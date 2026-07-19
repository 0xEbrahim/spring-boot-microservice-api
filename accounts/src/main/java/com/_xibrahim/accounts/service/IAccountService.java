package com._xibrahim.accounts.service;

import com._xibrahim.accounts.dto.AccountDto;
import com._xibrahim.accounts.dto.AccountResponseDto;

import java.util.List;

public interface IAccountService {

    AccountResponseDto createAccount(Long customerId, AccountDto accountDto);

    AccountResponseDto fetchAccount(Long number);

    List<AccountResponseDto> fetchAccounts();

    AccountResponseDto updateAccount(Long customerId, Long number, AccountDto accountDto);

    void deleteAccount(Long number);
}
