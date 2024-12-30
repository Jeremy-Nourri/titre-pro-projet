package org.example.server.repository;

import org.example.server.model.Project;
import org.example.server.model.User;
import org.example.server.model.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
    Optional<UserProject> findByUserAndProject(User user, Project project);

}
