package org.fasttrackit.onlineshop.service;

import org.fasttrackit.onlineshop.domain.Customer;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persistance.CustomerRepository;
import org.fasttrackit.onlineshop.transfer.SaveCustomerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    //IoC - Inversion of Control
    private final CustomerRepository customerRepository;

    //Dependency injection
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(SaveCustomerRequest request) {
        LOGGER.info("Creating customer {}", request);
        Customer customer = new Customer();
        customer.setLastName(request.getLastName());
        customer.setFirstName(request.getFirstName());

        return customerRepository.save(customer);
    }

    public Customer getCustomer(long id) {

        LOGGER.info("Retrevieng customer {}", id);
        //using optional
        return customerRepository.findById(id)
                //lambada expression
                .orElseThrow(() -> new ResourceNotFoundException("Customer " + id + " does not exist."));
    }

    public Customer updateCustomer(long id, SaveCustomerRequest request) {
        LOGGER.info("Updating customer {}: {}", id, request);
        Customer customer = getCustomer(id);
        BeanUtils.copyProperties(request, customer);

        return customerRepository.save(customer);
    }

    public void deleteCustomer(long id) {
        LOGGER.info("Deleting customer {}", id);
        customerRepository.deleteById(id);
        LOGGER.info("Deleted customer {}", id);
    }
}