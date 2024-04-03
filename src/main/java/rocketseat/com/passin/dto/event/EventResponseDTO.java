package rocketseat.com.passin.dto.event;

import rocketseat.com.passin.domain.event.Event;

public class EventResponseDTO {

    EventDetailsDTO eventDetailsDTO;

    public EventResponseDTO(Event event, Integer numberOfAttendees) {
        this.eventDetailsDTO = new EventDetailsDTO(
                event.getId(),
                event.getTitle(),
                event.getDetails(),
                event.getSlug(),
                event.getMaximumAttendees(),
                numberOfAttendees
        );
    }
}
