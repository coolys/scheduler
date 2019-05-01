package io.cooly.crawler.service;

import io.cooly.crawler.domain.Schedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Schedule.
 */
public interface ScheduleService {

    /**
     * Save a schedule.
     *
     * @param schedule the entity to save
     * @return the persisted entity
     */
    Schedule save(Schedule schedule);

    /**
     * Get all the schedules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Schedule> findAll(Pageable pageable);


    /**
     * Get the "id" schedule.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Schedule> findOne(String id);

    /**
     * Delete the "id" schedule.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the schedule corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Schedule> search(String query, Pageable pageable);
}
