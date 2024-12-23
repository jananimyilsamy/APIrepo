package com.customer.Customerdetailstest;


import com.customer.entity.CustomerDetails;
import com.customer.repository.CustomerDetailsRepo;
import com.customer.serviceimpl.CustomerServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    @Mock
    private CustomerDetailsRepo customerDetailsRepo;

    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;

    private CustomerDetails customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new CustomerDetails();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setDesignation("Manager");
        customer.setExpyr(3);
        customer.setCountries(Arrays.asList("USA","Japan"));
    }

    @Test
    void testCreateCustomer() {
        when(customerDetailsRepo.save(customer)).thenReturn(customer);
        CustomerDetails createdCustomer = customerServiceImpl.createCustomer(customer);
        assertNotNull(createdCustomer);
        assertEquals("John Doe", createdCustomer.getName());
        verify(customerDetailsRepo, times(1)).save(customer);
    }

    @Test
    void testGetCustomerById() {
        when(customerDetailsRepo.findById(1L)).thenReturn(Optional.of(customer));
        Optional<CustomerDetails> retrievedCustomer = customerServiceImpl.getCustomerById(1L);
        assertTrue(retrievedCustomer.isPresent());
        assertEquals("John Doe", retrievedCustomer.get().getName());
        verify(customerDetailsRepo, times(1)).findById(1L);
    }

    @Test
    void testGetAllCustomers() {
        when(customerDetailsRepo.findAll()).thenReturn(Arrays.asList(customer));
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
        assertEquals(1, customers.size());
        verify(customerDetailsRepo, times(1)).findAll();
    }

    @Test
    void testUpdateCustomer() {
        when(customerDetailsRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(customerDetailsRepo.save(customer)).thenReturn(customer);
        CustomerDetails updatedCustomer = customerServiceImpl.updateCustomer(1L, customer);
        assertNotNull(updatedCustomer);
        assertEquals("John Doe", updatedCustomer.getName());
        verify(customerDetailsRepo, times(1)).save(customer);
    }

    @Test
    void testDeleteCustomerById() {
        Long customerId = 1L;
        when(customerDetailsRepo.existsById(customerId)).thenReturn(true);
        boolean isDeleted = customerServiceImpl.deleteCustomerbyid(customerId);
        assertTrue(isDeleted, "Customer should be deleted successfully");
        verify(customerDetailsRepo, times(1)).existsById(customerId);
        verify(customerDetailsRepo, times(1)).deleteById(customerId);
      
      
    }
}
