package com.juniorstart.juniorstart.payload;

import com.juniorstart.juniorstart.model.JobOfferRequirements;
import com.juniorstart.juniorstart.model.Technologies;
import com.juniorstart.juniorstart.model.TechnologyType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class JobOfferRequest {
	@NotBlank
    private String message;

    private String type;

	@NotBlank
    private String position;

    private TechnologyType technologyType;

    private List<Technologies> technologies;

    private List<JobOfferRequirements> requirements;

    private String telephoneNumber;

    @Email
    private String email;
}
