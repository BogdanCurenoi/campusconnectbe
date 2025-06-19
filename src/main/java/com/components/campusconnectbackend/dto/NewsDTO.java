package com.components.campusconnectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
    private Integer id;
    private String title;
    private String body;
    private byte[] photo;
    private Integer departmentId;
    private String departmentName;

    // Constructor without photo for cases where you don't want to transfer the image
    public NewsDTO(Integer id, String title, String body, Integer departmentId, String departmentName) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }
}