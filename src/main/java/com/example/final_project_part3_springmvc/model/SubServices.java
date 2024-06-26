package com.example.final_project_part3_springmvc.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor

public class SubServices  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true)
    private String name;
    @ManyToOne
    private Services services;
    @Positive
    private double basePrice;
    private String description;
    @OneToMany( mappedBy = "subServices")
    private List<SubServiceExpert>subServiceExperts=new ArrayList<>();
    @OneToMany(mappedBy ="subServices" ,cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Order>orderList=new ArrayList<>();

}
