package com.components.campusconnectbackend.service;

import com.components.campusconnectbackend.dto.NewsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface NewsService {

    // Create
    NewsDTO createNews(NewsDTO newsDTO);

    // Create with file upload
    NewsDTO createNewsWithPhoto(String title, String body, MultipartFile photo) throws IOException;

    // Read
    List<NewsDTO> getAllNews();
    List<NewsDTO> getLatestNews(int count);
    Optional<NewsDTO> getNewsById(Integer id);
    List<NewsDTO> searchNewsByTitle(String title);
    List<NewsDTO> getNewsByDepartment(Integer departmentId);
    List<NewsDTO> getNewsBetweenDates(Date startDate, Date endDate);
    byte[] getNewsPhoto(Integer id);

    // Update
    NewsDTO updateNews(Integer id, NewsDTO newsDTO);
    NewsDTO updateNewsWithPhoto(Integer id, String title, String body, MultipartFile photo) throws IOException;

    // Delete
    void deleteNews(Integer id);

    // Check if exists
    boolean existsById(Integer id);
}