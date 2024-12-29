package org.example.server.repository;

import org.example.server.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.columns WHERE p.id = :projectId")
    Optional<Project> findByIdWithColumns(@Param("projectId") Long projectId);

    @Query("SELECT p FROM Project p JOIN p.userProjects up WHERE up.user.id = :userId")
    List<Project> findAllByUserId(@Param("userId") Long userId);

}

