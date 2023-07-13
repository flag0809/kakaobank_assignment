package com.example.blogsearchapi.service;

import com.example.blogsearchapi.entity.ResponseData;
import reactor.core.publisher.Mono;

public interface SearchApi {
    Mono<ResponseData> searchData(String keyword, String sort, int page, int pageSize);
}
