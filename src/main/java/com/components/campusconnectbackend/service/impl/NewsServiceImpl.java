package com.components.campusconnectbackend.service.impl;

import com.components.campusconnectbackend.domain.News;
import com.components.campusconnectbackend.dto.NewsDTO;
import com.components.campusconnectbackend.repository.NewsRepository;
import com.components.campusconnectbackend.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public NewsDTO createNews(NewsDTO newsDTO) {
        News news = convertToEntity(newsDTO);
        News savedNews = newsRepository.save(news);
        return convertToDTO(savedNews);
    }

    @Override
    public NewsDTO createNewsWithPhoto(String title, String body, MultipartFile photo) throws IOException {
        News news = new News();
        news.setTitle(title);
        news.setBody(body);

        if (photo != null && !photo.isEmpty()) {
            news.setPhoto(photo.getBytes());
        }

        News savedNews = newsRepository.save(news);
        return convertToDTO(savedNews);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDTO> getAllNews() {
        return newsRepository.findAllByOrderByIdDesc().stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDTO> getLatestNews(int count) {
        return newsRepository.findAllByOrderByIdDesc().stream()
                .limit(count)
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NewsDTO> getNewsById(Integer id) {
        return newsRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDTO> searchNewsByTitle(String title) {
        return newsRepository.findByTitleContaining(title).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDTO> getNewsByDepartment(Integer departmentId) {
        return newsRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToDTOWithoutPhoto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsDTO> getNewsBetweenDates(Date startDate, Date endDate) {
        // Since we don't have createdDate, this will return all news
        // You can modify this based on your needs
        return getAllNews();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getNewsPhoto(Integer id) {
        return newsRepository.findById(id)
                .map(News::getPhoto)
                .orElse(null);
    }

    @Override
    public NewsDTO updateNews(Integer id, NewsDTO newsDTO) {
        if (newsRepository.existsById(id)) {
            News news = convertToEntity(newsDTO);
            news.setId(id); // Ensure the correct ID is set

            // Get existing news to preserve or clear photo based on DTO
            newsRepository.findById(id).ifPresent(existingNews -> {
                // If photo is explicitly null in DTO, remove it (user deleted it)
                if (newsDTO.getPhoto() == null) {
                    news.setPhoto(null);
                } else if (newsDTO.getPhoto().length == 0) {
                    // Empty byte array also means remove photo
                    news.setPhoto(null);
                } else {
                    // Photo has content, use it
                    news.setPhoto(newsDTO.getPhoto());
                }
            });

            News updatedNews = newsRepository.save(news);
            return convertToDTO(updatedNews);
        } else {
            throw new RuntimeException("News with ID: " + id + " not found");
        }
    }

    @Override
    public NewsDTO updateNewsWithPhoto(Integer id, String title, String body, MultipartFile photo) throws IOException {
        if (newsRepository.existsById(id)) {
            News news = newsRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("News with ID: " + id + " not found"));

            news.setTitle(title);
            news.setBody(body);

            if (photo != null && !photo.isEmpty()) {
                news.setPhoto(photo.getBytes());
            }

            News updatedNews = newsRepository.save(news);
            return convertToDTO(updatedNews);
        } else {
            throw new RuntimeException("News with ID: " + id + " not found");
        }
    }

    @Override
    public void deleteNews(Integer id) {
        newsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return newsRepository.existsById(id);
    }

    // Helper methods to convert between entity and DTO
    private NewsDTO convertToDTO(News news) {
        String departmentName = null;
        if (news.getDepartment() != null) {
            departmentName = news.getDepartment().getName();
        }

        return new NewsDTO(
                news.getId(),
                news.getTitle(),
                news.getBody(),
                news.getPhoto(),
                news.getDepartmentId(),
                departmentName
        );
    }

    private NewsDTO convertToDTOWithoutPhoto(News news) {
        String departmentName = null;
        if (news.getDepartment() != null) {
            departmentName = news.getDepartment().getName();
        }

        return new NewsDTO(
                news.getId(),
                news.getTitle(),
                news.getBody(),
                news.getDepartmentId(),
                departmentName
        );
    }

    private News convertToEntity(NewsDTO newsDTO) {
        News news = new News();
        news.setId(newsDTO.getId());
        news.setTitle(newsDTO.getTitle());
        news.setBody(newsDTO.getBody());
        news.setDepartmentId(newsDTO.getDepartmentId());

        // Handle photo data exactly like PersonServiceImpl does
        if (newsDTO.getPhoto() != null) {
            news.setPhoto(newsDTO.getPhoto());
        }

        return news;
    }
}