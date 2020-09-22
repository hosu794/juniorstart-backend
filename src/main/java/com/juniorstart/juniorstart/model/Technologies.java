package com.juniorstart.juniorstart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Technologies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String nameTechnology;

    @JsonIgnore
    @ManyToMany(mappedBy = "technologies")
    private Set<JobOffer> jobOffers = new HashSet<>();

    public void addJobOffer(JobOffer jobOffer){ this.jobOffers.add(jobOffer); }
    public void deleteJobOffer(JobOffer jobOffer){ this.jobOffers.remove(jobOffer);}
}
