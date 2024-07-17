package com.example.final_project_part3_springmvc.repository;

import com.example.final_project_part3_springmvc.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository  extends JpaRepository<Order,Long>, JpaSpecificationExecutor {

    List<Order> findAllByOrderStatusOrOrderStatusAndSubServices(OrderStatus orderStatus1, OrderStatus orderStatus2, SubServices subServices);
    List<Order> findAllByExpertAndOrderStatus(Expert expert, OrderStatus orderStatus);
    @Modifying(clearAutomatically = true,flushAutomatically = true)
    @Query("UPDATE Order c SET c.orderStatus =  :status WHERE c.id = :id")
    void updateOrderStatus(OrderStatus status, long id);

    List<Order> findAllByCustomerAndOrderStatus(Customer customer,OrderStatus orderStatus);
}
