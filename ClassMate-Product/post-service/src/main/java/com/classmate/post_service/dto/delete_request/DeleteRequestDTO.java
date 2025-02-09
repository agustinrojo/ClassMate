package com.classmate.post_service.dto.delete_request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteRequestDTO {
    private Long reporterId;
    private String message;
}

