package com.example.final_project_part3_springmvc.specifications;

import com.example.final_project_part3_springmvc.model.Customer;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class CustomerSpecifications {
    public static Specification<Customer> getCustomerSpecification(String firstname, String lastName, String email) {
        return (root, query, criteriaBuilder) ->{ List<Predicate> predicates =new ArrayList<>();
            if (firstname!=null&& !firstname.isEmpty())
                predicates.add( criteriaBuilder.like(root.get("firstname"),firstname));
            if (lastName!=null&& !lastName.isEmpty())
                predicates.add( criteriaBuilder.like(root.get("lastname"),lastName));
            if (email!=null&& !email.isEmpty())
                predicates.add( criteriaBuilder.like(root.get("email"),email));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
