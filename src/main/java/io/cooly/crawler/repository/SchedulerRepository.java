package io.cooly.crawler.repository;


import io.cooly.crawler.domain.Scheduler;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Scheduler entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchedulerRepository extends MongoRepository<Scheduler, String> {

}
