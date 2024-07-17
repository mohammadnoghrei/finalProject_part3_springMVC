package com.example.final_project_part3_springmvc.repository;




import com.example.final_project_part3_springmvc.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>,JpaSpecificationExecutor{
    Optional<Customer> findByUsername(String username);

    @Modifying()
    @Query("UPDATE Customer c SET c.password = :password WHERE c.username = :username")
    void updatePassword(String password,String username);

//    @Query("SELECT u FROM Customer u JOIN FETCH u.orderList o GROUP BY u HAVING COUNT(o) > :orderCount")
    @Query("SELECT c FROM Customer c WHERE SIZE(c.orderList) > :orderCount")
    List<Customer> findCustomersWithOrderCount(@Param("orderCount") int orderCount);

}
