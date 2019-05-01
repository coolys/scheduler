package io.cooly.crawler.repository.search;

import io.cooly.crawler.domain.ScheduleConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ScheduleConfig entity.
 */
public interface ScheduleConfigSearchRepository extends ElasticsearchRepository<ScheduleConfig, String> {
}
