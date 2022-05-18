package com.marcode.ebanking.web;


import com.marcode.ebanking.dtos.CustomerDTO;
import com.marcode.ebanking.entities.Customer;
import com.marcode.ebanking.exceptions.CustomerNotFoundException;
import com.marcode.ebanking.services.BankAccountService;
import com.marcode.ebanking.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/v1/customers")
public class CustomerRestController {

    private final CustomerService customerService;

    @GetMapping()
    public List<CustomerDTO> getCustomers(){
        return customerService.listCustomer();
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {

        return customerService.getCustomer(customerId);
    }

    @PostMapping()
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        return customerService.saveCustomer(customerDTO);
    }

    @PutMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable(name = "id")Long customerId,  @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        customerDTO.setId(customerId);
        return customerService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable Long customerId){
        customerService.deleteCustomer(customerId);
    }
}
