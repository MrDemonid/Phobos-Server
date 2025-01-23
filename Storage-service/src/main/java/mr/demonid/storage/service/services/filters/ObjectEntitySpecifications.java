package mr.demonid.storage.service.services.filters;

import jakarta.persistence.criteria.Predicate;
import mr.demonid.storage.service.domain.ObjectEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ObjectEntitySpecifications {

    public static Specification<ObjectEntity> filterByCriteria(ObjectFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getFromId() != null && filter.getFromId() >= 0L) {
                Long to = filter.getToId() == null ? Long.MAX_VALUE : filter.getToId();
                predicates.add(criteriaBuilder.between(root.get("id"), filter.getFromId(), to));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
