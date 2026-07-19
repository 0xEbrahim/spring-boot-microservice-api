package com._xibrahim.accounts.service;

import com._xibrahim.accounts.dto.CustomerDto;

import java.util.List;

public interface ICustomerService {

    CustomerDto createCustomer(CustomerDto customerDto);

    CustomerDto fetchCustomer(Long id);

    List<CustomerDto> fetchCustomers();

    CustomerDto updateCustomer(Long id, CustomerDto customerDto);

    void deleteCustomer(Long id);
}
