package com.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.customer.entity.CustomerDetails;

@Repository

public interface CustomerDetailsRepo extends JpaRepository<CustomerDetails, Long> {

	@Query("SELECT c FROM CustomerDetails c WHERE  c.designation = designation AND c.expyr <=5")
	List<CustomerDetails> findCustomersWithExpLessThanFive(String designation);

	@Query("SELECT c FROM CustomerDetails c WHERE c.expyr > :i")
	List<CustomerDetails> findByExperienceyr(int i);

	@Query("SELECT COALESCE(MAX(c.id), 0) FROM CustomerDetails c")
	Long findMaxId();

}
