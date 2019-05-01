package io.cooly.crawler.web.rest;

import io.cooly.crawler.SchedulerApp;

import io.cooly.crawler.config.SecurityBeanOverrideConfiguration;

import io.cooly.crawler.domain.ScheduleConfig;
import io.cooly.crawler.repository.ScheduleConfigRepository;
import io.cooly.crawler.repository.search.ScheduleConfigSearchRepository;
import io.cooly.crawler.service.ScheduleConfigService;
import io.cooly.crawler.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.List;


import static io.cooly.crawler.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ScheduleConfigResource REST controller.
 *
 * @see ScheduleConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SchedulerApp.class})
public class ScheduleConfigResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ScheduleConfigRepository scheduleConfigRepository;

    @Autowired
    private ScheduleConfigService scheduleConfigService;

    /**
     * This repository is mocked in the io.cooly.crawler.repository.search test package.
     *
     * @see io.cooly.crawler.repository.search.ScheduleConfigSearchRepositoryMockConfiguration
     */
    @Autowired
    private ScheduleConfigSearchRepository mockScheduleConfigSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restScheduleConfigMockMvc;

    private ScheduleConfig scheduleConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScheduleConfigResource scheduleConfigResource = new ScheduleConfigResource(scheduleConfigService);
        this.restScheduleConfigMockMvc = MockMvcBuilders.standaloneSetup(scheduleConfigResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleConfig createEntity() {
        ScheduleConfig scheduleConfig = new ScheduleConfig()
            .name(DEFAULT_NAME);
        return scheduleConfig;
    }

    @Before
    public void initTest() {
        scheduleConfigRepository.deleteAll();
        scheduleConfig = createEntity();
    }

    @Test
    public void createScheduleConfig() throws Exception {
        int databaseSizeBeforeCreate = scheduleConfigRepository.findAll().size();

        // Create the ScheduleConfig
        restScheduleConfigMockMvc.perform(post("/api/schedule-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleConfig)))
            .andExpect(status().isCreated());

        // Validate the ScheduleConfig in the database
        List<ScheduleConfig> scheduleConfigList = scheduleConfigRepository.findAll();
        assertThat(scheduleConfigList).hasSize(databaseSizeBeforeCreate + 1);
        ScheduleConfig testScheduleConfig = scheduleConfigList.get(scheduleConfigList.size() - 1);
        assertThat(testScheduleConfig.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the ScheduleConfig in Elasticsearch
        verify(mockScheduleConfigSearchRepository, times(1)).save(testScheduleConfig);
    }

    @Test
    public void createScheduleConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scheduleConfigRepository.findAll().size();

        // Create the ScheduleConfig with an existing ID
        scheduleConfig.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleConfigMockMvc.perform(post("/api/schedule-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleConfig)))
            .andExpect(status().isBadRequest());

        // Validate the ScheduleConfig in the database
        List<ScheduleConfig> scheduleConfigList = scheduleConfigRepository.findAll();
        assertThat(scheduleConfigList).hasSize(databaseSizeBeforeCreate);

        // Validate the ScheduleConfig in Elasticsearch
        verify(mockScheduleConfigSearchRepository, times(0)).save(scheduleConfig);
    }

    @Test
    public void getAllScheduleConfigs() throws Exception {
        // Initialize the database
        scheduleConfigRepository.save(scheduleConfig);

        // Get all the scheduleConfigList
        restScheduleConfigMockMvc.perform(get("/api/schedule-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleConfig.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    public void getScheduleConfig() throws Exception {
        // Initialize the database
        scheduleConfigRepository.save(scheduleConfig);

        // Get the scheduleConfig
        restScheduleConfigMockMvc.perform(get("/api/schedule-configs/{id}", scheduleConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(scheduleConfig.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    public void getNonExistingScheduleConfig() throws Exception {
        // Get the scheduleConfig
        restScheduleConfigMockMvc.perform(get("/api/schedule-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateScheduleConfig() throws Exception {
        // Initialize the database
        scheduleConfigService.save(scheduleConfig);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockScheduleConfigSearchRepository);

        int databaseSizeBeforeUpdate = scheduleConfigRepository.findAll().size();

        // Update the scheduleConfig
        ScheduleConfig updatedScheduleConfig = scheduleConfigRepository.findById(scheduleConfig.getId()).get();
        updatedScheduleConfig
            .name(UPDATED_NAME);

        restScheduleConfigMockMvc.perform(put("/api/schedule-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedScheduleConfig)))
            .andExpect(status().isOk());

        // Validate the ScheduleConfig in the database
        List<ScheduleConfig> scheduleConfigList = scheduleConfigRepository.findAll();
        assertThat(scheduleConfigList).hasSize(databaseSizeBeforeUpdate);
        ScheduleConfig testScheduleConfig = scheduleConfigList.get(scheduleConfigList.size() - 1);
        assertThat(testScheduleConfig.getName()).isEqualTo(UPDATED_NAME);

        // Validate the ScheduleConfig in Elasticsearch
        verify(mockScheduleConfigSearchRepository, times(1)).save(testScheduleConfig);
    }

    @Test
    public void updateNonExistingScheduleConfig() throws Exception {
        int databaseSizeBeforeUpdate = scheduleConfigRepository.findAll().size();

        // Create the ScheduleConfig

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleConfigMockMvc.perform(put("/api/schedule-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleConfig)))
            .andExpect(status().isBadRequest());

        // Validate the ScheduleConfig in the database
        List<ScheduleConfig> scheduleConfigList = scheduleConfigRepository.findAll();
        assertThat(scheduleConfigList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ScheduleConfig in Elasticsearch
        verify(mockScheduleConfigSearchRepository, times(0)).save(scheduleConfig);
    }

    @Test
    public void deleteScheduleConfig() throws Exception {
        // Initialize the database
        scheduleConfigService.save(scheduleConfig);

        int databaseSizeBeforeDelete = scheduleConfigRepository.findAll().size();

        // Delete the scheduleConfig
        restScheduleConfigMockMvc.perform(delete("/api/schedule-configs/{id}", scheduleConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ScheduleConfig> scheduleConfigList = scheduleConfigRepository.findAll();
        assertThat(scheduleConfigList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ScheduleConfig in Elasticsearch
        verify(mockScheduleConfigSearchRepository, times(1)).deleteById(scheduleConfig.getId());
    }

    @Test
    public void searchScheduleConfig() throws Exception {
        // Initialize the database
        scheduleConfigService.save(scheduleConfig);
        when(mockScheduleConfigSearchRepository.search(queryStringQuery("id:" + scheduleConfig.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(scheduleConfig), PageRequest.of(0, 1), 1));
        // Search the scheduleConfig
        restScheduleConfigMockMvc.perform(get("/api/_search/schedule-configs?query=id:" + scheduleConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleConfig.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduleConfig.class);
        ScheduleConfig scheduleConfig1 = new ScheduleConfig();
        scheduleConfig1.setId("id1");
        ScheduleConfig scheduleConfig2 = new ScheduleConfig();
        scheduleConfig2.setId(scheduleConfig1.getId());
        assertThat(scheduleConfig1).isEqualTo(scheduleConfig2);
        scheduleConfig2.setId("id2");
        assertThat(scheduleConfig1).isNotEqualTo(scheduleConfig2);
        scheduleConfig1.setId(null);
        assertThat(scheduleConfig1).isNotEqualTo(scheduleConfig2);
    }
}
