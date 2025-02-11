package com.classmate.forum_service.dto.delete_request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteRequestDTOResponse {
    private String message;
    private Long reporterId;
    private String reporterNickname;
}
