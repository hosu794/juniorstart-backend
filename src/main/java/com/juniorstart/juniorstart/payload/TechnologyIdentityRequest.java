package com.juniorstart.juniorstart.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnologyIdentityRequest {

    private Long projectId;
    private Long technologyId;
}
