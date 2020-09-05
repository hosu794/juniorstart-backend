package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByTitle(String title, Pageable pageable);

    Optional<Project> findByName(String name);

    Optional<Project> findByRepository(String repository);

    Page<Project> findByIdIn(List<Long> projectsIds, Pageable pageable);

}
