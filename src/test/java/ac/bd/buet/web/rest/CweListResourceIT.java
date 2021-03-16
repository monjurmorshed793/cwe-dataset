package ac.bd.buet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ac.bd.buet.IntegrationTest;
import ac.bd.buet.domain.CweList;
import ac.bd.buet.repository.CweListRepository;
import ac.bd.buet.repository.search.CweListSearchRepository;
import ac.bd.buet.service.CweListQueryService;
import ac.bd.buet.service.dto.CweListCriteria;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CweListResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CweListResourceIT {

    private static final String DEFAULT_CWE_ID = "AAAAAAAAAA";
    private static final String UPDATED_CWE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    @Autowired
    private CweListRepository cweListRepository;

    /**
     * This repository is mocked in the ac.bd.buet.repository.search test package.
     *
     * @see ac.bd.buet.repository.search.CweListSearchRepositoryMockConfiguration
     */
    @Autowired
    private CweListSearchRepository mockCweListSearchRepository;

    @Autowired
    private CweListQueryService cweListQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCweListMockMvc;

    private CweList cweList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CweList createEntity(EntityManager em) {
        CweList cweList = new CweList().cweId(DEFAULT_CWE_ID).description(DEFAULT_DESCRIPTION).code(DEFAULT_CODE).tags(DEFAULT_TAGS);
        return cweList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CweList createUpdatedEntity(EntityManager em) {
        CweList cweList = new CweList().cweId(UPDATED_CWE_ID).description(UPDATED_DESCRIPTION).code(UPDATED_CODE).tags(UPDATED_TAGS);
        return cweList;
    }

    @BeforeEach
    public void initTest() {
        cweList = createEntity(em);
    }

    @Test
    @Transactional
    void createCweList() throws Exception {
        int databaseSizeBeforeCreate = cweListRepository.findAll().size();
        // Create the CweList
        restCweListMockMvc
            .perform(post("/api/cwe-lists").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cweList)))
            .andExpect(status().isCreated());

        // Validate the CweList in the database
        List<CweList> cweListList = cweListRepository.findAll();
        assertThat(cweListList).hasSize(databaseSizeBeforeCreate + 1);
        CweList testCweList = cweListList.get(cweListList.size() - 1);
        assertThat(testCweList.getCweId()).isEqualTo(DEFAULT_CWE_ID);
        assertThat(testCweList.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCweList.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCweList.getTags()).isEqualTo(DEFAULT_TAGS);

        // Validate the CweList in Elasticsearch
        verify(mockCweListSearchRepository, times(1)).save(testCweList);
    }

    @Test
    @Transactional
    void createCweListWithExistingId() throws Exception {
        // Create the CweList with an existing ID
        cweList.setId(1L);

        int databaseSizeBeforeCreate = cweListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCweListMockMvc
            .perform(post("/api/cwe-lists").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cweList)))
            .andExpect(status().isBadRequest());

        // Validate the CweList in the database
        List<CweList> cweListList = cweListRepository.findAll();
        assertThat(cweListList).hasSize(databaseSizeBeforeCreate);

        // Validate the CweList in Elasticsearch
        verify(mockCweListSearchRepository, times(0)).save(cweList);
    }

    @Test
    @Transactional
    void getAllCweLists() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        // Get all the cweListList
        restCweListMockMvc
            .perform(get("/api/cwe-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cweList.getId().intValue())))
            .andExpect(jsonPath("$.[*].cweId").value(hasItem(DEFAULT_CWE_ID)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)));
    }

    @Test
    @Transactional
    void getCweList() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        // Get the cweList
        restCweListMockMvc
            .perform(get("/api/cwe-lists/{id}", cweList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cweList.getId().intValue()))
            .andExpect(jsonPath("$.cweId").value(DEFAULT_CWE_ID))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS));
    }

    @Test
    @Transactional
    void getCweListsByIdFiltering() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        Long id = cweList.getId();

        defaultCweListShouldBeFound("id.equals=" + id);
        defaultCweListShouldNotBeFound("id.notEquals=" + id);

        defaultCweListShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCweListShouldNotBeFound("id.greaterThan=" + id);

        defaultCweListShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCweListShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCweListsByCweIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        // Get all the cweListList where cweId equals to DEFAULT_CWE_ID
        defaultCweListShouldBeFound("cweId.equals=" + DEFAULT_CWE_ID);

        // Get all the cweListList where cweId equals to UPDATED_CWE_ID
        defaultCweListShouldNotBeFound("cweId.equals=" + UPDATED_CWE_ID);
    }

    @Test
    @Transactional
    void getAllCweListsByCweIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        // Get all the cweListList where cweId not equals to DEFAULT_CWE_ID
        defaultCweListShouldNotBeFound("cweId.notEquals=" + DEFAULT_CWE_ID);

        // Get all the cweListList where cweId not equals to UPDATED_CWE_ID
        defaultCweListShouldBeFound("cweId.notEquals=" + UPDATED_CWE_ID);
    }

    @Test
    @Transactional
    void getAllCweListsByCweIdIsInShouldWork() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        // Get all the cweListList where cweId in DEFAULT_CWE_ID or UPDATED_CWE_ID
        defaultCweListShouldBeFound("cweId.in=" + DEFAULT_CWE_ID + "," + UPDATED_CWE_ID);

        // Get all the cweListList where cweId equals to UPDATED_CWE_ID
        defaultCweListShouldNotBeFound("cweId.in=" + UPDATED_CWE_ID);
    }

    @Test
    @Transactional
    void getAllCweListsByCweIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        // Get all the cweListList where cweId is not null
        defaultCweListShouldBeFound("cweId.specified=true");

        // Get all the cweListList where cweId is null
        defaultCweListShouldNotBeFound("cweId.specified=false");
    }

    @Test
    @Transactional
    void getAllCweListsByCweIdContainsSomething() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        // Get all the cweListList where cweId contains DEFAULT_CWE_ID
        defaultCweListShouldBeFound("cweId.contains=" + DEFAULT_CWE_ID);

        // Get all the cweListList where cweId contains UPDATED_CWE_ID
        defaultCweListShouldNotBeFound("cweId.contains=" + UPDATED_CWE_ID);
    }

    @Test
    @Transactional
    void getAllCweListsByCweIdNotContainsSomething() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        // Get all the cweListList where cweId does not contain DEFAULT_CWE_ID
        defaultCweListShouldNotBeFound("cweId.doesNotContain=" + DEFAULT_CWE_ID);

        // Get all the cweListList where cweId does not contain UPDATED_CWE_ID
        defaultCweListShouldBeFound("cweId.doesNotContain=" + UPDATED_CWE_ID);
    }

    @Test
    @Transactional
    void getAllCweListsByTagsIsEqualToSomething() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        // Get all the cweListList where tags equals to DEFAULT_TAGS
        defaultCweListShouldBeFound("tags.equals=" + DEFAULT_TAGS);

        // Get all the cweListList where tags equals to UPDATED_TAGS
        defaultCweListShouldNotBeFound("tags.equals=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllCweListsByTagsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        // Get all the cweListList where tags not equals to DEFAULT_TAGS
        defaultCweListShouldNotBeFound("tags.notEquals=" + DEFAULT_TAGS);

        // Get all the cweListList where tags not equals to UPDATED_TAGS
        defaultCweListShouldBeFound("tags.notEquals=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllCweListsByTagsIsInShouldWork() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        // Get all the cweListList where tags in DEFAULT_TAGS or UPDATED_TAGS
        defaultCweListShouldBeFound("tags.in=" + DEFAULT_TAGS + "," + UPDATED_TAGS);

        // Get all the cweListList where tags equals to UPDATED_TAGS
        defaultCweListShouldNotBeFound("tags.in=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllCweListsByTagsIsNullOrNotNull() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        // Get all the cweListList where tags is not null
        defaultCweListShouldBeFound("tags.specified=true");

        // Get all the cweListList where tags is null
        defaultCweListShouldNotBeFound("tags.specified=false");
    }

    @Test
    @Transactional
    void getAllCweListsByTagsContainsSomething() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        // Get all the cweListList where tags contains DEFAULT_TAGS
        defaultCweListShouldBeFound("tags.contains=" + DEFAULT_TAGS);

        // Get all the cweListList where tags contains UPDATED_TAGS
        defaultCweListShouldNotBeFound("tags.contains=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllCweListsByTagsNotContainsSomething() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        // Get all the cweListList where tags does not contain DEFAULT_TAGS
        defaultCweListShouldNotBeFound("tags.doesNotContain=" + DEFAULT_TAGS);

        // Get all the cweListList where tags does not contain UPDATED_TAGS
        defaultCweListShouldBeFound("tags.doesNotContain=" + UPDATED_TAGS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCweListShouldBeFound(String filter) throws Exception {
        restCweListMockMvc
            .perform(get("/api/cwe-lists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cweList.getId().intValue())))
            .andExpect(jsonPath("$.[*].cweId").value(hasItem(DEFAULT_CWE_ID)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)));

        // Check, that the count call also returns 1
        restCweListMockMvc
            .perform(get("/api/cwe-lists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCweListShouldNotBeFound(String filter) throws Exception {
        restCweListMockMvc
            .perform(get("/api/cwe-lists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCweListMockMvc
            .perform(get("/api/cwe-lists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCweList() throws Exception {
        // Get the cweList
        restCweListMockMvc.perform(get("/api/cwe-lists/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateCweList() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        int databaseSizeBeforeUpdate = cweListRepository.findAll().size();

        // Update the cweList
        CweList updatedCweList = cweListRepository.findById(cweList.getId()).get();
        // Disconnect from session so that the updates on updatedCweList are not directly saved in db
        em.detach(updatedCweList);
        updatedCweList.cweId(UPDATED_CWE_ID).description(UPDATED_DESCRIPTION).code(UPDATED_CODE).tags(UPDATED_TAGS);

        restCweListMockMvc
            .perform(
                put("/api/cwe-lists").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedCweList))
            )
            .andExpect(status().isOk());

        // Validate the CweList in the database
        List<CweList> cweListList = cweListRepository.findAll();
        assertThat(cweListList).hasSize(databaseSizeBeforeUpdate);
        CweList testCweList = cweListList.get(cweListList.size() - 1);
        assertThat(testCweList.getCweId()).isEqualTo(UPDATED_CWE_ID);
        assertThat(testCweList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCweList.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCweList.getTags()).isEqualTo(UPDATED_TAGS);

        // Validate the CweList in Elasticsearch
        verify(mockCweListSearchRepository).save(testCweList);
    }

    @Test
    @Transactional
    void updateNonExistingCweList() throws Exception {
        int databaseSizeBeforeUpdate = cweListRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCweListMockMvc
            .perform(put("/api/cwe-lists").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cweList)))
            .andExpect(status().isBadRequest());

        // Validate the CweList in the database
        List<CweList> cweListList = cweListRepository.findAll();
        assertThat(cweListList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CweList in Elasticsearch
        verify(mockCweListSearchRepository, times(0)).save(cweList);
    }

    @Test
    @Transactional
    void partialUpdateCweListWithPatch() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        int databaseSizeBeforeUpdate = cweListRepository.findAll().size();

        // Update the cweList using partial update
        CweList partialUpdatedCweList = new CweList();
        partialUpdatedCweList.setId(cweList.getId());

        partialUpdatedCweList.cweId(UPDATED_CWE_ID).description(UPDATED_DESCRIPTION).code(UPDATED_CODE).tags(UPDATED_TAGS);

        restCweListMockMvc
            .perform(
                patch("/api/cwe-lists")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCweList))
            )
            .andExpect(status().isOk());

        // Validate the CweList in the database
        List<CweList> cweListList = cweListRepository.findAll();
        assertThat(cweListList).hasSize(databaseSizeBeforeUpdate);
        CweList testCweList = cweListList.get(cweListList.size() - 1);
        assertThat(testCweList.getCweId()).isEqualTo(UPDATED_CWE_ID);
        assertThat(testCweList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCweList.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCweList.getTags()).isEqualTo(UPDATED_TAGS);
    }

    @Test
    @Transactional
    void fullUpdateCweListWithPatch() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        int databaseSizeBeforeUpdate = cweListRepository.findAll().size();

        // Update the cweList using partial update
        CweList partialUpdatedCweList = new CweList();
        partialUpdatedCweList.setId(cweList.getId());

        partialUpdatedCweList.cweId(UPDATED_CWE_ID).description(UPDATED_DESCRIPTION).code(UPDATED_CODE).tags(UPDATED_TAGS);

        restCweListMockMvc
            .perform(
                patch("/api/cwe-lists")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCweList))
            )
            .andExpect(status().isOk());

        // Validate the CweList in the database
        List<CweList> cweListList = cweListRepository.findAll();
        assertThat(cweListList).hasSize(databaseSizeBeforeUpdate);
        CweList testCweList = cweListList.get(cweListList.size() - 1);
        assertThat(testCweList.getCweId()).isEqualTo(UPDATED_CWE_ID);
        assertThat(testCweList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCweList.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCweList.getTags()).isEqualTo(UPDATED_TAGS);
    }

    @Test
    @Transactional
    void partialUpdateCweListShouldThrown() throws Exception {
        // Update the cweList without id should throw
        CweList partialUpdatedCweList = new CweList();

        restCweListMockMvc
            .perform(
                patch("/api/cwe-lists")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCweList))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteCweList() throws Exception {
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);

        int databaseSizeBeforeDelete = cweListRepository.findAll().size();

        // Delete the cweList
        restCweListMockMvc
            .perform(delete("/api/cwe-lists/{id}", cweList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CweList> cweListList = cweListRepository.findAll();
        assertThat(cweListList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CweList in Elasticsearch
        verify(mockCweListSearchRepository, times(1)).deleteById(cweList.getId());
    }

    @Test
    @Transactional
    void searchCweList() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cweListRepository.saveAndFlush(cweList);
        when(mockCweListSearchRepository.search(queryStringQuery("id:" + cweList.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cweList), PageRequest.of(0, 1), 1));

        // Search the cweList
        restCweListMockMvc
            .perform(get("/api/_search/cwe-lists?query=id:" + cweList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cweList.getId().intValue())))
            .andExpect(jsonPath("$.[*].cweId").value(hasItem(DEFAULT_CWE_ID)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)));
    }
}
