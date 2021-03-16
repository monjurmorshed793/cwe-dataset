package ac.bd.buet.service;

import ac.bd.buet.domain.CweList;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CweList}.
 */
public interface CweListService {
    /**
     * Save a cweList.
     *
     * @param cweList the entity to save.
     * @return the persisted entity.
     */
    CweList save(CweList cweList);

    /**
     * Partially updates a cweList.
     *
     * @param cweList the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CweList> partialUpdate(CweList cweList);

    /**
     * Get all the cweLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CweList> findAll(Pageable pageable);

    /**
     * Get the "id" cweList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CweList> findOne(Long id);

    /**
     * Delete the "id" cweList.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the cweList corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CweList> search(String query, Pageable pageable);
}
