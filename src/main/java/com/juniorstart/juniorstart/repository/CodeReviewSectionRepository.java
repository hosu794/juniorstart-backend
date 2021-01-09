package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.CodeReviewSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

@Repository
public interface CodeReviewSectionRepository extends JpaRepository<CodeReviewSection, Long> {
    Page<CodeReviewSection> findByCodeReviewTagsIn(List<String> codeReviewTags, Pageable pageable);
}
