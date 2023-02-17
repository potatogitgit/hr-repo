package org.example.service;

import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;

public interface CalendarService {
    public Event setEventSummary();

    public ConferenceData setConferenceData();

    public EventDateTime setStartAndEnd();

    public String[] setEventRecurrence();

    public EventAttendee[] setEventAttendees();

    public void createMeetEvent();
}
