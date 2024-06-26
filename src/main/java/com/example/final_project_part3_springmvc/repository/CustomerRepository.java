package com.example.final_project_part3_springmvc.repository;




import com.example.final_project_part3_springmvc.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByUsername(String username);

    @Modifying()
    @Query("UPDATE Customer c SET c.password = :password WHERE c.username = :username")
    void updatePassword(String password,String username);
}
