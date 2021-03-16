package ac.bd.buet.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ac.bd.buet.domain.CweList;
import ac.bd.buet.repository.CweListRepository;
import ac.bd.buet.repository.search.CweListSearchRepository;
import ac.bd.buet.service.CweListService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CweList}.
 */
@Service
@Transactional
public class CweListServiceImpl implements CweListService {

    private final Logger log = LoggerFactory.getLogger(CweListServiceImpl.class);

    private final CweListRepository cweListRepository;

    private final CweListSearchRepository cweListSearchRepository;

    public CweListServiceImpl(CweListRepository cweListRepository, CweListSearchRepository cweListSearchRepository) {
        this.cweListRepository = cweListRepository;
        this.cweListSearchRepository = cweListSearchRepository;
    }

    @Override
    public CweList save(CweList cweList) {
        log.debug("Request to save CweList : {}", cweList);
        CweList result = cweListRepository.save(cweList);
        cweListSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<CweList> partialUpdate(CweList cweList) {
        log.debug("Request to partially update CweList : {}", cweList);

        return cweListRepository
            .findById(cweList.getId())
            .map(
                existingCweList -> {
                    if (cweList.getCweId() != null) {
                        existingCweList.setCweId(cweList.getCweId());
                    }

                    if (cweList.getDescription() != null) {
                        existingCweList.setDescription(cweList.getDescription());
                    }

                    if (cweList.getCode() != null) {
                        existingCweList.setCode(cweList.getCode());
                    }

                    if (cweList.getTags() != null) {
                        existingCweList.setTags(cweList.getTags());
                    }

                    return existingCweList;
                }
            )
            .map(cweListRepository::save)
            .map(
                savedCweList -> {
                    cweListSearchRepository.save(savedCweList);

                    return savedCweList;
                }
            );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CweList> findAll(Pageable pageable) {
        log.debug("Request to get all CweLists");
        return cweListRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CweList> findOne(Long id) {
        log.debug("Request to get CweList : {}", id);
        return cweListRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CweList : {}", id);
        cweListRepository.deleteById(id);
        cweListSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CweList> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CweLists for query {}", query);
        return cweListSearchRepository.search(queryStringQuery(query), pageable);
    }
}
