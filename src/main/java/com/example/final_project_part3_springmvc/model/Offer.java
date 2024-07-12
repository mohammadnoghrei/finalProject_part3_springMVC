package com.example.final_project_part3_springmvc.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


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
//    @FutureOrPresent

    LocalDateTime sendOfferDate;

    LocalDateTime startOfferDate;

    LocalDateTime endOfferDate;
    boolean confirmed;




}
