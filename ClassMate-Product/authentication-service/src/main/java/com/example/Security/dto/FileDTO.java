package com.example.Security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private Long photoId;
    private String originalFilename;
    private String contentType;
    private Long size;
}
