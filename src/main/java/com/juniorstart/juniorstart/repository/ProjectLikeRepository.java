package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.ProjectLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectLikeRepository extends JpaRepository<ProjectLike, Long> {

    long countByProjectId(long projectId);

    @Query("SELECT v FROM ProjectLike v where v.user.id = :userId and v.project.id in :projectId")
    ProjectLike findByUserIdAndProjectId(@Param("userId") UUID userId, @Param("projectId") Long projectId);
}
