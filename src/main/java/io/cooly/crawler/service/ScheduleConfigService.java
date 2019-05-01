package io.cooly.crawler.service;

import io.cooly.crawler.domain.ScheduleConfig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ScheduleConfig.
 */
public interface ScheduleConfigService {

    /**
     * Save a scheduleConfig.
     *
     * @param scheduleConfig the entity to save
     * @return the persisted entity
     */
    ScheduleConfig save(ScheduleConfig scheduleConfig);

    /**
     * Get all the scheduleConfigs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ScheduleConfig> findAll(Pageable pageable);


    /**
     * Get the "id" scheduleConfig.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ScheduleConfig> findOne(String id);

    /**
     * Delete the "id" scheduleConfig.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the scheduleConfig corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ScheduleConfig> search(String query, Pageable pageable);
}
