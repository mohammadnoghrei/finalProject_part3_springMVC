package com.example.final_project_part3_springmvc.specifications;

import com.example.final_project_part3_springmvc.dto.expert.ExpertCriteriaDto;
import com.example.final_project_part3_springmvc.model.Expert;
import com.example.final_project_part3_springmvc.model.SubServiceExpert;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ExpertSpecifications {

    public static Specification<Expert> getExpertSpecification(ExpertCriteriaDto expertCriteriaDto) {
        return (root, query, criteriaBuilder) ->{
            List<Predicate> predicates =new ArrayList<>();

            if (expertCriteriaDto.firstname!=null&& !expertCriteriaDto.firstname.isEmpty())
                predicates.add( criteriaBuilder.like(root.get("firstname"),expertCriteriaDto.firstname));
            if (expertCriteriaDto.lastname!=null&& !expertCriteriaDto.lastname.isEmpty())
                predicates.add( criteriaBuilder.like(root.get("lastname"),expertCriteriaDto.lastname));
            if (expertCriteriaDto.email!=null&& !expertCriteriaDto.email.isEmpty())
                predicates.add( criteriaBuilder.like(root.get("email"),expertCriteriaDto.email));
            if (expertCriteriaDto.rate>0&&expertCriteriaDto.rate<=5)
                predicates.add( criteriaBuilder.greaterThanOrEqualTo(root.get("avgScore"),expertCriteriaDto.rate));
            if (expertCriteriaDto.subServiceName!=null&& !expertCriteriaDto.subServiceName.isEmpty()) {
                Join<Expert, SubServiceExpert> expertSubServiceJoin = root.join("subServiceExperts", JoinType.INNER);
                criteriaBuilder.equal(expertSubServiceJoin.get("subServices").get("name"), expertCriteriaDto.subServiceName);
            }
            if (expertCriteriaDto.orderCount>0)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.size(root.get("orderList")), expertCriteriaDto.orderCount));
            if (expertCriteriaDto.minRegisterDate!=null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("registerDate"),expertCriteriaDto.minRegisterDate));
            if (expertCriteriaDto.maxRegisterDate!=null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("registerDate"),expertCriteriaDto.maxRegisterDate));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
