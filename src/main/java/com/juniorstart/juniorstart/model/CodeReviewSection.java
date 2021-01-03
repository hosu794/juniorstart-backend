package com.juniorstart.juniorstart.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CodeReviewSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String code;

    @ElementCollection
    private Map<String,String> comments = new HashMap<>();

    @ElementCollection
    private HashSet<String> codeReviewTags = new HashSet<>();

    @Min(0) @Max(10)
    private short rate;

    private long numberOfRatings;

    @ManyToOne
    private User user;

/*
    public void addUserToCodeReviewSection(User user) {
        this.user.dr(user);
        user.getCodeReviewSections().add(this);
    }
 */

    public CodeReviewSectionDto toCodeReviewSectionDto() {
        return new CodeReviewSectionDto(
                this.code,
                this.comments,
                this.codeReviewTags,
                this.rate,
                this.numberOfRatings,
                this.user
        );
    }

    @Data
    @AllArgsConstructor
    public static class CodeReviewSectionDto {
        private String code;
        private Map<String,String> comments;
        private HashSet<String> codeReviewTags;
        private short rate;
        private long numberOfRatings;
        private User user;
    }



}
