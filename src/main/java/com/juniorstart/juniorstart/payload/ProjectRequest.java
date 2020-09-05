package com.juniorstart.juniorstart.payload;

import com.juniorstart.juniorstart.model.Technology;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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
