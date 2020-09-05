package com.juniorstart.juniorstart.payload;

import com.juniorstart.juniorstart.model.TechnologyType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateTechnologyRequest {

    private String description;
    private String title;
    private TechnologyType technologyType;
    private Long technologyId;

}
