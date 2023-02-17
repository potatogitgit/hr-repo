package org.example.controller;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import org.example.startup.GoogleCalendarStartup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class CalendarController {

    GoogleCalendarStartup googleCalendarStartup = GoogleCalendarStartup.getInstance();

    Calendar service = googleCalendarStartup.getCalendarService();

    @RequestMapping({"home", "/", ""})
    public String showIndex(){
        return "index.html";
    }

    @RequestMapping("/login")
    public void authenticateGoogle() throws GeneralSecurityException, IOException {
    }

    @RequestMapping("/events")
    public String getEvents(Model model) throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
        model.addAttribute("events", items);
        return "events.html";
    }

    @RequestMapping("/callHR")
    public String callHR(Model model) throws IOException {
        java.util.Calendar startDate = java.util.Calendar.getInstance();
        java.util.Calendar endDate = java.util.Calendar.getInstance();
        startDate.set(2023, 1, 18, 10, 0);
        endDate.set(2023, 1, 18, 10, 30);
        /*
        Event summary, location and details
         */
        Event event = new Event()
                .setSummary("Test Event")
                .setLocation("Virtual")
                .setDescription("A chance to hear more about Google's developer products.");

        /*
        Create a meet solution
         */

        // Conference Solution Key to set what kind of conference
        ConferenceSolutionKey conferenceSKey = new ConferenceSolutionKey();
        conferenceSKey.setType("hangoutsMeet"); // Non-G suite user

        // Conference Request to create the meet request
        CreateConferenceRequest createConferenceReq = new CreateConferenceRequest();
        createConferenceReq.setRequestId("testEvent123"); // ID generated by you
        createConferenceReq.setConferenceSolutionKey(conferenceSKey);

        // Conference Data - Full Data used to attach to an event
        ConferenceData conferenceData = new ConferenceData();
        conferenceData.setCreateRequest(createConferenceReq);
        event.setConferenceData(conferenceData); // attach the meeting to your event


        /*
        Event start time
         */
        DateTime startDateTime = new DateTime(startDate.getTime());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Manila");
        event.setStart(start);

        /*
        Event end time
         */
        DateTime endDateTime = new DateTime(endDate.getTime());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Manila");
        event.setEnd(end);

        /*
        Event recurrence
         */
        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));

        /*
        Event attendees
         */
        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail("wil.bacante@telusinternational.com")
        };
        event.setAttendees(Arrays.asList(attendees));

        /*
        Event reminder
         */
        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        /*
        Set which calendar to put.
        Then, add the event to the user by invoking Google API service
         */
        String calendarId = "primary";

//        try {
//            /* Code changes - START */
//
//            // .setConferenceDataVersion(1) enables the creation of new meetings
//            event = service.events().insert(calendarId, event).setConferenceDataVersion(1).execute();
//
//            /* Code changes - END */
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            event = service.events().insert(calendarId, event).setConferenceDataVersion(1).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Event created: %s\n", event.getHtmlLink());
        model.addAttribute("meetLink", "https://meet.google.com/" + event.getConferenceData().getConferenceId());
        model.addAttribute("eventLink", event.getHtmlLink());
        return "eventCreated";
    }

    public CalendarController() throws GeneralSecurityException, IOException {
    }
}
