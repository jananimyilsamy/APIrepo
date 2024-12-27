package com.customer.controllerimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.customer.controller.CustomerController;
import com.customer.entity.CustomerDetails;
import com.customer.exception.CustomerNotFoundException;
import com.customer.service.CustomerService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/customers")
@ControllerAdvice
public class CustomerControllerImpl implements CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerControllerImpl.class);

	@Autowired
	private CustomerService customerservice;

	// Create a new customer
	@Override
	@PostMapping
	public ResponseEntity<CustomerDetails> createCustomer(@RequestBody CustomerDetails customer) {
		logger.info("Creating new customer with name: {}", customer.getName());
		CustomerDetails createdCustomer = customerservice.createCustomer(customer);
		return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
	}

	// Retrieve a customer by ID
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<CustomerDetails> getCustomerById(@PathVariable Long id) {
		logger.info("Retrieving customer with ID: {}", id);
		Optional<CustomerDetails> customer = customerservice.getCustomerById(id);
		return customer.map(ResponseEntity::ok).orElseGet(() -> {
			logger.error("Customer with ID {} not found", id);
			throw new CustomerNotFoundException("Customer not found");
		});
	}

	// Retrieve all customers
	@Override
	@GetMapping("/all")
	public ResponseEntity<List<CustomerDetails>> getAllCustomers() throws InterruptedException, ExecutionException {
		logger.info("Retrieving all customers");
		CompletableFuture<List<CustomerDetails>> customers = customerservice.getAllCustomers();
		return new ResponseEntity<>(customers.get(), HttpStatus.OK);
	}

	// Update a customer by ID
	@Override
	@PutMapping("/{id}")
	public ResponseEntity<CustomerDetails> updateCustomer(@PathVariable Long id,
			@RequestBody CustomerDetails customer) {
		logger.info("Updating customer with ID: {}", id);
		CustomerDetails updatedCustomer = customerservice.updateCustomer(id, customer);
		return updatedCustomer != null ? new ResponseEntity<>(updatedCustomer, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// check if the customer is a sales customer and has more than 5 years of
	// experience
	@Override
	@GetMapping("/check-customer-experience/{Designation}")
	public ResponseEntity<List<CustomerDetails>> findCustomerWithExpLessThanFive(@PathVariable String Designation) {
		logger.info(" controller method for findCustomerWithExpLessThanFive:{}", Designation);
		List<CustomerDetails> customerDesignations = customerservice
				.findCustomersWithExperienceLessThanFive(Designation);
		if (customerDesignations != null && !customerDesignations.isEmpty()) {
			return ResponseEntity.ok(customerDesignations);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
		}
	}

	// get customers with the required experience in the given country
	@Override
	@GetMapping("/check-country-experience")
	public ResponseEntity<List<String>> checkExperienceInCountry(@RequestParam("country") String country) {
		logger.info(" controller method for checkExperienceInCountry:{}", country);
		List<String> customersWithExperience = customerservice.customerExperienceInCountryForFiveYears(country);
		if (customersWithExperience != null && !customersWithExperience.isEmpty()) {
			return ResponseEntity.ok(customersWithExperience);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
		}
	}

	// Delete a customer by ID
	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
		logger.info("Attempting to delete customer with ID: {}", id);

		boolean isDeleted = customerservice.deleteCustomerbyid(id);

		if (isDeleted) {
			return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("id not found Deletion not possible", HttpStatus.NOT_FOUND);
		}
	}

}
