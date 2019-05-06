package io.cooly.crawler.client;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@AuthorizedFeignClient(name = "fetcher")
public interface WebUrlClient {
    @RequestMapping("/api/web-urls")
    List<WebUrl> getFetches();
}
