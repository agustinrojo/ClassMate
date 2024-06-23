package com.classmate.post_service.dto.filedtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
    private Long id;
    private String originalFilename;
    private String contentType;
    private Long size;
}
