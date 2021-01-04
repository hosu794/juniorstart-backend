package com.juniorstart.juniorstart.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CodeReviewSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String code;

    @Builder.Default
    @ElementCollection
    private Map<String,String> comments = new HashMap<>();

    @Builder.Default
    @ElementCollection
    private List<String> codeReviewTags = new ArrayList<>();

    @Min(0) @Max(10)
    private byte rate;

    private long numberOfRatings;

    @ManyToOne
    private User user;

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
    @Builder
    @AllArgsConstructor
    public static class CodeReviewSectionDto {
        private String code;
        private Map<String,String> comments;
        private List<String> codeReviewTags;
        private byte rate;
        private long numberOfRatings;
        private User user;
    }



}
