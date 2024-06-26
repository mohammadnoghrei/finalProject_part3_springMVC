package com.example.final_project_part3_springmvc.controller;

import com.example.final_project_part3_springmvc.dto.offer.OfferSaveRequest;
import com.example.final_project_part3_springmvc.dto.offer.OfferSaveResponse;
import com.example.final_project_part3_springmvc.dto.order.OrderSaveRequest;
import com.example.final_project_part3_springmvc.dto.order.OrderSaveResponse;
import com.example.final_project_part3_springmvc.mapper.OfferMapper;
import com.example.final_project_part3_springmvc.mapper.OrderMapper;
import com.example.final_project_part3_springmvc.model.Offer;
import com.example.final_project_part3_springmvc.model.Order;
import com.example.final_project_part3_springmvc.service.OfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OfferController {
    private final OfferService offerService;

    @PostMapping("/save-offer")
    public ResponseEntity<OfferSaveResponse> saveOffer(@RequestBody OfferSaveRequest request) {
        Offer mappedOffer = OfferMapper.INSTANCE.offerSaveRequestToModel(request);
        Offer savedOffer = offerService.saveOffer(mappedOffer);
        return new ResponseEntity<>(OfferMapper.INSTANCE.modelToOfferSaveResponse(savedOffer), HttpStatus.CREATED);
    }

    @GetMapping("/get-by-id-offer/{id}")
    public OfferSaveResponse getOfferById(@PathVariable Long id) {
        return OfferMapper.INSTANCE.modelToOfferSaveResponse(offerService.findById(id));
    }

    @GetMapping("/find-all-offer-by-order/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<OfferSaveResponse> findAllOfferByOrder(@PathVariable long id ) {
        List<Offer> offers = offerService.findAllOfferByOrder(id);
        List<OfferSaveResponse> offerSaveResponses= new ArrayList<>();
        offers.stream().forEach(a->offerSaveResponses.add(OfferMapper.INSTANCE.modelToOfferSaveResponse(a)));
        return offerSaveResponses;
    }

    @RequestMapping("/confirm-offer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String confirmOffer(@PathVariable long id) {
        offerService.confirmOffer(id);
        return "confirm offer with id:" + id;
    }
}
