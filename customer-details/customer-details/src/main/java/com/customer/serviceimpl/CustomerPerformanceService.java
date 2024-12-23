package com.customer.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.entity.CustomerDetails;
import com.customer.repository.CustomerDetailsRepo;

@Service
public class CustomerPerformanceService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerPerformanceService.class);

	@Autowired
	private CustomerDetailsRepo customerdetailsrepo;

    // Sequential Execution
    public String fetchCustomerDetailsSequential(List<Long> ids) {
    	
        long startTime = System.currentTimeMillis();
        logger.debug("Sequential Execution");
        List<CustomerDetails> results = new ArrayList<>();
        
        for (Long id : ids) {
            results.add(fetchCustomerById(id)); // Simulate a database fetch
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Sequential Execution Time: " + (endTime - startTime) + " ms");
        
        return "Sequential Execution Time: " + (endTime - startTime) + " ms";
    }

    // Parallel Execution
    public String fetchCustomerDetailsParallel(List<Long> ids) {
    	  logger.debug("Parallel Execution");
        long startTime = System.currentTimeMillis();

        List<CompletableFuture<CustomerDetails>> futures = ids.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> fetchCustomerById(id)))
                .collect(Collectors.toList());

        List<CustomerDetails> results = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        System.out.println("Parallel Execution Time: " + (endTime - startTime) + " ms");
        return "Parallel Execution Time: " + (endTime - startTime) + " ms";
    }

    // Simulate fetching a customer by ID
    private CustomerDetails fetchCustomerById(Long id) {
        try {
            Thread.sleep(500); // Simulate latency
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return customerdetailsrepo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}
