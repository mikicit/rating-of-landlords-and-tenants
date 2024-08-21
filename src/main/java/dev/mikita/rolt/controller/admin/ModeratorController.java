package dev.mikita.rolt.controller.admin;

import dev.mikita.rolt.dto.request.ModeratorCreateRequestDTO;
import dev.mikita.rolt.dto.response.ModeratorResponseDTO;
import dev.mikita.rolt.service.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@RequestMapping("/api/admin/moderators")
public class ModeratorController {
    private final ModeratorService moderatorService;

    @Autowired
    public ModeratorController(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createModerator(@RequestBody ModeratorCreateRequestDTO dto) {
        ModeratorResponseDTO moderatorResponseDTO = moderatorService.add(dto);
        return ResponseEntity.created(URI.create("/api/moderators/" + moderatorResponseDTO.id())).build();
    }
}
