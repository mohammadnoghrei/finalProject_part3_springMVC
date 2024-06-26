package com.example.final_project_part3_springmvc.repository;


import com.example.final_project_part3_springmvc.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicesRepository extends JpaRepository<Services,Long> {

    Optional<Services> findByServiceName(String name);

}
