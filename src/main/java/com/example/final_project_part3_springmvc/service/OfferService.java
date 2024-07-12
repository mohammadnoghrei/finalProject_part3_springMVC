package com.example.final_project_part3_springmvc.service;

import com.example.final_project_part3_springmvc.exception.InvalidEntityException;
import com.example.final_project_part3_springmvc.exception.NotFoundException;
import com.example.final_project_part3_springmvc.exception.StatusException;
import com.example.final_project_part3_springmvc.model.Offer;
import com.example.final_project_part3_springmvc.model.OrderStatus;
import com.example.final_project_part3_springmvc.repository.OfferRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService  {
    private final OfferRepository offerRepository;
    private final ExpertService expertService;
    private final OrderService orderService;
   ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
   Validator validator = validatorFactory.getValidator();

    public boolean validate(Offer entity) {

        Set<ConstraintViolation<Offer>> violations = validator.validate(entity);
        if (violations.isEmpty())
            return true;
        else {log.warn("Invalid user data found:");
            for (ConstraintViolation<Offer> violation : violations) {
                log.warn(violation.getMessage());
            }
            return false;
        }
    }

    public Offer findById(Long id){
        return offerRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(String.format("the entity with %s not found",id)));
    }

    public List<Offer> findAllOfferByOrder(long orderId){
        List<Offer> allByOrder = offerRepository.findAllByOrder(orderService.findById(orderId));
        allByOrder.sort(Comparator.comparing(Offer::getPrice));
        allByOrder.sort(Comparator.comparing(a->a.getExpert().getAvgScore()));
        return allByOrder;
    }

    public Offer saveOffer(Offer newOffer){
//       newOffer.setSendOfferDate(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))));
       newOffer.setSendOfferDate(LocalDateTime.now());
       newOffer.setOrder(orderService.findById(newOffer.getOrder().getId()));
       newOffer.setExpert(expertService.findById(newOffer.getExpert().getId()));
        if (!validate(newOffer))
            throw new InvalidEntityException("the offer entity have invalid variable");
        if (newOffer.getOrder().getOrderStatus().equals(OrderStatus.START_SERVICE)||
                newOffer.getOrder().getOrderStatus().equals(OrderStatus.DONE_SERVICE)||
                newOffer.getOrder().getOrderStatus().equals(OrderStatus.PAID_SERVICE_PRICE) ||
                newOffer.getOrder().getOrderStatus().equals(OrderStatus.WAITING_FOR_COMING_EXPERT_TO_YOUR_LOCATION))
            throw new StatusException("the status of order is not valid");
        if (!newOffer.getOrder().getOrderStatus().equals(OrderStatus.WAITING_FOR_CHOOSE_EXPERT))
            orderService.updateOrderStatusToWaitingForChooseExpert(newOffer.getOrder().getId());
        return offerRepository.save(newOffer);
    }

    @Transactional
    public Offer confirmOffer(long offerId){
        Offer offer=findById(offerId);
        if (offer.isConfirmed())
            throw new InvalidEntityException(String.format("the offer with %s must be not confirm",offerId));
        offer.setConfirmed(true);
        orderService.confirmOrder(offer);
        return offerRepository.save(offer);
    }

}
