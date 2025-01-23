package mr.demonid.storage.service.repository;

import mr.demonid.storage.service.domain.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {

    /**
     * Поиск и возврат объекта вместе с его зависимостями.
     */
    @Query("SELECT u FROM WorkSchedule u " +
            "LEFT JOIN FETCH u.objects " +
            "LEFT JOIN FETCH u.persons " +
            "WHERE u.id = :id")
    Optional<WorkSchedule> findByIdWithDependencies(@Param("id") Long id);

    /**
     * Поиск и возврат всех объектов вместе с их зависимостями.
     */
    @Query("SELECT u FROM WorkSchedule u " +
            "LEFT JOIN FETCH u.objects " +
            "LEFT JOIN FETCH u.persons")
    List<WorkSchedule> findAllWithDependencies();

}
