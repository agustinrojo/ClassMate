package com.classmate.comment_service.dto.filedtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDeletionDTO {
    private List<Long> attachmentIdsToDelete;
}
