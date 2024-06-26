package com.example.final_project_part3_springmvc.service;

import com.example.final_project_part3_springmvc.exception.InvalidEntityException;
import com.example.final_project_part3_springmvc.exception.NotFoundException;
import com.example.final_project_part3_springmvc.exception.StatusException;
import com.example.final_project_part3_springmvc.model.Offer;
import com.example.final_project_part3_springmvc.model.Order;
import com.example.final_project_part3_springmvc.model.OrderStatus;
import com.example.final_project_part3_springmvc.repository.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final SubServicesService subServicesService;
    private final CustomerService customerService;
    private final ExpertService expertService;
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
   Validator validator = validatorFactory.getValidator();


    public boolean validate(Order entity) {

        Set<ConstraintViolation<Order>> violations = validator.validate(entity);
        if (violations.isEmpty())
            return true;
        else {
            System.out.println("Invalid user data found:");
            for (ConstraintViolation<Order> violation : violations) {
                System.out.println(violation.getMessage());
            }
            return false;
        }
    }

    public List<Order> findAllOrderByExpertSubService(String subServiceName){
       return orderRepository.findAllByOrderStatusOrOrderStatusAndSubServices(OrderStatus.WAITING_FOR_EXPERT_OFFER,OrderStatus.WAITING_FOR_CHOOSE_EXPERT,subServicesService.findBySubServiceName(subServiceName));
    }
    public Order saveOrder(String customerUsername, double offerPrice, LocalDate offerDate, String address,String subService ){
     Order order =Order.builder().customer(customerService.findByUsername(customerUsername))
             .customerOfferPrice(offerPrice).requestedDateToDoOrder(offerDate).subServices(subServicesService.findBySubServiceName(subService))
             .orderRegisterDate(LocalDate.now()).orderStatus(OrderStatus.WAITING_FOR_EXPERT_OFFER).address(address).build();
     if (order.getSubServices().getBasePrice()>offerPrice)
         throw new InvalidEntityException("the order entity have invalid price variable");
     if (!validate(order))
         throw new InvalidEntityException("the order entity have invalid variable");
     return orderRepository.save(order);
    }

    public Order findById(Long id){
        return orderRepository.findById(id).orElseThrow(()-> new NotFoundException(String.format("the entity with %s not found",id)));
    }
    @Transactional
    public void updateOrderStatusToStart(long id) {
        if (orderRepository.findById(id).isEmpty())
            throw new NotFoundException(String.format("the entity with %s not found", id));
        else if (!findById(id).getOrderStatus().equals(OrderStatus.WAITING_FOR_COMING_EXPERT_TO_YOUR_LOCATION)) {
            throw new StatusException(String.format("the status of entity with %s must be WAITING_FOR_COMING_EXPERT_TO_YOUR_LOCATION before you change it", id));
        }
        orderRepository.updateOrderStatus(OrderStatus.START_SERVICE,id);
    }

    @Transactional
    public void updateOrderStatusToWaitingForChooseExpert(long id) {
        if (orderRepository.findById(id).isEmpty())
            throw new NotFoundException(String.format("the entity with %s not found", id));
        else if (!findById(id).getOrderStatus().equals(OrderStatus.WAITING_FOR_EXPERT_OFFER)) {
            throw new StatusException(String.format("the status of entity with %s must be WAITING_FOR_EXPERT_OFFER before you change it", id));
        }
        orderRepository.updateOrderStatus(OrderStatus.WAITING_FOR_CHOOSE_EXPERT,id);
    }

    @Transactional
    public Order updateOrderStatusToDuneAndSaveRateAndDescription(long id,String description, int rate) {
        Order order= findById(id);
        if (!findById(id).getOrderStatus().equals(OrderStatus.START_SERVICE))
            throw new StatusException(String.format("the status of entity with %s must be START_SERVICE before you change it", id));
        order.setOrderStatus(OrderStatus.DONE_SERVICE);
        order.setRate(rate);
        order.setDescription(description);
        if (!validate(order)) {
            throw new InvalidEntityException("the order entity have invalid variable");
        }
        Order order1=orderRepository.save(order);
        expertService.updateScore(calculateAvgScore(order.getExpert().getUsername()),order.getExpert().getUsername());
        return order1;
    }

    public Order confirmOrder( Offer offer){
        if (!offer.getOrder().getOrderStatus().equals(OrderStatus.WAITING_FOR_CHOOSE_EXPERT))
            throw new StatusException(String.format("the status of entity with %s must be WAITING_FOR_CHOOSE_EXPERT before you change it", offer.getOrder().getId()));
        offer.getOrder().setExpert(offer.getExpert());
        offer.getOrder().setFinalPrice(offer.getPrice());
        offer.getOrder().setOrderStatus(OrderStatus.WAITING_FOR_COMING_EXPERT_TO_YOUR_LOCATION);
        return orderRepository.save(offer.getOrder());
    }

    public double calculateAvgScore(String expertUsername){
        List<Order>paiedOrderList=orderRepository.findAllByExpertAndOrderStatus(expertService.findByUsername(expertUsername),OrderStatus.PAID_SERVICE_PRICE);
        List<Order>duneOrderList=orderRepository.findAllByExpertAndOrderStatus(expertService.findByUsername(expertUsername),OrderStatus.DONE_SERVICE);
        List<Order> orderList = new ArrayList<>();
        orderList.addAll(paiedOrderList);
        orderList.addAll(duneOrderList);
        return orderList.stream()
                .mapToInt(Order::getRate)
                .average()
                .orElse(0);
    }

}
