package com.example.final_project_part3_springmvc.controller;

import com.example.final_project_part3_springmvc.dto.customer.CustomerSaveRequest;
import com.example.final_project_part3_springmvc.dto.customer.CustomerSaveResponse;
import com.example.final_project_part3_springmvc.dto.expert.ExpertSaveResponse;
import com.example.final_project_part3_springmvc.mapper.CustomerMapper;
import com.example.final_project_part3_springmvc.model.Customer;
import com.example.final_project_part3_springmvc.model.Expert;
import com.example.final_project_part3_springmvc.service.CustomerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/register-customer")
    public ResponseEntity<CustomerSaveResponse> registerCustomer( @RequestBody CustomerSaveRequest request) {
        Customer mappedCustomer = CustomerMapper.INSTANCE.customerSaveRequestToModel(request);
        Customer savedCustomer = customerService.registerCustomer(mappedCustomer);
        return new ResponseEntity<>(CustomerMapper.INSTANCE.modelToCustomerSaveResponse(savedCustomer), HttpStatus.CREATED);
    }

   @GetMapping("/get-by-id-customer/{id}")
    public CustomerSaveResponse getCustomerById(@PathVariable Long id) {
       return CustomerMapper.INSTANCE.modelToCustomerSaveResponse(customerService.findById(id));
    }

    @GetMapping("/get-by-username-customer/{username}")
    public CustomerSaveResponse getCustomerByUsername(@PathVariable String username) {
        return CustomerMapper.INSTANCE.modelToCustomerSaveResponse(customerService.findByUsername(username));}

    @DeleteMapping("/delete-by-customer-username/{username}")
    public String deleteUser(@PathVariable String username){
        customerService.deleteByUsername(username);
        return "deleted customer with username:"+username;
    }
    @RequestMapping("/change-customer-password/{username}/{oldPassword}/{newPassword}/{finalNewPassword}")
    public String changeCustomerPassword(@PathVariable String username, @PathVariable String oldPassword,@PathVariable String newPassword, @PathVariable String finalNewPassword) {
        customerService.updatePassword(username,oldPassword,newPassword,finalNewPassword);
        return "change customer password with username: "+username;
    }
    @GetMapping("search-customer")
    public List<CustomerSaveResponse> searchCustomer(@RequestParam(required = false) String firstname,
                                                 @RequestParam(required = false) String lastname,
                                                 @RequestParam(required = false) String email){
        List<Customer> customers =customerService.customerSearch(firstname,lastname,email);
        return customers.stream()
                .map(CustomerMapper.INSTANCE::modelToCustomerSaveResponse).collect(Collectors.toList());
    }
}
