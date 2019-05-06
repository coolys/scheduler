package io.cooly.crawler.service.impl;

import io.cooly.crawler.domain.Scheduler;
import io.cooly.crawler.repository.SchedulerRepository;
import io.cooly.crawler.repository.search.SchedulerSearchRepository;
import io.cooly.crawler.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Scheduler.
 */
@Service
public class SchedulerServiceImpl implements SchedulerService {

    private final Logger log = LoggerFactory.getLogger(SchedulerServiceImpl.class);

    private final SchedulerRepository schedulerRepository;

    private final SchedulerSearchRepository schedulerSearchRepository;

    public SchedulerServiceImpl(SchedulerRepository schedulerRepository, SchedulerSearchRepository schedulerSearchRepository) {
        this.schedulerRepository = schedulerRepository;
        this.schedulerSearchRepository = schedulerSearchRepository;
    }

    /**
     * Save a scheduler.
     *
     * @param scheduler the entity to save
     * @return the persisted entity
     */
    @Override
    public Scheduler save(Scheduler scheduler) {
        log.debug("Request to save Scheduler : {}", scheduler);
        Scheduler result = schedulerRepository.save(scheduler);
        schedulerSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the schedulers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Scheduler> findAll(Pageable pageable) {
        log.debug("Request to get all Schedulers");
        return schedulerRepository.findAll(pageable);
    }


    /**
     * Get one scheduler by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Scheduler> findOne(String id) {
        log.debug("Request to get Scheduler : {}", id);
        return schedulerRepository.findById(id);
    }

    /**
     * Delete the scheduler by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Scheduler : {}", id);
        schedulerRepository.deleteById(id);
        schedulerSearchRepository.deleteById(id);
    }

    /**
     * Search for the scheduler corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Scheduler> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Schedulers for query {}", query);
        return schedulerSearchRepository.search(queryStringQuery(query), pageable);    }
}
