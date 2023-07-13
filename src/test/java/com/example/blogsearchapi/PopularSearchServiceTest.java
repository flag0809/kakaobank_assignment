package com.example.blogsearchapi;

import com.example.blogsearchapi.entity.PopularSearch;
import com.example.blogsearchapi.repository.PopularSearchRepository;
import com.example.blogsearchapi.service.PopularSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class PopularSearchServiceTest {

    @Autowired
    private PopularSearchService popularSearchService;

    @MockBean
    private PopularSearchRepository popularSearchRepository;

    @Test
    void testGetPopularSearches() {

        List<PopularSearch> expectedSearches = Arrays.asList(new PopularSearch("keyword1", 10),
                new PopularSearch("keyword2", 5));
        when(popularSearchRepository.findTop10ByOrderBySearchCountDesc()).thenReturn(expectedSearches);

        List<PopularSearch> actualSearches = popularSearchService.getPopularSearches();

        assertEquals(expectedSearches, actualSearches);
        verify(popularSearchRepository, times(1)).findTop10ByOrderBySearchCountDesc();
    }

    @Test
    void testUpdatePopularSearches_whenKeywordExists() {

        String keyword = "test";
        PopularSearch popularSearch = new PopularSearch(keyword, 5);

        when(popularSearchRepository.findByKeyword(keyword)).thenReturn(popularSearch);

        popularSearchService.updatePopularSearches(keyword);

        verify(popularSearchRepository, times(1)).findByKeyword(keyword);
        verify(popularSearchRepository, times(1)).save(popularSearch);
        assertEquals(6, popularSearch.getSearchCount());  // Assuming getSearchCount() is a method in PopularSearch
    }

    @Test
    void testUpdatePopularSearches_whenKeywordDoesNotExist() {

        String keyword = "test";

        when(popularSearchRepository.findByKeyword(keyword)).thenReturn(null);

        popularSearchService.updatePopularSearches(keyword);

        verify(popularSearchRepository, times(1)).findByKeyword(keyword);

        verify(popularSearchRepository, times(1)).save(any(PopularSearch.class));
    }
}
