package mr.demonid.storage.service.services.filters;


import mr.demonid.storage.service.domain.Person;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonSpecification {
    public static Specification<Person> filterBy(PersonFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getTabNo() != null) {
                // Используем "LIKE" для частичного совпадения в любом месте строки
                predicates.add(criteriaBuilder.equal(root.get("tabNo"), filter.getTabNo()));
            }
            if (filter.getLastName() != null) {
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + filter.getLastName() + "%"));
            }
            if (filter.getFirstName() != null) {
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + filter.getFirstName() + "%"));
            }
            if (filter.getGender() != null) {
                predicates.add(criteriaBuilder.equal(root.get("gender"), filter.getGender()));
            }
            // настраиваем дату.
            if (filter.getFromDate() != null) {
                LocalDate to = filter.getToDate() != null ? filter.getToDate() : LocalDate.now();   // по настоящее время
                predicates.add(criteriaBuilder.between(root.get("birthDate"), filter.getFromDate(), to));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
