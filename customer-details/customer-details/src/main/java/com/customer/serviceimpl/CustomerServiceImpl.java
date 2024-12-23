package com.customer.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.customer.entity.CustomerDetails;
import com.customer.repository.CustomerDetailsRepo;
import com.customer.service.CustomerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private CustomerDetailsRepo customerdetailsrepo;


	// Create a new customer
	@Override
	@CachePut(value = "createCustomer", key = "#customer")
	@Retry(name = "createCustomerRetry", fallbackMethod = "fallbackForCreateCustomer")
	@CircuitBreaker(name = "createCustomerCircuitBreaker", fallbackMethod = "fallbackForCreateCustomer")
	public CustomerDetails createCustomer(CustomerDetails customer) {
		long startTime = System.currentTimeMillis();
		logger.info("Creating new customer with name: {}", customer.getName());
		CustomerDetails result = customerdetailsrepo.save(customer);
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		logger.info("Method createCustomer executed in {} ms", duration);
		return result;
	}

	private CustomerDetails fallbackForCreateCustomer(CustomerDetails customer, Throwable t) {
		logger.error("Fallback: Unable to create customer due to: {}", t.getMessage());
		return new CustomerDetails(); // Return an empty object or a default value
	}

	// Retrieve a customer by ID
	@Override
	@Cacheable(value = "customers", key = "#id")
	@Retry(name = "getCustomerByIdRetry", fallbackMethod = "fallbackForGetCustomerById")
	@CircuitBreaker(name = "getCustomerByIdCircuitBreaker", fallbackMethod = "fallbackForGetCustomerById")
	public Optional<CustomerDetails> getCustomerById(Long id) {
		long startTime = System.currentTimeMillis();
		logger.info("Retrieving customer with ID: {}", id);
		Optional<CustomerDetails> result = customerdetailsrepo.findById(id);
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		logger.info("Method  getCustomerById executed in {} ms", duration);

		return result;
	}

	private Optional<CustomerDetails> fallbackForGetCustomerById(Long id, Throwable t) {
		logger.error("Fallback: Unable to retrieve customer with ID {} due to: {}", id, t.getMessage());
		return Optional.empty();
	}

	// Retrieve all customers
	@Override
	@Async("taskExecutor")
	@Cacheable(value = "allCustomers", key = "'allCustomersnew'")
	@Retry(name = "getAllCustomersRetry", fallbackMethod = "fallbackForGetAllCustomers")
	@CircuitBreaker(name = "getAllCustomersCircuitBreaker", fallbackMethod = "fallbackForGetAllCustomers")
	public CompletableFuture<List<CustomerDetails>> getAllCustomers() {
		long startTime = System.currentTimeMillis();
		logger.info("Retrieving all customers");
		CompletableFuture<List<CustomerDetails>> result = CompletableFuture
				.completedFuture(customerdetailsrepo.findAll());
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		logger.info("Method  getAllCustomers executed in {} ms", duration);

		return result;
	}

	private CompletableFuture<List<CustomerDetails>> fallbackForGetAllCustomers(Throwable t) {
		logger.error("Fallback: Unable to retrieve all customers due to: {}", t.getMessage());
		return CompletableFuture.completedFuture(List.of()); // Return an empty list
	}

	// Update a customer by ID
	@Override
	@Transactional
	@CacheEvict(value = "customers", key = "#id")
	@Retry(name = "updateCustomerRetry", fallbackMethod = "fallbackForUpdateCustomer")
	@CircuitBreaker(name = "updateCustomerCircuitBreaker", fallbackMethod = "fallbackForUpdateCustomer")
	public CustomerDetails updateCustomer(Long id, CustomerDetails customer) {
		long startTime = System.currentTimeMillis();
		logger.info("Updating customer with ID: {}", id);
		CustomerDetails result = customerdetailsrepo.findById(id)
				.map(existingCustomer -> customerdetailsrepo.save(customer))
				.orElseThrow(() -> new RuntimeException("Customer not found"));
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		logger.info("Method  updateCustomer executed in {} ms", duration);

		return result;
	}

	private CustomerDetails fallbackForUpdateCustomer(Long id, CustomerDetails customer, Throwable t) {
		logger.error("Fallback: Unable to update customer with ID {} due to: {}", id, t.getMessage());
		return null; // Return null or a default value
	}

	// Delete a customer by ID
	@Override
	@CacheEvict(value = "customers", key = "#id")
	@Retry(name = "deleteCustomerRetry", fallbackMethod = "fallbackForDeleteCustomer")
	@CircuitBreaker(name = "deleteCustomerCircuitBreaker", fallbackMethod = "fallbackForDeleteCustomer")
	public boolean deleteCustomerbyid(Long id) {
		long startTime = System.currentTimeMillis();
		logger.info("Deleting customer with ID: {}", id);
		if (customerdetailsrepo.existsById(id)) {
			customerdetailsrepo.deleteById(id);
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			logger.info("Method  deleteCustomerbyid executed in {} ms", duration);

			return true;
		} else {
			return false;
		}
	}

	private boolean fallbackForDeleteCustomer(Long id, Throwable t) {
		logger.error("Fallback: Unable to delete customer with ID {} due to: {}", id, t.getMessage());
		return false; // Indicate failure
	}

	// Check if the customer is a less than 5 years of experience in designation
	@Override
	@Cacheable(value = "customersWithExperienceLessThanFive", key = "#Designation")
	public List<CustomerDetails> findCustomersWithExperienceLessThanFive(String Designation) {
		long startTime = System.currentTimeMillis();
		logger.info("findCustomersWithExperienceLessThanFive in ", Designation);
		List<CustomerDetails> customer = customerdetailsrepo.findCustomersWithExpLessThanFive(Designation);
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		logger.info("Method  findCustomersWithExperienceLessThanFive executed in {} ms", duration);

		return customer;
	}

	// Check if the customer has at least 5 years of experience in country
	@Override
	@Cacheable(value = "customerExperienceInCountry", key = "#country")
	public List<String> customerExperienceInCountryForFiveYears(String country) {
		long startTime = System.currentTimeMillis();
		logger.info("customerExperienceInCountryForFiveYears", country);

		List<CustomerDetails> customerDetailsList = customerdetailsrepo.findByExperienceyr(5);

		List<String> customerNames = customerDetailsList.stream()
				.filter(countryfilter -> countryfilter.getCountries().contains(country)).map(CustomerDetails::getName)
				.collect(Collectors.toList());

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		logger.info("Method  customerExperienceInCountryForFiveYears executed in {} ms", duration);

		return customerNames;

	}

}
