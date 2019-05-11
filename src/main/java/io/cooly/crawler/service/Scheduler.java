package io.cooly.crawler.service;



import io.cooly.crawler.client.WebUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.cooly.crawler.client.FetcherServiceClient;

@Component
public class Scheduler {
    private final Logger log = LoggerFactory.getLogger(Scheduler.class);
    @Autowired
    private FetcherServiceClient webUrlClient;
    @Scheduled(fixedRate = 1000)
    public void fixedRateSch() {
        WebUrl web = new WebUrl();
        web.setUrl("https://vnexpress.net/");
        webUrlClient.createWebUrl(web);

    }
}
