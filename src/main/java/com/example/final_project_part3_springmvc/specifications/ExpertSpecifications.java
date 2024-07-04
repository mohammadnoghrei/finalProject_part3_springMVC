package com.example.final_project_part3_springmvc.specifications;

import com.example.final_project_part3_springmvc.model.Expert;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ExpertSpecifications {

    public static Specification<Expert> getExpertSpecification(String firstname, String lastName, String email,int rate) {
        return (root, query, criteriaBuilder) ->{
            List<Predicate> predicates =new ArrayList<>();

            if (firstname!=null&& !firstname.isEmpty())
                predicates.add( criteriaBuilder.like(root.get("firstname"),firstname));
            if (lastName!=null&& !lastName.isEmpty())
                predicates.add( criteriaBuilder.like(root.get("lastname"),lastName));
            if (email!=null&& !email.isEmpty())
                predicates.add( criteriaBuilder.like(root.get("email"),email));
            if (rate>=0&&rate<=5)
                predicates.add( criteriaBuilder.greaterThanOrEqualTo(root.get("avgScore"),rate));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
