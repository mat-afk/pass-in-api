package rocketseat.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.attendee.exceptions.AttendeeAlreadyRegisteredException;
import rocketseat.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import rocketseat.com.passin.domain.checkin.CheckIn;
import rocketseat.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import rocketseat.com.passin.dto.attendee.AttendeeDetailsDTO;
import rocketseat.com.passin.dto.attendee.AttendeesListResponseDTO;
import rocketseat.com.passin.dto.attendee.AttendeeBadgeDTO;
import rocketseat.com.passin.repositories.AttendeeRepository;
import rocketseat.com.passin.repositories.CheckInRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final CheckInRepository checkInRepository;

    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        return this.attendeeRepository.findAllByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendees(String eventId) {
        List<Attendee> attendeeList = this.attendeeRepository.findAllByEventId(eventId);

        List<AttendeeDetailsDTO> attendeeDetailsDTOList = attendeeList.stream().map(attendee -> {
            // find out if attendee has checkedIn
            Optional<CheckIn> checkIn = this.checkInRepository.findByAttendeeId(attendee.getId());
            LocalDateTime checkedInAt = checkIn.map(CheckIn::getCreatedAt).orElse(null);

            return new AttendeeDetailsDTO(
                    attendee.getId(),
                    attendee.getName(),
                    attendee.getEmail(),
                    attendee.getCreatedAt(),
                    checkedInAt
            );
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsDTOList);
    }

    public void registerAttendee(Attendee newAttendee) {
        this.attendeeRepository.save(newAttendee);
    }

    public void verifyAttendeeSubscription(String email, String eventId) {
        Optional<Attendee> registeredAttendee = this.attendeeRepository.findByEventIdAndEmail(eventId, email);

        if (registeredAttendee.isPresent())
            throw new AttendeeAlreadyRegisteredException("Attendee is already registered");
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(
            String attendeeId,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Attendee attendee = this.attendeeRepository.findById(attendeeId).orElseThrow(() ->
                new AttendeeNotFoundException("Attendee not found with id: " + attendeeId));

        var uri = uriComponentsBuilder.path("/attendees/{id}/check-in").buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDTO attendeeBadgeDTO = new AttendeeBadgeDTO(
                attendee.getName(),
                attendee.getEmail(),
                uri,
                attendee.getEvent().getId()
        );

        return new AttendeeBadgeResponseDTO(attendeeBadgeDTO);
    }
}
