package com.classmate.post_service.dto.comment_count_event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCountEvent {
    private Long commentCount;
    private Long postId;
}
