package com.classmate.post_service.dto.filedtos;

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
