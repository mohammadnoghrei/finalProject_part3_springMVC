package com.example.final_project_part3_springmvc.repository;


import com.example.final_project_part3_springmvc.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Repository
public interface PersonRepository  extends JpaRepository<Person,Long> {
    Optional<Person> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE Person p " +
            "SET p.enabled = TRUE WHERE p.username = ?1")
    int enablePerson(String username);
}
