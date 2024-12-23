package com.customer.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.List;

import com.customer.convertor.JpaConverterJson;

@Entity
@Table(name = "customerdetails") // Maps this entity to the "customerdetails" table
@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields
public class CustomerDetails implements Serializable {

    private static final long serialVersionUID = 1L; // Ensures compatibility during serialization

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID values
    private Long id;

    @NotBlank(message = "Name is mandatory") // Validation: Name should not be blank
    @Size(max = 100, message = "Name must not exceed 100 characters") // Validation: Max length
    private String name;

    @Email(message = "Email should be valid") // Validation: Email format
    @NotBlank(message = "Email is mandatory") // Validation: Email should not be blank
    private String email;

    @NotBlank(message = "Designation is mandatory") // Validation: Designation should not be blank
    private String designation;
    
    @Min(value = 0, message = "Experience in years must be at least 0") // Validation: Minimum value
    @Max(value = 50, message = "Experience in years must not exceed 50") // Validation: Maximum value
    private Integer expyr; // Experience in years
    
    @Convert(converter = JpaConverterJson.class) // Custom converter for JSON
    private List<String> countries;
   
}
