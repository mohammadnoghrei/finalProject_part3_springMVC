package com.example.final_project_part3_springmvc.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@Getter
@Setter
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor

public class Offer  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull
    private Order order;
    @ManyToOne (fetch = FetchType.EAGER)
    private Expert expert;
    @Min(0)
    double price;
    @FutureOrPresent
//    LocalDate sendOfferDate;
//    @FutureOrPresent
//    LocalDate startOfferDate;
//    @FutureOrPresent
//    LocalDate endOfferDate;
    boolean confirmed;




}
