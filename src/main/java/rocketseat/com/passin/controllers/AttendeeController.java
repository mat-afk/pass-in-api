package rocketseat.com.passin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import rocketseat.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import rocketseat.com.passin.services.AttendeeService;

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
}
