package org.example.server.controller;

import jakarta.validation.Valid;
import org.example.server.dto.request.ProjectDtoRequest;
import org.example.server.dto.response.ProjectDtoResponse;
import org.example.server.mapper.ProjectMapper;
import org.example.server.model.Project;
import org.example.server.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProjectDtoResponse> createProject(@Valid @RequestBody ProjectDtoRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();

        ProjectDtoResponse response = projectService.createProject(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDtoResponse> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectDtoRequest request) {

        ProjectDtoResponse response = projectService.updateProject(projectId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/users/{userId}")
    public ResponseEntity<String> addUserToProject(@PathVariable Long projectId, @PathVariable Long userId) {
        projectService.addUserToProject(projectId, userId);
        return ResponseEntity.ok("Utilisateur ajouté avec succès au projet.");
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDtoResponse> getProjectById(@PathVariable Long projectId) {
        ProjectDtoResponse project = projectService.getProjectById(projectId);

        return ResponseEntity.ok(project);
    }
}
