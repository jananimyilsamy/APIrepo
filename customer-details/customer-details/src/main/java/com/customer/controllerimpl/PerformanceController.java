package com.customer.controllerimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.customer.serviceimpl.CustomerPerformanceService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/performance")
public class PerformanceController {

	@Autowired
    private  CustomerPerformanceService performanceService;

    

    @GetMapping("/compare")
    public String comparePerformance() {
        List<Long> customerIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);

        System.out.println("Running Sequential Execution:");
     String  fetchCustomerDetailsSequential=  performanceService.fetchCustomerDetailsSequential(customerIds);

        System.out.println("\nRunning Parallel Execution:");
      String  fetchCustomerDetailsParallel= performanceService.fetchCustomerDetailsParallel(customerIds);

        return fetchCustomerDetailsSequential+" "+fetchCustomerDetailsParallel;
    }
}
