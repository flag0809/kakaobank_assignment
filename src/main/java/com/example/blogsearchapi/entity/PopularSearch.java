package com.example.blogsearchapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class PopularSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;

    private int searchCount;

    @Version
    private int version;

    public PopularSearch(String keyword, int searchCount) {
        this.keyword = keyword;
        this.searchCount = searchCount;
    }

    public PopularSearch() {

    }

    public void incrementSearchCount() {
        searchCount++;
    }
}
