package com.example.blogsearchapi.service;

import com.example.blogsearchapi.entity.Meta;
import com.example.blogsearchapi.entity.ResponseData;
import io.undertow.util.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeoutException;

@Service
public class BlogSearchService {

    private final SearchApi searchApi;

    private final PopularSearchService popularSearchService;

    @Autowired
    public BlogSearchService(@Qualifier("kakaoBlogSearchApi") SearchApi searchApi, PopularSearchService popularSearchService) {
        this.searchApi = searchApi;
        this.popularSearchService = popularSearchService;
    }

    public Mono<ResponseData> searchBlogPosts(String keyword, String sort, int page, int pageSize) {

        return Mono.fromCallable(() -> {
                    // 필수값 체크
                    if (StringUtils.isBlank(keyword)) {
                        throw new BadRequestException("키워드는 필수 값 입니다.");
                    }

                    // sort 값 체크
                    if (!StringUtils.equalsAny(sort, "accuracy", "recency")) {
                        throw new BadRequestException("Sort는 accuracy, recency 만 사용 가능 합니다.");
                    }

                    // page 범위 체크
                    if (page < 1 || page > 50) {
                        throw new BadRequestException("page는 1~50 사이의 값입니다.");
                    }

                    // pageSize 범위 체크
                    if (pageSize < 1 || pageSize > 50) {
                        throw new BadRequestException("pageSize는 1~50 사이의 값입니다.");
                    }

                    return new Object();
                })
                .then(searchApi.searchData(keyword, sort, page, pageSize))
                .flatMap(responseData ->
                    Mono.fromRunnable(() -> popularSearchService.updatePopularSearches(keyword))
                                .subscribeOn(Schedulers.boundedElastic())
                                .then(Mono.just(responseData))
                )
                .switchIfEmpty(Mono.defer(() -> Mono.just(new ResponseData())))
                .onErrorResume(BadRequestException.class, ex -> Mono.just(createErrorResponse("BadRequest", ex.getMessage())))
                .onErrorResume(TimeoutException.class, ex -> Mono.just(createErrorResponse("Timeout", ex.getMessage())));
    }

    private ResponseData createErrorResponse(String status, String message) {
        ResponseData responseData = new ResponseData();
        Meta meta = new Meta();

        meta.setMessage(message);
        meta.setStatus(status);

        responseData.setMeta(meta);
        return responseData;
    }
}
