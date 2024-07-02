package com.example.final_project_part3_springmvc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "orders")
public class Order  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @NotNull
    private Customer customer;
    @ManyToOne
    private Expert expert;
    @ManyToOne(cascade = {CascadeType.REMOVE})
    @NotNull
    private SubServices subServices;
    private double customerOfferPrice;
    private double finalPrice;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    @FutureOrPresent
    private LocalDateTime orderRegisterDate;
    @FutureOrPresent
    private LocalDateTime requestedDateToDoOrder;
    @FutureOrPresent
    private LocalDateTime startOrderDate;
    @FutureOrPresent
    private LocalDateTime endOrderDate;
    @FutureOrPresent
    private LocalDateTime endOrderDateOffer;
    private boolean doOrder;
    @Min(0)@Max(5)
    private int rate;
    private String description;
    @OneToOne
    private Comment comment;
    private String address;
    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER)
    private List<Offer> offerList=new ArrayList<>();



}
