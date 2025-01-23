package mr.demonid.logger.service.services.filter;

import jakarta.persistence.criteria.Predicate;
import mr.demonid.logger.service.domain.LogOperation;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Настройка условий для выборки данных из БД.
 */
public class LogSpecification {
    public static Specification<LogOperation> filterBy(LogFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getRepeater() >= 0) {
                // добавляем условие в выборку
                predicates.add(criteriaBuilder.equal(root.get("repeater"), filter.getRepeater()));
            }
            if (filter.getKey() >= 0) {
                // добавляем условие в выборку
                predicates.add(criteriaBuilder.equal(root.get("key"), filter.getKey()));
            }
            // настраиваем диапазон дат
            LocalDateTime to = filter.getTo() != null ? filter.getTo() : LocalDateTime.now();
            LocalDateTime from = filter.getFrom() != null ? filter.getFrom() : to.minusDays(1);
            predicates.add(criteriaBuilder.between(root.get("date"), from, to));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
