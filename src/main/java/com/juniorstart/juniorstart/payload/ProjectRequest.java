package com.juniorstart.juniorstart.payload;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    @Min(3)
    @Max(128)
    private String name;
    @Max(128)
    @NotNull
    @Min(3)
    private String title;

    @NotNull
    @Min(6)
    private String description;

    @Min(10)
    private String body;
    @Max(8)
    private long numberOfSeats;

    @NotNull
    private String repository;

    @NotNull
    private boolean recruiting;

}
