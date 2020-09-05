package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.ProjectComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectCommentRepository extends JpaRepository<ProjectComment, Long> {

    Page<ProjectComment> findByUserPrivateId(UUID userId, Pageable pageable);
    Page<ProjectComment> findByProjectId(Long projectId, Pageable pageable);
    Page<ProjectComment> findByCreatedBy(Long userId, Pageable pageable);

}
