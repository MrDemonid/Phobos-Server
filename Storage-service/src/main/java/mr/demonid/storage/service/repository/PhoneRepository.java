package mr.demonid.storage.service.repository;

import mr.demonid.storage.service.domain.Phone;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long>, JpaSpecificationExecutor<Phone> {

//    List<Phone> findTop10ByNumberContaining(String number);

    /**
     * Выборка телефонов по шаблону номера и их типу.
     * @param number Шаблон номера (% - любые символы).
     * @param type   Тип телефона (MOBILE, WORK и тд.).
     */
    @Query("SELECT u FROM Phone u LEFT JOIN FETCH u.object LEFT JOIN FETCH u.person " +
            "WHERE (:number IS NULL OR u.number LIKE :number) AND (:type IS NULL OR u.type = :type)")
    List<Phone> findAllWithContaining(String number, String type);

    /**
     * Делает постраничную выборку телефонов, по заданному шаблону номера.
     * @param pageable      Номер и размер страницы.
     * @param numberPattern Шаблон строки поиска.
     */
    @Query("SELECT u FROM Phone u LEFT JOIN FETCH u.object LEFT JOIN FETCH u.person WHERE u.number LIKE :numberPattern")
    List<Phone> findAllWithLimit10(Pageable pageable, @Param("numberPattern") String numberPattern);

    /**
     * Возвращает телефон по ID, вместе с его зависимостями.
     */
    @Query("SELECT u FROM Phone u LEFT JOIN FETCH u.object LEFT JOIN FETCH u.person WHERE u.id = :id")
    Optional<Phone> findByIdWithDependencies(long id);

    /**
     * Возвращает все телефоны с их зависимостями.
     * @return
     */
    @Query("SELECT u FROM Phone u LEFT JOIN FETCH u.object LEFT JOIN FETCH u.person")
    List<Phone> findAllIdWithDependencies();


}
