package com.example.blogsearchapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Meta {

    @JsonProperty("is_end")
    private boolean isEnd;

    @JsonProperty("pageable_count")
    private int pageableCount;

    @JsonProperty("total_count")
    private int totalCount;

    @JsonProperty("status")
    private String status = "200 OK";

    @JsonProperty("message")
    private String message = "성공";

    @JsonProperty("source")
    private String source = "Kakao";
}
