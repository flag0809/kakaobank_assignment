package com.example.blogsearchapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Document {
    @JsonProperty("blogname")
    private String blogName;

    @JsonProperty("contents")
    private String contents;

    @JsonProperty("datetime")
    private String datetime;

    @JsonProperty("thumbnail")
    private String thumbnail;

    @JsonProperty("title")
    private String title;

    @JsonProperty("url")
    private String url;
}
