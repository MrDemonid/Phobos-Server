package mr.demonid.storage.service.repository;

import mr.demonid.storage.service.domain.ObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObjectEntityRepository extends JpaRepository<ObjectEntity, Long>, JpaSpecificationExecutor<ObjectEntity> {

    /**
     * Загрузка объекта с зависимостями.
     */
    @Query("""
        SELECT o FROM ObjectEntity o
        LEFT JOIN FETCH o.photos
        LEFT JOIN FETCH o.persons
        LEFT JOIN FETCH o.phones
        LEFT JOIN FETCH o.workSchedules
        WHERE o.id = :id
    """)
    Optional<ObjectEntity> findObjectDetails(@Param("id") Long id);

}
