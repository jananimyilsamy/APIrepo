package com.customer.Customerdetailstest;



import com.customer.entity.CustomerDetails;
import com.customer.repository.CustomerDetailsRepo;
import com.customer.serviceimpl.CustomerServiceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    @Autowired
    private CustomerDetailsRepo customerDetailsRepo;

    @Test
    void testCreateCustomer() {
        CustomerDetails customer = new CustomerDetails();
        customer.setName("Jane Doe");
        customer.setEmail("jane.doe@example.com");
        customer.setDesignation("Engineer");
        customer.setExpyr(2);
        customer.setCountries(Arrays.asList("USA","Japan"));

        CustomerDetails createdCustomer = customerServiceImpl.createCustomer(customer);
        assertNotNull(createdCustomer);
        assertEquals("Jane Doe", createdCustomer.getName());
    }

    @Test
    void testGetCustomerById() {
        CustomerDetails customer = new CustomerDetails();
        customer.setName("Mike Ross");
        customer.setEmail("mike.ross@example.com");
        customer.setDesignation("Analyst");
        customer.setExpyr(4);
        customer.setCountries(Arrays.asList("Canada","Japan"));

        CustomerDetails savedCustomer = customerDetailsRepo.save(customer);
        Optional<CustomerDetails> retrievedCustomer = customerServiceImpl.getCustomerById(savedCustomer.getId());
        assertTrue(retrievedCustomer.isPresent());
        assertEquals("Mike Ross", retrievedCustomer.get().getName());
    }

    @Test
    void testGetAllCustomers() {
    	
        CustomerDetails customer1 = new CustomerDetails();
        customer1.setName("Rachel Green");
        customer1.setEmail("rachel.green@example.com");
        customer1.setDesignation("HR");
        customer1.setExpyr(5);
        customer1.setCountries(Arrays.asList("Canada","Japan"));

        CustomerDetails customer2 = new CustomerDetails();
        customer2.setName("Chandler Bing");
        customer2.setEmail("chandler.bing@example.com");
        customer2.setDesignation("Manager");
        customer2.setExpyr(6);
        customer2.setCountries(Arrays.asList("Canada","Japan"));

        Long save1=customerDetailsRepo.save(customer1).getId();
        Long save2=customerDetailsRepo.save(customer2).getId();

        List<CustomerDetails> customers = null;
		try {
			customers = customerServiceImpl.getAllCustomers().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        assertFalse(customers.isEmpty());
        assertNotNull(save1);
        assertNotNull(save2);
    }

    @Test
    void testUpdateCustomer() {
        CustomerDetails customer = new CustomerDetails();
        customer.setName("Ross Geller");
        customer.setEmail("ross.geller@example.com");
        customer.setDesignation("Scientist");
        customer.setExpyr(7);
        customer.setCountries(Arrays.asList("Canada","Japan"));

        CustomerDetails savedCustomer = customerDetailsRepo.save(customer);
        savedCustomer.setName("Ross Geller Updated");
        CustomerDetails updatedCustomer = customerServiceImpl.updateCustomer(savedCustomer.getId(), savedCustomer);
        assertNotNull(updatedCustomer);
        assertEquals("Ross Geller Updated", updatedCustomer.getName());
    }

    @Test
    void testDeleteCustomerById() {
        CustomerDetails customer = new CustomerDetails();
        customer.setName("Joey Tribbiani");
        customer.setEmail("joey.tribbiani@example.com");
        customer.setDesignation("Actor");
        customer.setExpyr(3);
        customer.setCountries(Arrays.asList("Canada","Japan"));

        CustomerDetails savedCustomer = customerDetailsRepo.save(customer);
        boolean isDeleted = customerServiceImpl.deleteCustomerbyid(savedCustomer.getId());
        assertTrue(isDeleted);
    }
}
