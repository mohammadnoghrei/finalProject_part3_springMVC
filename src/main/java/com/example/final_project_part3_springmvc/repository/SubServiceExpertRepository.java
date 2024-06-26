package com.example.final_project_part3_springmvc.repository;



import com.example.final_project_part3_springmvc.model.Expert;
import com.example.final_project_part3_springmvc.model.SubServiceExpert;
import com.example.final_project_part3_springmvc.model.SubServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SubServiceExpertRepository extends JpaRepository<SubServiceExpert,Long> {
    Optional<SubServiceExpert> findBySubServicesAndExpert(SubServices subServices, Expert expert);



}
