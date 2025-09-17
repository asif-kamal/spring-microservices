package com.ecommerce.user.dto;

import com.ecommerce.user.model.CustomerRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class CustomerResponseDTO {
    private String id;
    private String keyCloakId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private CustomerRole role;
    private AddressDTO address;
}