package io.cooly.crawler.service.impl;

import io.cooly.crawler.service.ScheduleConfigService;
import io.cooly.crawler.domain.ScheduleConfig;
import io.cooly.crawler.repository.ScheduleConfigRepository;
import io.cooly.crawler.repository.search.ScheduleConfigSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ScheduleConfig.
 */
@Service
public class ScheduleConfigServiceImpl implements ScheduleConfigService {

    private final Logger log = LoggerFactory.getLogger(ScheduleConfigServiceImpl.class);

    private final ScheduleConfigRepository scheduleConfigRepository;

    private final ScheduleConfigSearchRepository scheduleConfigSearchRepository;

    public ScheduleConfigServiceImpl(ScheduleConfigRepository scheduleConfigRepository, ScheduleConfigSearchRepository scheduleConfigSearchRepository) {
        this.scheduleConfigRepository = scheduleConfigRepository;
        this.scheduleConfigSearchRepository = scheduleConfigSearchRepository;
    }

    /**
     * Save a scheduleConfig.
     *
     * @param scheduleConfig the entity to save
     * @return the persisted entity
     */
    @Override
    public ScheduleConfig save(ScheduleConfig scheduleConfig) {
        log.debug("Request to save ScheduleConfig : {}", scheduleConfig);
        ScheduleConfig result = scheduleConfigRepository.save(scheduleConfig);
        scheduleConfigSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the scheduleConfigs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ScheduleConfig> findAll(Pageable pageable) {
        log.debug("Request to get all ScheduleConfigs");
        return scheduleConfigRepository.findAll(pageable);
    }


    /**
     * Get one scheduleConfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<ScheduleConfig> findOne(String id) {
        log.debug("Request to get ScheduleConfig : {}", id);
        return scheduleConfigRepository.findById(id);
    }

    /**
     * Delete the scheduleConfig by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete ScheduleConfig : {}", id);
        scheduleConfigRepository.deleteById(id);
        scheduleConfigSearchRepository.deleteById(id);
    }

    /**
     * Search for the scheduleConfig corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ScheduleConfig> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ScheduleConfigs for query {}", query);
        return scheduleConfigSearchRepository.search(queryStringQuery(query), pageable);    }
}
