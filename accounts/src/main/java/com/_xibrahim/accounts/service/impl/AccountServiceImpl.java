package com._xibrahim.accounts.service.impl;

import com._xibrahim.accounts.dto.AccountDto;
import com._xibrahim.accounts.dto.AccountResponseDto;
import com._xibrahim.accounts.entity.Account;
import com._xibrahim.accounts.entity.Customer;
import com._xibrahim.accounts.exception.NotFoundException;
import com._xibrahim.accounts.mapper.ApiMapper;
import com._xibrahim.accounts.repository.AccountRepository;
import com._xibrahim.accounts.repository.CustomerRepository;
import com._xibrahim.accounts.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final ApiMapper apiMapper;

    @Override
    @Transactional
    public AccountResponseDto createAccount(Long customerId, AccountDto accountDto) {
        Customer customer = getCustomerById(customerId);

        Account account = apiMapper.transformFromDto(accountDto, Account.class);
        account.setNumber(generateAccountNumber());
        account.setCustomer(customer);

        Account savedAccount = accountRepository.save(account);

        return apiMapper.transformToDto(savedAccount, AccountResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDto fetchAccount(Long number) {
        Account account = getAccountByNumber(number);
        return apiMapper.transformToDto(account, AccountResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDto> fetchAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(account -> apiMapper.transformToDto(account, AccountResponseDto.class))
                .toList();
    }

    @Override
    @Transactional
    public AccountResponseDto updateAccount(Long customerId, Long number, AccountDto accountDto) {
        getAccountByNumber(number);
        Customer customer = getCustomerById(customerId);

        Account account = apiMapper.transformFromDto(accountDto, Account.class);
        account.setNumber(number);
        account.setCustomer(customer);

        Account savedAccount = accountRepository.save(account);
        return apiMapper.transformToDto(savedAccount, AccountResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteAccount(Long number) {
        Account account = getAccountByNumber(number);
        accountRepository.delete(account);
    }

    private Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + customerId));
    }

    private Account getAccountByNumber(Long number) {
        return accountRepository.findById(number)
                .orElseThrow(() -> new NotFoundException("Account not found with number: " + number));
    }

    private Long generateAccountNumber() {
        Long number;
        do {
            number = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
        } while (accountRepository.existsById(number));
        return number;
    }
}
