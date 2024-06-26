package com.example.final_project_part3_springmvc.repository;


import com.example.final_project_part3_springmvc.model.Offer;
import com.example.final_project_part3_springmvc.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer,Long> {

    List<Offer> findAllByOrder(Order order);
}
