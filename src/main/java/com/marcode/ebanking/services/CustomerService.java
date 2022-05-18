package com.marcode.ebanking.services;

import com.marcode.ebanking.dtos.CustomerDTO;
import com.marcode.ebanking.entities.Customer;
import com.marcode.ebanking.exceptions.CustomerNotFoundException;
import com.marcode.ebanking.repositories.CustomerRepository;

import java.util.List;

public interface CustomerService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);
    List<CustomerDTO> listCustomer();

    Customer findCustomer(Long customerId) throws CustomerNotFoundException;
}
