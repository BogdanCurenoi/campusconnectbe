package com.components.campusconnectbackend.repository;

import com.components.campusconnectbackend.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

    List<News> findByTitleContaining(String title);

    List<News> findByDepartmentId(Integer departmentId);

    // Since we don't have createdDate, we'll order by ID (newest first)
    List<News> findAllByOrderByIdDesc();
}