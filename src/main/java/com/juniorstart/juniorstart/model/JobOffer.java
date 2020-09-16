package com.juniorstart.juniorstart.model;



import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobOffer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String message;
	
	@NotNull
	private String type;
	
	@NotNull
	private String position;
	
	@NotNull
	private String technologies;
	
	@NotNull
	private String requirements;
	
	@NotNull
	private String contact;
	@JsonIgnore
	@ManyToOne (fetch = FetchType.LAZY, optional = true, cascade =  CascadeType.ALL)
    @JoinColumn(name = "offerCreator_id", nullable = true)

    private User offerCreator;

}
