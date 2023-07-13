package com.example.blogsearchapi.repository;

import com.example.blogsearchapi.entity.PopularSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PopularSearchRepository extends JpaRepository<PopularSearch, Long> {

    PopularSearch findByKeyword(String keyword);

    List<PopularSearch> findTop10ByOrderBySearchCountDesc();
}
