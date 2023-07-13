package com.example.blogsearchapi.service;

import com.example.blogsearchapi.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class NaverBlogSearchApi implements SearchApi{
    private final WebClient webClient;

    @Value(value = "${naver.rest.client-id}")
    private String naverClientId;

    @Value(value = "${naver.rest.client-secret}")
    private String naverClientSecret;

    @Value(value = "${naver.rest.url}")
    private String naverUrl;

    @Autowired
    public NaverBlogSearchApi() {
        this.webClient = WebClient.create();
    }

    @Override
    public Mono<ResponseData> searchData(String keyword, String sort, int page, int pageSize) {
        String naverSort = ("recency".equals(sort)) ? "date" : "sim";
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(naverUrl)
                        .path("/v1/search/blog.json")
                        .queryParam("query", keyword)
                        .queryParam("display", pageSize)
                        .queryParam("start", page)
                        .queryParam("sort", naverSort)
                        .build())
                .header("X-Naver-Client-Id", naverClientId)
                .header("X-Naver-Client-Secret", naverClientSecret)
                .retrieve()
                .bodyToMono(ResponseDataNaver.class)
                .flatMap(responseDataNaver -> {
                    ResponseData responseData = mapSearchResultToResponseData(responseDataNaver);
                    return Mono.just(responseData);
                });
    }

    private ResponseData mapSearchResultToResponseData(ResponseDataNaver responseDataNaver) {
        int total = responseDataNaver.getTotal();

        ResponseData responseData = new ResponseData();
        Meta meta = new Meta();
        meta.setStatus("200 OK");
        meta.setMessage("성공");
        meta.setTotalCount(total);
        meta.setSource("Naver");
        responseData.setMeta(meta);

        List<Document> documents = new ArrayList<>();

        for(Item item : responseDataNaver.getItems()) {
            Document document = new Document();
            document.setBlogName(item.getBloggerName());
            document.setContents(item.getDescription());
            document.setTitle(item.getTitle());
            document.setUrl(item.getLink());
            document.setDatetime(item.getPostDate());
            documents.add(document);
        }

        responseData.setDocuments(documents);

        return responseData;
    }
}
