package com.juniorstart.juniorstart.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    private String name;
    private String title;
    private String description;
    private String body;
    private long numberOfSeats;
    private String repository;
    private boolean recruiting;

}
