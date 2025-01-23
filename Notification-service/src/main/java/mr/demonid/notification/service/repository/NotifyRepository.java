package mr.demonid.notification.service.repository;

import mr.demonid.notification.service.domain.NotifyTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Интерфейс работы с базой данных уведомлений.
 */
@Repository
public interface NotifyRepository extends JpaRepository<NotifyTarget, Long> {

    @Query("SELECT u FROM NotifyTarget u WHERE u.key = :key")
    List<NotifyTarget> findAllByKey(@Param("key") Integer key);

}
