package rocketseat.com.passin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rocketseat.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import rocketseat.com.passin.services.AttendeeService;
import rocketseat.com.passin.services.CheckInService;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {

    private final AttendeeService attendeeService;

    @GetMapping("/{id}/badge")
    public ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(
            @PathVariable String id,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        AttendeeBadgeResponseDTO response = this.attendeeService.getAttendeeBadge(id, uriComponentsBuilder);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/check-in")
    public ResponseEntity<String> postCheckIn(@PathVariable String id, UriComponentsBuilder uriComponentsBuilder) {
        this.attendeeService.checkIn(id);

        var uri = uriComponentsBuilder.path("/attendees/{id}/badge").buildAndExpand(id).toUri();

        return ResponseEntity.created(uri).build();
    }
}
