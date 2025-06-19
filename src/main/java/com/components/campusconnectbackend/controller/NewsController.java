package com.components.campusconnectbackend.controller;

import com.components.campusconnectbackend.dto.NewsDTO;
import com.components.campusconnectbackend.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*") // For development - restrict this in production
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // Create a new news item
    @PostMapping
    public ResponseEntity<NewsDTO> createNews(@RequestBody NewsDTO newsDTO) {
        NewsDTO createdNews = newsService.createNews(newsDTO);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }

    // Create a new news item with photo
    @PostMapping("/with-photo")
    public ResponseEntity<NewsDTO> createNewsWithPhoto(
            @RequestParam("title") String title,
            @RequestParam("body") String body,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {
        try {
            NewsDTO createdNews = newsService.createNewsWithPhoto(title, body, photo);
            return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Get all news
    @GetMapping
    public ResponseEntity<List<NewsDTO>> getAllNews() {
        List<NewsDTO> news = newsService.getAllNews();
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    // Get latest news
    @GetMapping("/latest")
    public ResponseEntity<List<NewsDTO>> getLatestNews(@RequestParam(defaultValue = "5") int count) {
        List<NewsDTO> news = newsService.getLatestNews(count);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    // Get news by ID
    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable Integer id) {
        return newsService.getNewsById(id)
                .map(newsDTO -> new ResponseEntity<>(newsDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get news photo
    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getNewsPhoto(@PathVariable Integer id) {
        byte[] photo = newsService.getNewsPhoto(id);
        if (photo != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust based on your photo type
            return new ResponseEntity<>(photo, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Search news by title
    @GetMapping("/search")
    public ResponseEntity<List<NewsDTO>> searchNewsByTitle(@RequestParam String title) {
        List<NewsDTO> news = newsService.searchNewsByTitle(title);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    // Get news by department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<NewsDTO>> getNewsByDepartment(@PathVariable Integer departmentId) {
        List<NewsDTO> news = newsService.getNewsByDepartment(departmentId);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    // Get news between dates
    @GetMapping("/date-range")
    public ResponseEntity<List<NewsDTO>> getNewsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        List<NewsDTO> news = newsService.getNewsBetweenDates(startDate, endDate);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    // Update news
    @PutMapping("/{id}")
    public ResponseEntity<NewsDTO> updateNews(@PathVariable Integer id, @RequestBody NewsDTO newsDTO) {
        try {
            NewsDTO updatedNews = newsService.updateNews(id, newsDTO);
            return new ResponseEntity<>(updatedNews, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update news with photo
    @PutMapping("/{id}/with-photo")
    public ResponseEntity<NewsDTO> updateNewsWithPhoto(
            @PathVariable Integer id,
            @RequestParam("title") String title,
            @RequestParam("body") String body,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {
        try {
            NewsDTO updatedNews = newsService.updateNewsWithPhoto(id, title, body, photo);
            return new ResponseEntity<>(updatedNews, HttpStatus.OK);
        } catch (RuntimeException | IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete news
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Integer id) {
        if (newsService.existsById(id)) {
            newsService.deleteNews(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}