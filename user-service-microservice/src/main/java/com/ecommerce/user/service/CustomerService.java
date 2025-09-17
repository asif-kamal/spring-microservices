package com.ecommerce.user.service;

import com.ecommerce.user.dto.AddressDTO;
import com.ecommerce.user.dto.CustomerRequestDTO;
import com.ecommerce.user.dto.CustomerResponseDTO;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.Customer;
import com.ecommerce.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

//    private List<customer> customerList = new ArrayList<>();
//    private Long nextId = 1L;

    public List<CustomerResponseDTO> fetchAllCustomers(){
        return customerRepository.findAll().stream()
                .map(this::mapTocustomerResponseDTO)
                .collect(Collectors.toList());
    }

    public void addCustomer(CustomerRequestDTO customerRequest){
//        customer.setId(nextId++);


        Customer customer = new Customer();
        updateCustomerFromRequestDTO(customer, customerRequest);


        customerRepository.save(customer);
    }

    public Optional<CustomerResponseDTO> fetchCustomer(String id) {
        return customerRepository.findById(String.valueOf(id))
                .map(this::mapTocustomerResponseDTO);
    }

    public boolean updateCustomer(String id, CustomerRequestDTO updatedcustomerRequest) {
        return customerRepository.findById(String.valueOf(id))
                .map(existingcustomer -> {
                    updateCustomerFromRequestDTO(existingcustomer, updatedcustomerRequest);
                    customerRepository.save(existingcustomer);
                    return true;
                }).orElse(false);
    }

    private void updateCustomerFromRequestDTO(Customer customer, CustomerRequestDTO customerRequest) {
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhone(customerRequest.getPhone());

        if (customerRequest.getAddress() != null) {
            Address existingAddress = customer.getAddress();

            if (existingAddress != null) {
                // Update existing address fields instead of creating new one
                existingAddress.setStreet(customerRequest.getAddress().getStreet());
                existingAddress.setState(customerRequest.getAddress().getState());
                existingAddress.setZipcode(customerRequest.getAddress().getZipcode());
                existingAddress.setCity(customerRequest.getAddress().getCity());
                existingAddress.setCountry(customerRequest.getAddress().getCountry());
            } else {
                // Only create new address if customer doesn't have one
                Address address = new Address();
                address.setStreet(customerRequest.getAddress().getStreet());
                address.setState(customerRequest.getAddress().getState());
                address.setZipcode(customerRequest.getAddress().getZipcode());
                address.setCity(customerRequest.getAddress().getCity());
                address.setCountry(customerRequest.getAddress().getCountry());
                customer.setAddress(address);
            }
        }
    }


    private CustomerResponseDTO mapTocustomerResponseDTO(Customer customer){
        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(String.valueOf(customer.getId()));
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setRole(customer.getRole());

        if (customer.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(customer.getAddress().getStreet());
            addressDTO.setCity(customer.getAddress().getCity());
            addressDTO.setState(customer.getAddress().getState());
            addressDTO.setCountry(customer.getAddress().getCountry());
            addressDTO.setZipcode(customer.getAddress().getZipcode());
            response.setAddress(addressDTO);
        }
        return response;
    }
}