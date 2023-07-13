package com.example.blogsearchapi.service;

import com.example.blogsearchapi.entity.PopularSearch;
import com.example.blogsearchapi.repository.PopularSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PopularSearchService {
    private final PopularSearchRepository popularSearchRepository;

    @Autowired
    public PopularSearchService(PopularSearchRepository popularSearchRepository) {
        this.popularSearchRepository = popularSearchRepository;
    }



    public List<PopularSearch> getPopularSearches() {
        return popularSearchRepository.findTop10ByOrderBySearchCountDesc();
    }

    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            backoff = @Backoff(delay = 1000)
    )
    @Transactional
    public void updatePopularSearches(String keyword) {
        PopularSearch popularSearch = popularSearchRepository.findByKeyword(keyword);
        if (popularSearch == null) {
            popularSearch = new PopularSearch(keyword, 1);
        } else {
            popularSearch.incrementSearchCount();
        }
        popularSearchRepository.save(popularSearch);
    }
}
