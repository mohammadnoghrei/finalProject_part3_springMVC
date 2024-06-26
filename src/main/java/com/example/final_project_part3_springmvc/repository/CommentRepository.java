package com.example.final_project_part3_springmvc.repository;


import com.example.final_project_part3_springmvc.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
