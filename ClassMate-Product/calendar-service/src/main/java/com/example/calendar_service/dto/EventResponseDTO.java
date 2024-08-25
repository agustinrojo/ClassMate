package com.example.calendar_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
}
