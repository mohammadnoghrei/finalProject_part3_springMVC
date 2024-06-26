package com.example.final_project_part3_springmvc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

public class Expert extends Person {
    @Enumerated(value = EnumType.STRING)
    private ExpertStatus expertStatus;
//    @Range(min = 0,max = 5)
    private double avgScore;
    @NotNull
    private byte[] image;
    @OneToMany( mappedBy = "expert")
    private List<SubServiceExpert>subServiceExperts=new ArrayList<>();
    @OneToMany (mappedBy = "expert",cascade = {CascadeType.PERSIST,CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<Offer>offerList=new ArrayList<>();
    double cardBalance;
}
