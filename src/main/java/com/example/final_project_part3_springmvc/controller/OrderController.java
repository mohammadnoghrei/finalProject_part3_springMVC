package com.example.final_project_part3_springmvc.controller;

import com.example.final_project_part3_springmvc.dto.expert.ExpertSaveResponse;
import com.example.final_project_part3_springmvc.dto.order.OrderCriteriaDto;
import com.example.final_project_part3_springmvc.dto.order.OrderSaveRequest;
import com.example.final_project_part3_springmvc.dto.order.OrderSaveResponse;
import com.example.final_project_part3_springmvc.mapper.ExpertMapper;
import com.example.final_project_part3_springmvc.mapper.OrderMapper;
import com.example.final_project_part3_springmvc.model.Expert;
import com.example.final_project_part3_springmvc.model.Order;
import com.example.final_project_part3_springmvc.model.OrderStatus;
import com.example.final_project_part3_springmvc.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/save-order")
    public ResponseEntity<OrderSaveResponse> saveOrder( @RequestBody OrderSaveRequest request) {
        Order mappedOrder = OrderMapper.INSTANCE.orderSaveRequestToModel(request);
        Order savedOrder = orderService.saveOrder(mappedOrder);
        return new ResponseEntity<>(OrderMapper.INSTANCE.modelToOrderSaveResponse(savedOrder), HttpStatus.CREATED);
    }

    @GetMapping("/get-by-id-order/{id}")
    public OrderSaveResponse getOrderById(@PathVariable Long id) {
        return OrderMapper.INSTANCE.modelToOrderSaveResponse(orderService.findById(id));
    }

    @GetMapping("/find-all-order-by-subService/{subService}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderSaveResponse> findAllOrderBySubService(@PathVariable String subService) {
        List<Order> orderList = orderService.findAllOrderByExpertSubService(subService);
        List<OrderSaveResponse> orderSaveResponses= new ArrayList<>();
        orderList.forEach(a->orderSaveResponses.add(OrderMapper.INSTANCE.modelToOrderSaveResponse(a)));
        return orderSaveResponses;
    }

    @RequestMapping("/update-orderStatus-to-WaitingForChooseExpert/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateOrderStatusToWaitingForChooseExpert(@PathVariable long id) {
        orderService.updateOrderStatusToWaitingForChooseExpert(id);
        return "update OrderStatus To Waiting For Choose Expert in order:" + id;
    }

    @RequestMapping("/update-orderStatus-toStart/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateOrderStatusToStart(@PathVariable long id) {
        orderService.updateOrderStatusToStart(id);
        return "update OrderStatus ToStart in order:" + id;
    }

    @RequestMapping("/update-orderStatus-toStart/{id}/{description}/{rate}")
    @ResponseStatus(HttpStatus.OK)
    public OrderSaveResponse updateOrderStatusToStart(@PathVariable long id, @PathVariable String description, @PathVariable int rate) {
        Order order = orderService.updateOrderStatusToDuneAndSaveRateAndDescription(id, description, rate);
        return OrderMapper.INSTANCE.modelToOrderSaveResponse(order);

    }

    @PostMapping("/payment-card-balance")
    public String paymentWithCardBalance(@RequestParam long id){
        orderService.orderPaymentWithCardBalance(id);
        return "paid order";
    }

    @GetMapping("/find-by-expert-and-orderStatus/{id}/{status}")
    public List<OrderSaveResponse> findAllByExpertAndOrderStatus(@PathVariable long id, @PathVariable String status){
        List<Order> orderList = orderService.findAllOrderByExpertAndOrderStatus(id, OrderStatus.valueOf(status));
        List<OrderSaveResponse> orderSaveResponses= new ArrayList<>();
        orderList.forEach(a->orderSaveResponses.add(OrderMapper.INSTANCE.modelToOrderSaveResponse(a)));
        return orderSaveResponses;
    }

    @GetMapping("/find-by-customer-and-orderStatus/{id}/{status}")
    public List<OrderSaveResponse> findAllByCustomerAndOrderStatus(@PathVariable long id, @PathVariable String status){
        List<Order> orderList = orderService.findAllOrderByCustomerAndOrderStatus(id, OrderStatus.valueOf(status));
        List<OrderSaveResponse> orderSaveResponses= new ArrayList<>();
        orderList.forEach(a->orderSaveResponses.add(OrderMapper.INSTANCE.modelToOrderSaveResponse(a)));
        return orderSaveResponses;
    }

    @GetMapping("search-order")
    public List<OrderSaveResponse> searchExpert(@RequestBody OrderCriteriaDto orderCriteriaDto){
        List<Order> orders =orderService.orderSearch(orderCriteriaDto);
        return orders.stream()
                .map(OrderMapper.INSTANCE::modelToOrderSaveResponse).collect(Collectors.toList());
    }
}
