package com.example.blogsearchapi.service;

import com.example.blogsearchapi.entity.Meta;
import com.example.blogsearchapi.entity.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class KakaoBlogSearchApi implements SearchApi {

    private final NaverBlogSearchApi naverBlogSearchApi;

    private final WebClient webClient;

    @Autowired
    public KakaoBlogSearchApi(NaverBlogSearchApi naverBlogSearchApi) {
        this.webClient = WebClient.create();
        this.naverBlogSearchApi = naverBlogSearchApi;
    }
    @Value(value = "${kakao.rest.api-key}")
    private String apiKey;

    @Value(value = "${kakao.rest.url}")
    private String url;

    public Mono<ResponseData> searchData(String keyword, String sort, int page, int pageSize) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(url)
                        .path("/v2/search/blog")
                        .queryParam("query", keyword)
                        .queryParam("sort", sort)
                        .queryParam("page", page)
                        .queryParam("size", pageSize)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + apiKey)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(ResponseData.class);
                    } else if(response.statusCode().is5xxServerError()) {
                        return naverBlogSearchApi.searchData(keyword, sort, page, pageSize);
                    } else {
                        ResponseData responseData = new ResponseData();
                        Meta meta = new Meta();

                        meta.setStatus(response.statusCode().toString());
                        meta.setMessage("오류");
                        responseData.setMeta(meta);
                        return Mono.just(responseData);
                    }
                });
    }
}
