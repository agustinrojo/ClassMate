package com.example.Security.dto.google_sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SyncDTO {
    private Long userId;
    private boolean isSynced;
}
