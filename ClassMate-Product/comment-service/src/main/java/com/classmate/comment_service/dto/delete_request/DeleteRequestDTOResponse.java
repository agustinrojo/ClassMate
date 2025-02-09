package com.classmate.comment_service.dto.delete_request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteRequestDTOResponse {
    Long reporterId;
    String reporterNickname;
    String message;
}
