package vn.edu.fpt.savora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.savora.entity.DiningTable;
import vn.edu.fpt.savora.enums.TableStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTable, Integer> {
    Optional<DiningTable> findByTableCodeIgnoreCase(String tableCode);
    List<DiningTable> findByStatusNotOrderByTableCodeAsc(TableStatus excludedStatus);
}
