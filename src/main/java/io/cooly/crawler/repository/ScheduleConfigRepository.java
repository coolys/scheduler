package io.cooly.crawler.repository;

import io.cooly.crawler.domain.ScheduleConfig;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the ScheduleConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduleConfigRepository extends MongoRepository<ScheduleConfig, String> {

}
