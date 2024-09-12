package com.example.calendar_service.service.impl;

import com.example.calendar_service.dto.EventRequestDTO;
import com.example.calendar_service.dto.EventUpdateDTO;

import com.example.calendar_service.repository.IEventRepository;
import com.example.calendar_service.service.IGoogleCalendarService;
import com.example.calendar_service.service.IGoogleOAuth2Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GoogleCalendarServiceImpl implements IGoogleCalendarService {

    private static final String APPLICATION_NAME = "ClassMate";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final IGoogleOAuth2Service googleOAuth2Service;
    private final IEventRepository eventRepository;

    public GoogleCalendarServiceImpl(IGoogleOAuth2Service googleOAuth2Service, IEventRepository eventRepository) {
        this.googleOAuth2Service = googleOAuth2Service;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public void createEvent(String calendarId, EventRequestDTO eventRequestDTO) throws IOException {
        Long userId = eventRequestDTO.getUserId();
        Calendar calendar = getCalendar(userId);
        String calendarTimeZone = getCalendarTimeZone(calendar, calendarId);

        Event event = new Event()
                .setSummary(eventRequestDTO.getTitle())
                .setDescription(eventRequestDTO.getDescription());

        LocalDateTime startDateTime = eventRequestDTO.getStartDate().atStartOfDay().plusDays(1).minusHours(21);
        LocalDateTime endDateTime = eventRequestDTO.getEndDate().atStartOfDay().plusDays(1).minusHours(21);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String startDateTimeFormatted = startDateTime.format(formatter);
        String endDateTimeFormatted = endDateTime.format(formatter);

        System.out.println("start: " + startDateTimeFormatted);
        System.out.println("end: " + endDateTimeFormatted);

        EventDateTime start = new EventDateTime()
                .setDateTime(new DateTime(startDateTimeFormatted))
                .setTimeZone(calendarTimeZone);
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(new DateTime(endDateTimeFormatted))
                .setTimeZone(calendarTimeZone);
        event.setEnd(end);

        Event newGoogleEvent = calendar.events().insert(calendarId, event).execute();

        com.example.calendar_service.entity.calendar.Event savedEventEntity = eventRepository.findById(eventRequestDTO.getId())
                .orElseThrow();

        savedEventEntity.setGoogleId(newGoogleEvent.getId());

        eventRepository.save(savedEventEntity);
    }


    @Override
    public void updateEvent(String calendarId, String googleEventId, EventUpdateDTO updatedEvent, Long userId) throws IOException {
        Calendar calendar = getCalendar(userId);
        String calendarTimeZone = getCalendarTimeZone(calendar, calendarId);
        Event event = calendar.events().get(calendarId, googleEventId).execute();

        LocalDateTime startDateTime = updatedEvent.getStartDate().atStartOfDay().plusDays(1).minusHours(21);
        LocalDateTime endDateTime = updatedEvent.getEndDate().atStartOfDay().plusDays(1).minusHours(21);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String startDateTimeFormatted = startDateTime.format(formatter);
        String endDateTimeFormatted = endDateTime.format(formatter);

        event.setSummary(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());

        EventDateTime start = new EventDateTime()
                .setDateTime(new DateTime(startDateTimeFormatted))
                .setTimeZone(calendarTimeZone);

        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(new DateTime(endDateTimeFormatted))
                .setTimeZone(calendarTimeZone);

        event.setEnd(end);
        calendar.events().update(calendarId, googleEventId, event).execute();
    }

    @Override
    public void deleteEvent(String calendarId, String googleEventId, Long userId) throws IOException {
        Calendar calendar = getCalendar(userId);
        calendar.events().delete(calendarId, googleEventId).execute();
    }

    private Calendar getCalendar(Long userId) {
        String accessToken = googleOAuth2Service.getValidAccessToken(userId);
        try {
            GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
            return new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    @Transactional
    public void uploadAllEvents(Long userId, String calendarId)  {
        System.out.println("ejecutando bulk upload");
        Calendar calendar = getCalendar(userId);
        String calendarTimeZone = getCalendarTimeZone(calendar, calendarId);
        List<com.example.calendar_service.entity.calendar.Event> events = eventRepository.findEventsByUserId(userId).stream()
                .filter((com.example.calendar_service.entity.calendar.Event event) -> event.getGoogleId() == null)
                .toList();

        if(!events.isEmpty()){
            for (com.example.calendar_service.entity.calendar.Event e : events){
                Event googleEvent = mapEventToGoogleEvent(e, calendarTimeZone);

                try {
                    Event insertedGoogleEvent = calendar.events().insert(calendarId, googleEvent).execute();
                    e.setGoogleId(insertedGoogleEvent.getId());
                    eventRepository.save(e);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private String getCalendarTimeZone(Calendar calendar, String calendarId) {
        try {
            return calendar.calendars().get(calendarId).execute().getTimeZone();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private Event mapEventToGoogleEvent(com.example.calendar_service.entity.calendar.Event event, String calendarTimeZone){
        Event googleEvent = new Event()
                .setSummary(event.getTitle())
                .setDescription(event.getDescription());

        LocalDateTime startDateTime = event.getStartDate().atStartOfDay().plusDays(1).minusHours(21);
        LocalDateTime endDateTime = event.getEndDate().atStartOfDay().plusDays(1).minusHours(21);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String startDateTimeFormatted = startDateTime.format(formatter);
        String endDateTimeFormatted = endDateTime.format(formatter);

        EventDateTime start = new EventDateTime()
                .setDateTime(new DateTime(startDateTimeFormatted))
                .setTimeZone(calendarTimeZone);
        googleEvent.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(new DateTime(endDateTimeFormatted))
                .setTimeZone(calendarTimeZone);
        googleEvent.setEnd(end);

        return googleEvent;
    }

}
