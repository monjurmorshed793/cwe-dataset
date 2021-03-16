package ac.bd.buet.repository;

import ac.bd.buet.domain.CweList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CweList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CweListRepository extends JpaRepository<CweList, Long>, JpaSpecificationExecutor<CweList> {}
