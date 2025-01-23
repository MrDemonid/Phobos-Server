package mr.demonid.storage.service.repository;

import mr.demonid.storage.service.domain.Person;
import mr.demonid.storage.service.domain.Phone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    /**
     * Поиск сотрудника по его табельному номеру.
     * @param tabNo Табельный номер.
     */
    Person findByTabNo(Long tabNo);

    /**
     * Делает постраничную выборку сотрудников, по заданному шаблону фамилии.
     * @param pageable      Номер и размер страницы.
     * @param namePattern   Шаблон строки поиска.
     */
    @Query("SELECT u FROM Person u " +
            "LEFT JOIN FETCH u.photos " +
            "LEFT JOIN FETCH u.phones " +
            "LEFT JOIN FETCH u.workSchedules " +
            "LEFT JOIN FETCH u.addresses " +
            "WHERE u.lastName LIKE :namePattern")
    List<Person> findAllWithLimit10(Pageable pageable, @Param("namePattern") String namePattern);

    /**
     * Выборка полных данных о сотруднике.
     * @param tabNo Табельный номер сотрудника.
     */
    @Query("SELECT p FROM Person p " +
            "LEFT JOIN FETCH p.photos " +
            "LEFT JOIN FETCH p.phones " +
            "LEFT JOIN FETCH p.workSchedules " +
            "LEFT JOIN FETCH p.addresses " +
            "WHERE p.tabNo = :tabNo")
    Optional<Person> findPersonDetails(Long tabNo);

    /**
     * Возвращает все ID связанных с сотрудником объектов
     * @param tabNo Табельный номер сотрудника.
     *
     */
    @Query("SELECT o.id FROM ObjectEntity o JOIN o.persons p WHERE p.tabNo = :tabNo")
    Set<Long> findObjectIdsByPersonTabNo(Long tabNo);

    /*
     Самый быстрый, но не переносимый вариант. Запрос связанных ID напрямую из связной таблицы, через SQL:
     @Query(value = "SELECT object_id FROM object_person WHERE person_id = :tabNo", nativeQuery = true)
     */

}
