package com.juniorstart.juniorstart.payload;

import com.juniorstart.juniorstart.model.TechnologyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyRequest {

    private String title;
    private TechnologyType technologyType;
    private String description;

}
