package com.ecommerce.user.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class CustomerRequestDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private AddressDTO address;

}
