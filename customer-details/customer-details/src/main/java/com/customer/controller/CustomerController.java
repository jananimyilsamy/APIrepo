package com.customer.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;

import com.customer.entity.CustomerDetails;

public interface CustomerController {

	ResponseEntity<CustomerDetails> createCustomer(CustomerDetails customer);

	ResponseEntity<CustomerDetails> getCustomerById(Long id);

	ResponseEntity<List<CustomerDetails>> getAllCustomers() throws InterruptedException, ExecutionException;

	ResponseEntity<CustomerDetails> updateCustomer(Long id, CustomerDetails customer);

	ResponseEntity<String> deleteCustomer(Long id);

	ResponseEntity<List<String>> checkExperienceInCountry(String country);

	ResponseEntity<List<CustomerDetails>> findCustomerWithExpLessThanFive(String Designation);

}
