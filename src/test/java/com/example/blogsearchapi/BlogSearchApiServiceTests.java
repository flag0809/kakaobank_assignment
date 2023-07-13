package com.example.blogsearchapi;

import com.example.blogsearchapi.entity.ResponseData;
import com.example.blogsearchapi.service.BlogSearchService;
import com.example.blogsearchapi.service.PopularSearchService;
import com.example.blogsearchapi.service.SearchApi;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class BlogSearchApiServiceTests {

	@Autowired
	private BlogSearchService blogSearchService;

	@MockBean
	@Qualifier("kakaoBlogSearchApi")
	private SearchApi searchApi;

	@MockBean
	private PopularSearchService popularSearchService;

	@Test
	void searchBlogPostsTest() {
		String keyword = "test";
		String sort = "accuracy";
		int page = 1;
		int pageSize = 10;

		ResponseData expectedResponseData = new ResponseData();

		Mockito.when(searchApi.searchData(keyword, sort, page, pageSize)).thenReturn(Mono.just(expectedResponseData));

		Mono<ResponseData> actualMono = blogSearchService.searchBlogPosts(keyword, sort, page, pageSize);

		StepVerifier.create(actualMono)
				.expectNext(expectedResponseData)
				.verifyComplete();

		verify(searchApi, times(1)).searchData(keyword, sort, page, pageSize);
		verify(popularSearchService, times(1)).updatePopularSearches(keyword);
	}

}
