package com.example.final_project_part3_springmvc.service;

import com.example.final_project_part3_springmvc.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService  {
    private final CommentRepository commentRepository;
}
