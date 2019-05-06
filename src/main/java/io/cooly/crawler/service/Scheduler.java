package io.cooly.crawler.service;



import io.cooly.crawler.client.WebUrl;
import io.cooly.crawler.client.WebUrlClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Scheduler {
    private final Logger log = LoggerFactory.getLogger(Scheduler.class);
    @Autowired
    private WebUrlClient webUrlClient;
    @Scheduled(fixedRate = 1000)
    public void fixedRateSch() {
        List<WebUrl> fetchList = webUrlClient.getFetches();
        for(WebUrl fetch: fetchList) {
            log.info("=======current fetch: {}", fetch);
        }

    }
}
