package com.customer.service;

import com.customer.entity.CustomerDetails;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface CustomerService {

	CustomerDetails createCustomer(CustomerDetails customer);

	Optional<CustomerDetails> getCustomerById(Long id);

	CompletableFuture<List<CustomerDetails>> getAllCustomers();

	CustomerDetails updateCustomer(Long id, CustomerDetails customer);

	List<String> customerExperienceInCountryForFiveYears(String country);

	List<CustomerDetails> findCustomersWithExperienceLessThanFive(String Designation);

	boolean deleteCustomerbyid(Long id);

}
