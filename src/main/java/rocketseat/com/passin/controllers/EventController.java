package rocketseat.com.passin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rocketseat.com.passin.dto.attendee.AttendeeIdDTO;
import rocketseat.com.passin.dto.attendee.AttendeeRequestDTO;
import rocketseat.com.passin.dto.attendee.AttendeesListResponseDTO;
import rocketseat.com.passin.dto.event.EventIdDTO;
import rocketseat.com.passin.dto.event.EventRequestDTO;
import rocketseat.com.passin.dto.event.EventResponseDTO;
import rocketseat.com.passin.services.AttendeeService;
import rocketseat.com.passin.services.EventService;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id) {
        EventResponseDTO eventResponseDTO = this.eventService.getEventDetails(id);

        return ResponseEntity.ok(eventResponseDTO);
    }

    @PostMapping("/")
    public ResponseEntity<EventIdDTO> postEvent(
            @RequestBody EventRequestDTO body,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        EventIdDTO eventIdDTO = eventService.createEvent(body);

        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();

        return ResponseEntity.created(uri).body(eventIdDTO);
    }

    @GetMapping("/{id}/attendees")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String id) {
        AttendeesListResponseDTO attendeesListResponseDTO = this.attendeeService.getEventsAttendees(id);
        return ResponseEntity.ok(attendeesListResponseDTO);
    }

    @PostMapping("/{id}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(
            @PathVariable String id,
            @RequestBody AttendeeRequestDTO body,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        AttendeeIdDTO attendeeIdDTO = eventService.registerAttendeeOnEvent(body, id);

        var uri = uriComponentsBuilder.path("/attendees/{id}/badge")
                .buildAndExpand(attendeeIdDTO.attendeeId()).toUri();

        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }
}
