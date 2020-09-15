package com.juniorstart.juniorstart.payload;



import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    private String technologies;
	
	@NotBlank
    private String requirements;
	
	@NotBlank
    private String contact;
}
