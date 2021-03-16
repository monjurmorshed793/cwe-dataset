package ac.bd.buet.service;

import ac.bd.buet.domain.*; // for static metamodels
import ac.bd.buet.domain.CweList;
import ac.bd.buet.repository.CweListRepository;
import ac.bd.buet.repository.search.CweListSearchRepository;
import ac.bd.buet.service.dto.CweListCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CweList} entities in the database.
 * The main input is a {@link CweListCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CweList} or a {@link Page} of {@link CweList} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CweListQueryService extends QueryService<CweList> {

    private final Logger log = LoggerFactory.getLogger(CweListQueryService.class);

    private final CweListRepository cweListRepository;

    private final CweListSearchRepository cweListSearchRepository;

    public CweListQueryService(CweListRepository cweListRepository, CweListSearchRepository cweListSearchRepository) {
        this.cweListRepository = cweListRepository;
        this.cweListSearchRepository = cweListSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CweList} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CweList> findByCriteria(CweListCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CweList> specification = createSpecification(criteria);
        return cweListRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CweList} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CweList> findByCriteria(CweListCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CweList> specification = createSpecification(criteria);
        return cweListRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CweListCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CweList> specification = createSpecification(criteria);
        return cweListRepository.count(specification);
    }

    /**
     * Function to convert {@link CweListCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CweList> createSpecification(CweListCriteria criteria) {
        Specification<CweList> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CweList_.id));
            }
            if (criteria.getCweId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCweId(), CweList_.cweId));
            }
            if (criteria.getTags() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTags(), CweList_.tags));
            }
        }
        return specification;
    }
}
