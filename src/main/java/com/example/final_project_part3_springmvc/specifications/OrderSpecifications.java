package com.example.final_project_part3_springmvc.specifications;

import com.example.final_project_part3_springmvc.dto.order.OrderCriteriaDto;
import com.example.final_project_part3_springmvc.model.Order;
import com.example.final_project_part3_springmvc.model.OrderStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecifications {

    public static Specification<Order> getOrderSpecification(OrderCriteriaDto orderCriteriaDto) {
        return (root, query, criteriaBuilder) ->{
            List<Predicate> predicates =new ArrayList<>();

            if (orderCriteriaDto.getCustomerUserName()!=null&& !orderCriteriaDto.getCustomerUserName().isEmpty())
                predicates.add( criteriaBuilder.like(root.get("customer").get("username"), orderCriteriaDto.getCustomerUserName()));
            if (orderCriteriaDto.getExpertUserName()!=null&& !orderCriteriaDto.getExpertUserName().isEmpty())
                predicates.add( criteriaBuilder.like(root.get("expert").get("username"), orderCriteriaDto.getExpertUserName()));
            if (orderCriteriaDto.getStatus()!=null&& !orderCriteriaDto.getStatus().isEmpty())
                predicates.add( criteriaBuilder.equal(root.get("orderStatus"), OrderStatus.valueOf(orderCriteriaDto.getStatus())));
            if (orderCriteriaDto.getSubServiceName()!=null&& !orderCriteriaDto.getSubServiceName().isEmpty())
                predicates.add( criteriaBuilder.like(root.get("subServices").get("name"), orderCriteriaDto.getSubServiceName()));
            if (orderCriteriaDto.getServiceName()!=null&& !orderCriteriaDto.getServiceName().isEmpty())
                predicates.add( criteriaBuilder.like(root.get("subServices").get("services").get("serviceName"), orderCriteriaDto.getServiceName()));
            if (orderCriteriaDto.getStartDate()!=null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("orderRegisterDate"),orderCriteriaDto.getStartDate()));
            if (orderCriteriaDto.getEndDate()!=null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("orderRegisterDate"),orderCriteriaDto.getEndDate()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
