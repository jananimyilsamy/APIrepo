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
@Table(name = "customerdetails") 
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class CustomerDetails implements Serializable {

    private static final long serialVersionUID = 1L; 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @NotBlank(message = "Name is mandatory") 
    @Size(max = 100, message = "Name must not exceed 100 characters") 
    private String name;

    @Email(message = "Email should be valid") 
    @NotBlank(message = "Email is mandatory") 
    private String email;

    @NotBlank(message = "Designation is mandatory") 
    private String designation;
    
    @Min(value = 0, message = "Experience in years must be at least 0") 
    @Max(value = 50, message = "Experience in years must not exceed 50") 
    private Integer expyr; 
    
    @Convert(converter = JpaConverterJson.class) // Custom converter for JSON
    private List<String> countries;
   
}
