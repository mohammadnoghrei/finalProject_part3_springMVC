package com.example.final_project_part3_springmvc.service;

import com.example.final_project_part3_springmvc.dto.order.OrderCriteriaDto;
import com.example.final_project_part3_springmvc.exception.InvalidEntityException;
import com.example.final_project_part3_springmvc.exception.NotFoundException;
import com.example.final_project_part3_springmvc.exception.StatusException;
import com.example.final_project_part3_springmvc.model.*;
import com.example.final_project_part3_springmvc.repository.OrderRepository;
import com.example.final_project_part3_springmvc.specifications.OrderSpecifications;
import com.example.final_project_part3_springmvc.utility.Util;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final SubServicesService subServicesService;
    private final CustomerService customerService;
    private final ExpertService expertService;
    private final WalletService walletService;
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
    public Order saveOrder(Order order ){
        order.setOrderRegisterDate(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERT_OFFER);
     if (order.getSubServices().getBasePrice()>order.getCustomerOfferPrice())
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
        order.setEndOrderDate(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))));
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
        offer.getOrder().setEndOrderDateOffer(offer.getEndOfferDate());
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
    public List<Order> findAllOrderByExpertAndOrderStatus(long expertId, OrderStatus orderStatus){
        return orderRepository.findAllByExpertAndOrderStatus(expertService.findById(expertId),orderStatus);
    }

    public List<Order> findAllOrderByCustomerAndOrderStatus(long customerId, OrderStatus orderStatus){
        return orderRepository.findAllByCustomerAndOrderStatus(customerService.findById(customerId),orderStatus);
    }

    @Transactional
   public void orderPaymentWithCardBalance(long orderId){
        Order order=findById(orderId);
        if (!order.getOrderStatus().equals(OrderStatus.DONE_SERVICE))
            throw new StatusException(String.format("the status of entity with %s must be Done_SERVICE before you change it", orderId));
        if (order.getFinalPrice()>order.getCustomer().getCardBalance())
            throw new InvalidEntityException(String.format("the customer of order with %s dont have enough balance", orderId));
        Customer customer =order.getCustomer();
        customer.setCardBalance(customer.getCardBalance()-order.getFinalPrice());
        customerService.updateCustomer(customer);
        Expert expert =order.getExpert();
        expert.setCardBalance(expert.getCardBalance()+(order.getFinalPrice()-(order.getFinalPrice()*0.3)));
        expertService.updateExpert(expert);
        orderRepository.updateOrderStatus(OrderStatus.PAID_SERVICE_PRICE,orderId);
    }
    @Transactional
    public void orderPaymentWithWallet(long orderId,Wallet wallet1){
        Order order=findById(orderId);
        Wallet wallet=walletService.walletCheck(wallet1);
        if (!order.getOrderStatus().equals(OrderStatus.DONE_SERVICE))
            throw new StatusException(String.format("the status of entity with %s must be Done_SERVICE before you change it", orderId));
        if (order.getFinalPrice()>wallet.getBalance())
            throw new InvalidEntityException("the customer wallet dont have enough balance");
        walletService.updateBalance(wallet.getId(),wallet.getBalance()-order.getFinalPrice());
        Expert expert =order.getExpert();
        expert.setCardBalance(expert.getCardBalance()+(order.getFinalPrice()-(order.getFinalPrice()*0.3)));
        expertService.updateExpert(expert);
        orderRepository.updateOrderStatus(OrderStatus.PAID_SERVICE_PRICE,orderId);
    }

   public void checkDelayTimeOfExpert(long orderId){
        Order order =findById(orderId);
        int difference=order.getRate() - Util.getHourDifferenceBetweenDates(order.getEndOrderDate(),order.getEndOrderDateOffer());
       if (difference<0){
           Expert expert=expertService.findById(order.getExpert().getId());
           expert.setExpertStatus(ExpertStatus.WAITING_FOR_CONFIRMATION);
           expertService.updateExpert(expert);}
       else if (difference>=0) {
           order.setRate(difference);
           orderRepository.save(order);
       }
   }
    public List<Order> orderSearch(OrderCriteriaDto orderCriteriaDto) {
        Specification<Order> specification = OrderSpecifications.getOrderSpecification(orderCriteriaDto);
        return orderRepository.findAll(specification);
    }
}
