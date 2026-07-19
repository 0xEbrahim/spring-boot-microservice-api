package com._xibrahim.accounts.service.impl;

import com._xibrahim.accounts.dto.CustomerDto;
import com._xibrahim.accounts.entity.Customer;
import com._xibrahim.accounts.exception.AlreadyExistsException;
import com._xibrahim.accounts.exception.NotFoundException;
import com._xibrahim.accounts.mapper.ApiMapper;
import com._xibrahim.accounts.repository.CustomerRepository;
import com._xibrahim.accounts.service.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final ApiMapper apiMapper;

    @Override
    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        customerRepository.findByEmail(customerDto.getEmail())
                .ifPresent(customer -> {
                    throw new AlreadyExistsException("Customer already exists with email: " + customerDto.getEmail());
                });

        Customer customer = apiMapper.transformFromDto(customerDto, Customer.class);
        customer.setId(null);
        Customer savedCustomer = customerRepository.save(customer);
        return apiMapper.transformToDto(savedCustomer, CustomerDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto fetchCustomer(Long id) {
        Customer customer = getCustomerById(id);
        return apiMapper.transformToDto(customer, CustomerDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> fetchCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> apiMapper.transformToDto(customer, CustomerDto.class))
                .toList();
    }

    @Override
    @Transactional
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer existingCustomer = getCustomerById(id);

        customerRepository.findByEmail(customerDto.getEmail())
                .filter(customer -> !customer.getId().equals(id))
                .ifPresent(customer -> {
                    throw new AlreadyExistsException("Customer already exists with email: " + customerDto.getEmail());
                });

        Customer customer = apiMapper.transformFromDto(customerDto, Customer.class);
        customer.setId(existingCustomer.getId());

        Customer savedCustomer = customerRepository.save(customer);
        return apiMapper.transformToDto(savedCustomer, CustomerDto.class);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    private Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));
    }
}
