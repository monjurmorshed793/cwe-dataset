package ac.bd.buet.repository.search;

import ac.bd.buet.domain.CweList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CweList} entity.
 */
public interface CweListSearchRepository extends ElasticsearchRepository<CweList, Long> {}
