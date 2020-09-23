package com.juniorstart.juniorstart.payload;

import com.juniorstart.juniorstart.model.JobOfferRequirements;
import com.juniorstart.juniorstart.model.Technologies;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Data
@Builder
public class JobOfferRequest {
	@NotBlank
    private long publicId;
	
	@NotBlank
    private String message;
	
	@NotBlank
    private String type;
	
	@NotBlank
    private String position;
	
	@NotBlank
    private List<Technologies> technologies;

    @NotBlank
    private List<JobOfferRequirements> requirements;

    private String telephoneNumber;

    private String email;
}
