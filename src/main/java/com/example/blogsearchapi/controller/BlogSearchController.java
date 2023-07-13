package com.example.blogsearchapi.controller;

import com.example.blogsearchapi.entity.PopularSearch;
import com.example.blogsearchapi.entity.ResponseData;
import com.example.blogsearchapi.service.BlogSearchService;
import com.example.blogsearchapi.service.PopularSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BlogSearchController {

    private final BlogSearchService blogSearchService;

    private final PopularSearchService popularSearchService;

    @Autowired
    public BlogSearchController(BlogSearchService blogSearchService, PopularSearchService popularSearchService) {
        this.blogSearchService = blogSearchService;
        this.popularSearchService = popularSearchService;
    }

    @GetMapping("/blog")
    public Mono<ResponseEntity<ResponseData>> searchBlogPosts(@RequestParam("keyword") String keyword,
                                                              @RequestParam(value = "sort", required = false, defaultValue = "accuracy") String sort,
                                                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return blogSearchService.searchBlogPosts(keyword, sort, page, pageSize)
                .map(responseData -> ResponseEntity.ok().body(responseData))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/popular-searches")
    public List<PopularSearch> getPopularSearches() {
        return popularSearchService.getPopularSearches();
    }
}
