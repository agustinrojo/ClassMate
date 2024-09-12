package com.example.calendar_service.mapper;

import com.example.calendar_service.dto.EventRequestDTO;
import com.example.calendar_service.dto.EventResponseDTO;
import com.example.calendar_service.entity.calendar.Event;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface IEventMapper {

    Event mapToEvent(EventRequestDTO eventRequestDTO);

    EventResponseDTO mapToResponseEventDTO(Event eventEntity);
}
