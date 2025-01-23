package mr.demonid.logger.service.repository;

import mr.demonid.logger.service.domain.LogOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogOperation, Long>, JpaSpecificationExecutor<LogOperation> {
}

