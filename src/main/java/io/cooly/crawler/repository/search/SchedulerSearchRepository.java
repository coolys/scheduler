package io.cooly.crawler.repository.search;


import io.cooly.crawler.domain.Scheduler;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Scheduler entity.
 */
public interface SchedulerSearchRepository extends ElasticsearchRepository<Scheduler, String> {
}
