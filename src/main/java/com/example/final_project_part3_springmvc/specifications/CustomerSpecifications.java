package com.example.final_project_part3_springmvc.specifications;

import com.example.final_project_part3_springmvc.dto.customer.CustomerCriteriaDto;
import com.example.final_project_part3_springmvc.model.Customer;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class CustomerSpecifications {
    public static Specification<Customer> getCustomerSpecification( CustomerCriteriaDto customerCriteriaDto) {
        return (root, query, criteriaBuilder) ->{ List<Predicate> predicates =new ArrayList<>();

            if (customerCriteriaDto.firstname!=null&& !customerCriteriaDto.firstname.isEmpty())
                predicates.add( criteriaBuilder.like(root.get("firstname"),customerCriteriaDto.firstname));
            if (customerCriteriaDto.lastname!=null&& !customerCriteriaDto.lastname.isEmpty())
                predicates.add( criteriaBuilder.like(root.get("lastname"),customerCriteriaDto.lastname));
            if (customerCriteriaDto.email!=null&& !customerCriteriaDto.email.isEmpty())
                predicates.add( criteriaBuilder.like(root.get("email"),customerCriteriaDto.email));
            if (customerCriteriaDto.orderCount>0)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.size(root.get("orderList")), customerCriteriaDto.orderCount));
            if (customerCriteriaDto.minRegisterDate!=null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("registerDate"),customerCriteriaDto.minRegisterDate));
            if (customerCriteriaDto.maxRegisterDate!=null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("registerDate"),customerCriteriaDto.maxRegisterDate));


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


}
