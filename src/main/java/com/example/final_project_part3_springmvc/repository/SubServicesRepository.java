package com.example.final_project_part3_springmvc.repository;


import com.example.final_project_part3_springmvc.model.Services;
import com.example.final_project_part3_springmvc.model.SubServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubServicesRepository  extends JpaRepository<SubServices,Long> {

    List<SubServices> findAllByServices(Services services);
    Optional<SubServices> findByName(String name);
    @Modifying
    @Query("UPDATE SubServices c SET c.basePrice = :basePrice WHERE c.name = :name")
    void updateBasePrice(double basePrice,String name);
    @Modifying
    @Query("UPDATE SubServices c SET c.description = :description WHERE c.name = :name")
    void updateDescription(String description, String name);
}
