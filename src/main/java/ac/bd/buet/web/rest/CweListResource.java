package ac.bd.buet.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import ac.bd.buet.domain.CweList;
import ac.bd.buet.service.CweListQueryService;
import ac.bd.buet.service.CweListService;
import ac.bd.buet.service.dto.CweListCriteria;
import ac.bd.buet.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ac.bd.buet.domain.CweList}.
 */
@RestController
@RequestMapping("/api")
public class CweListResource {

    private final Logger log = LoggerFactory.getLogger(CweListResource.class);

    private static final String ENTITY_NAME = "cweList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CweListService cweListService;

    private final CweListQueryService cweListQueryService;

    public CweListResource(CweListService cweListService, CweListQueryService cweListQueryService) {
        this.cweListService = cweListService;
        this.cweListQueryService = cweListQueryService;
    }

    /**
     * {@code POST  /cwe-lists} : Create a new cweList.
     *
     * @param cweList the cweList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cweList, or with status {@code 400 (Bad Request)} if the cweList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cwe-lists")
    public ResponseEntity<CweList> createCweList(@RequestBody CweList cweList) throws URISyntaxException {
        log.debug("REST request to save CweList : {}", cweList);
        if (cweList.getId() != null) {
            throw new BadRequestAlertException("A new cweList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CweList result = cweListService.save(cweList);
        return ResponseEntity
            .created(new URI("/api/cwe-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cwe-lists} : Updates an existing cweList.
     *
     * @param cweList the cweList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cweList,
     * or with status {@code 400 (Bad Request)} if the cweList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cweList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cwe-lists")
    public ResponseEntity<CweList> updateCweList(@RequestBody CweList cweList) throws URISyntaxException {
        log.debug("REST request to update CweList : {}", cweList);
        if (cweList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CweList result = cweListService.save(cweList);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cweList.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cwe-lists} : Updates given fields of an existing cweList.
     *
     * @param cweList the cweList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cweList,
     * or with status {@code 400 (Bad Request)} if the cweList is not valid,
     * or with status {@code 404 (Not Found)} if the cweList is not found,
     * or with status {@code 500 (Internal Server Error)} if the cweList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cwe-lists", consumes = "application/merge-patch+json")
    public ResponseEntity<CweList> partialUpdateCweList(@RequestBody CweList cweList) throws URISyntaxException {
        log.debug("REST request to update CweList partially : {}", cweList);
        if (cweList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<CweList> result = cweListService.partialUpdate(cweList);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cweList.getId().toString())
        );
    }

    /**
     * {@code GET  /cwe-lists} : get all the cweLists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cweLists in body.
     */
    @GetMapping("/cwe-lists")
    public ResponseEntity<List<CweList>> getAllCweLists(CweListCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CweLists by criteria: {}", criteria);
        Page<CweList> page = cweListQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cwe-lists/count} : count all the cweLists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cwe-lists/count")
    public ResponseEntity<Long> countCweLists(CweListCriteria criteria) {
        log.debug("REST request to count CweLists by criteria: {}", criteria);
        return ResponseEntity.ok().body(cweListQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cwe-lists/:id} : get the "id" cweList.
     *
     * @param id the id of the cweList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cweList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cwe-lists/{id}")
    public ResponseEntity<CweList> getCweList(@PathVariable Long id) {
        log.debug("REST request to get CweList : {}", id);
        Optional<CweList> cweList = cweListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cweList);
    }

    /**
     * {@code DELETE  /cwe-lists/:id} : delete the "id" cweList.
     *
     * @param id the id of the cweList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cwe-lists/{id}")
    public ResponseEntity<Void> deleteCweList(@PathVariable Long id) {
        log.debug("REST request to delete CweList : {}", id);
        cweListService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/cwe-lists?query=:query} : search for the cweList corresponding
     * to the query.
     *
     * @param query the query of the cweList search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/cwe-lists")
    public ResponseEntity<List<CweList>> searchCweLists(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CweLists for query {}", query);
        Page<CweList> page = cweListService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
