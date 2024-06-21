package com.example.storage_service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostFileDeletionDTO {
    private List<Long> attachmentIdsToDelete;
}
