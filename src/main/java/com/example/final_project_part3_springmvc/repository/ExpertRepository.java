package com.example.final_project_part3_springmvc.repository;


import com.example.final_project_part3_springmvc.model.Expert;
import com.example.final_project_part3_springmvc.model.ExpertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpertRepository extends JpaRepository<Expert,Long> {
    Optional<Expert> findByUsername(String userName);
    List<Expert> findAllByExpertStatus(ExpertStatus expertStatus);

    @Modifying
    @Query("UPDATE Expert c SET c.password = :password WHERE c.username = :username")
    void updatePassword(String password,String username);

    @Modifying
    @Query("UPDATE Expert c SET c.avgScore = :score WHERE c.username = :username")
    void updateScore(double score,String username);

    @Modifying
    @Query("UPDATE Expert c SET c.expertStatus =  :expertStatus WHERE c.username = :username")
    void confirmExpert(ExpertStatus expertStatus,String username);
}
