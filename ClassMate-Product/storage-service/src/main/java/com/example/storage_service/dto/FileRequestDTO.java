package com.example.storage_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileRequestDTO {
    private byte[] bytes;
    private String originalFilename;
    private String contentType;
}
