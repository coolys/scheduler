package io.cooly.crawler.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ScheduleConfigSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ScheduleConfigSearchRepositoryMockConfiguration {

    @MockBean
    private ScheduleConfigSearchRepository mockScheduleConfigSearchRepository;

}
