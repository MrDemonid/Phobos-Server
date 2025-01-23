package mr.demonid.storage.service.services.filters;

import jakarta.persistence.criteria.Predicate;
import mr.demonid.storage.service.domain.Phone;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class PhoneSpecification {
    public static Specification<Phone> filterBy(PhoneFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getNumber() != null) {
                // Используем "LIKE" для частичного совпадения в любом месте строки
                System.out.println("  -- add predicate: " + filter.getNumber());
                predicates.add(criteriaBuilder.like(root.get("number"), "%" + filter.getNumber() + "%"));
            }
            if (filter.getType() != null) {
                // добавляем условие в выборку
                System.out.println("  -- add predicate: " + filter.getType());
                predicates.add(criteriaBuilder.equal(root.get("type"), filter.getType()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
