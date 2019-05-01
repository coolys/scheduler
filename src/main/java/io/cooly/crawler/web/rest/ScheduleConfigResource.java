package io.cooly.crawler.web.rest;
import io.cooly.crawler.domain.ScheduleConfig;
import io.cooly.crawler.service.ScheduleConfigService;
import io.cooly.crawler.web.rest.errors.BadRequestAlertException;
import io.cooly.crawler.web.rest.util.HeaderUtil;
import io.cooly.crawler.web.rest.util.PaginationUtil;
import io.github.coolys.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ScheduleConfig.
 */
@RestController
@RequestMapping("/api")
public class ScheduleConfigResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleConfigResource.class);

    private static final String ENTITY_NAME = "schedulerScheduleConfig";

    private final ScheduleConfigService scheduleConfigService;

    public ScheduleConfigResource(ScheduleConfigService scheduleConfigService) {
        this.scheduleConfigService = scheduleConfigService;
    }

    /**
     * POST  /schedule-configs : Create a new scheduleConfig.
     *
     * @param scheduleConfig the scheduleConfig to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scheduleConfig, or with status 400 (Bad Request) if the scheduleConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/schedule-configs")
    public ResponseEntity<ScheduleConfig> createScheduleConfig(@RequestBody ScheduleConfig scheduleConfig) throws URISyntaxException {
        log.debug("REST request to save ScheduleConfig : {}", scheduleConfig);
        if (scheduleConfig.getId() != null) {
            throw new BadRequestAlertException("A new scheduleConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScheduleConfig result = scheduleConfigService.save(scheduleConfig);
        return ResponseEntity.created(new URI("/api/schedule-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /schedule-configs : Updates an existing scheduleConfig.
     *
     * @param scheduleConfig the scheduleConfig to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scheduleConfig,
     * or with status 400 (Bad Request) if the scheduleConfig is not valid,
     * or with status 500 (Internal Server Error) if the scheduleConfig couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/schedule-configs")
    public ResponseEntity<ScheduleConfig> updateScheduleConfig(@RequestBody ScheduleConfig scheduleConfig) throws URISyntaxException {
        log.debug("REST request to update ScheduleConfig : {}", scheduleConfig);
        if (scheduleConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ScheduleConfig result = scheduleConfigService.save(scheduleConfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scheduleConfig.getId().toString()))
            .body(result);
    }

    /**
     * GET  /schedule-configs : get all the scheduleConfigs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of scheduleConfigs in body
     */
    @GetMapping("/schedule-configs")
    public ResponseEntity<List<ScheduleConfig>> getAllScheduleConfigs(Pageable pageable) {
        log.debug("REST request to get a page of ScheduleConfigs");
        Page<ScheduleConfig> page = scheduleConfigService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/schedule-configs");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /schedule-configs/:id : get the "id" scheduleConfig.
     *
     * @param id the id of the scheduleConfig to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scheduleConfig, or with status 404 (Not Found)
     */
    @GetMapping("/schedule-configs/{id}")
    public ResponseEntity<ScheduleConfig> getScheduleConfig(@PathVariable String id) {
        log.debug("REST request to get ScheduleConfig : {}", id);
        Optional<ScheduleConfig> scheduleConfig = scheduleConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scheduleConfig);
    }

    /**
     * DELETE  /schedule-configs/:id : delete the "id" scheduleConfig.
     *
     * @param id the id of the scheduleConfig to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/schedule-configs/{id}")
    public ResponseEntity<Void> deleteScheduleConfig(@PathVariable String id) {
        log.debug("REST request to delete ScheduleConfig : {}", id);
        scheduleConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/schedule-configs?query=:query : search for the scheduleConfig corresponding
     * to the query.
     *
     * @param query the query of the scheduleConfig search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/schedule-configs")
    public ResponseEntity<List<ScheduleConfig>> searchScheduleConfigs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ScheduleConfigs for query {}", query);
        Page<ScheduleConfig> page = scheduleConfigService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/schedule-configs");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
