package com.ecommerce.user.controller;

import com.ecommerce.user.dto.CustomerRequestDTO;
import com.ecommerce.user.dto.CustomerResponseDTO;
import com.ecommerce.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class CustomerController {

    @Autowired
    private final CustomerService customerService;
//    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllUsers(){
        return new ResponseEntity<>(customerService.fetchAllCustomers(),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getUser(@PathVariable String id){
        log.info("Request received for user: {}", id);

        log.trace("This is TRACE level - Very detailed logs");
        log.debug("This is DEBUG level - Used for development debugging");
        log.info("This is INFO level - General system information");
        log.warn("This is WARN level - Something might be wrong");
        log.error("This is ERROR level - Something failed");

        return customerService.fetchCustomer(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody CustomerRequestDTO customerRequestDTO){
        customerService.addCustomer(customerRequestDTO);
        return ResponseEntity.ok("User added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id,
                                             @RequestBody CustomerRequestDTO updateCustomerRequestDTO){
        boolean updated = customerService.updateCustomer(id, updateCustomerRequestDTO);
        if (updated)
            return ResponseEntity.ok("User updated successfully");
        return ResponseEntity.notFound().build();
    }
}