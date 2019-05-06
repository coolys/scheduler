package io.cooly.crawler.web.rest;

import io.cooly.crawler.domain.Scheduler;
import io.cooly.crawler.repository.SchedulerRepository;
import io.cooly.crawler.repository.search.SchedulerSearchRepository;
import io.cooly.crawler.web.rest.errors.BadRequestAlertException;
import io.cooly.crawler.web.rest.util.HeaderUtil;
import io.github.coolys.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Scheduler.
 */
@RestController
@RequestMapping("/api")
public class SchedulerResource {

    private final Logger log = LoggerFactory.getLogger(SchedulerResource.class);

    private static final String ENTITY_NAME = "schedulerScheduler";

    private final SchedulerRepository schedulerRepository;

    private final SchedulerSearchRepository schedulerSearchRepository;

    public SchedulerResource(SchedulerRepository schedulerRepository, SchedulerSearchRepository schedulerSearchRepository) {
        this.schedulerRepository = schedulerRepository;
        this.schedulerSearchRepository = schedulerSearchRepository;
    }

    /**
     * POST  /schedulers : Create a new scheduler.
     *
     * @param scheduler the scheduler to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scheduler, or with status 400 (Bad Request) if the scheduler has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/schedulers")
    public ResponseEntity<Scheduler> createScheduler(@RequestBody Scheduler scheduler) throws URISyntaxException {
        log.debug("REST request to save Scheduler : {}", scheduler);
        if (scheduler.getId() != null) {
            throw new BadRequestAlertException("A new scheduler cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Scheduler result = schedulerRepository.save(scheduler);
        schedulerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/schedulers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /schedulers : Updates an existing scheduler.
     *
     * @param scheduler the scheduler to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scheduler,
     * or with status 400 (Bad Request) if the scheduler is not valid,
     * or with status 500 (Internal Server Error) if the scheduler couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/schedulers")
    public ResponseEntity<Scheduler> updateScheduler(@RequestBody Scheduler scheduler) throws URISyntaxException {
        log.debug("REST request to update Scheduler : {}", scheduler);
        if (scheduler.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Scheduler result = schedulerRepository.save(scheduler);
        schedulerSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scheduler.getId().toString()))
            .body(result);
    }

    /**
     * GET  /schedulers : get all the schedulers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of schedulers in body
     */
    @GetMapping("/schedulers")
    public List<Scheduler> getAllSchedulers() {
        log.debug("REST request to get all Schedulers");
        return schedulerRepository.findAll();
    }

    /**
     * GET  /schedulers/:id : get the "id" scheduler.
     *
     * @param id the id of the scheduler to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scheduler, or with status 404 (Not Found)
     */
    @GetMapping("/schedulers/{id}")
    public ResponseEntity<Scheduler> getScheduler(@PathVariable String id) {
        log.debug("REST request to get Scheduler : {}", id);
        Optional<Scheduler> scheduler = schedulerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(scheduler);
    }

    /**
     * DELETE  /schedulers/:id : delete the "id" scheduler.
     *
     * @param id the id of the scheduler to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/schedulers/{id}")
    public ResponseEntity<Void> deleteScheduler(@PathVariable String id) {
        log.debug("REST request to delete Scheduler : {}", id);
        schedulerRepository.deleteById(id);
        schedulerSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/schedulers?query=:query : search for the scheduler corresponding
     * to the query.
     *
     * @param query the query of the scheduler search
     * @return the result of the search
     */
    @GetMapping("/_search/schedulers")
    public List<Scheduler> searchSchedulers(@RequestParam String query) {
        log.debug("REST request to search Schedulers for query {}", query);
        return StreamSupport
            .stream(schedulerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
