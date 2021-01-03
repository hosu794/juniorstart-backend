package com.juniorstart.juniorstart.repository;

import com.juniorstart.juniorstart.model.CodeReviewSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface CodeReviewRepository extends JpaRepository<CodeReviewSection, Long> {
    Page<CodeReviewSection> findByCodeReviewTagsIn(HashSet<String> codeReviewTags, Pageable pageable);
}
