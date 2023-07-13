package com.example.blogsearchapi;

import com.example.blogsearchapi.entity.PopularSearch;
import com.example.blogsearchapi.entity.ResponseData;
import com.example.blogsearchapi.service.BlogSearchService;
import com.example.blogsearchapi.service.PopularSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class BlogSearchApiControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BlogSearchService blogSearchService;

    @MockBean
    private PopularSearchService popularSearchService;

    @Test
    void testSearchBlogPosts() {
        String keyword = "example";
        String sort = "recency";
        int page = 1;
        int pageSize = 10;

        ResponseData responseData = new ResponseData();

        when(blogSearchService.searchBlogPosts(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(Mono.just(responseData));

        webTestClient.get().uri("/api/blog?keyword={keyword}&sort={sort}&page={page}&pageSize={pageSize}", keyword, sort, page, pageSize)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseData.class)
                .isEqualTo(responseData);

        verify(blogSearchService).searchBlogPosts(keyword, sort, page, pageSize);
    }

    @Test
    void testGetPopularSearches() {

        List<PopularSearch> popularSearches = new ArrayList<>();

        when(popularSearchService.getPopularSearches()).thenReturn(popularSearches);

        webTestClient.get().uri("/api/popular-searches")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PopularSearch.class)
                .isEqualTo(popularSearches);

        // BlogSearchService의 메서드가 호출되었는지 확인
        verify(popularSearchService).getPopularSearches();
    }

}
