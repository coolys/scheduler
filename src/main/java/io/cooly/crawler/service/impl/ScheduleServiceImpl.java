package io.cooly.crawler.service.impl;

import io.cooly.crawler.service.ScheduleService;
import io.cooly.crawler.domain.Schedule;
import io.cooly.crawler.repository.ScheduleRepository;
import io.cooly.crawler.repository.search.ScheduleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Schedule.
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final Logger log = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    private final ScheduleRepository scheduleRepository;

    private final ScheduleSearchRepository scheduleSearchRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, ScheduleSearchRepository scheduleSearchRepository) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleSearchRepository = scheduleSearchRepository;
    }

    /**
     * Save a schedule.
     *
     * @param schedule the entity to save
     * @return the persisted entity
     */
    @Override
    public Schedule save(Schedule schedule) {
        log.debug("Request to save Schedule : {}", schedule);
        Schedule result = scheduleRepository.save(schedule);
        scheduleSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the schedules.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Schedule> findAll(Pageable pageable) {
        log.debug("Request to get all Schedules");
        return scheduleRepository.findAll(pageable);
    }


    /**
     * Get one schedule by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Schedule> findOne(String id) {
        log.debug("Request to get Schedule : {}", id);
        return scheduleRepository.findById(id);
    }

    /**
     * Delete the schedule by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Schedule : {}", id);
        scheduleRepository.deleteById(id);
        scheduleSearchRepository.deleteById(id);
    }

    /**
     * Search for the schedule corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Schedule> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Schedules for query {}", query);
        return scheduleSearchRepository.search(queryStringQuery(query), pageable);    }
}
