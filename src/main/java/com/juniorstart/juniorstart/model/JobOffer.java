package com.juniorstart.juniorstart.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

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

	private String type;

	@NotNull
	private String position;

	@Enumerated(EnumType.STRING)
	private TechnologyType technologyType;

	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(
			name = "jobOffer_technologies",
			joinColumns = @JoinColumn(name = "jobOffer_id"),
			inverseJoinColumns = @JoinColumn(name = "technology_id"))
	private Set<Technologies> technologies = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<JobOfferRequirements> jobOfferRequirements = new HashSet<>();

	private String telephoneNumber;

	private String email;

	@JsonIgnore
	@ManyToOne (fetch = FetchType.LAZY, optional = true, cascade =  CascadeType.ALL)
    @JoinColumn(name = "offerCreator_id", nullable = true)
    private User offerCreator;
}
