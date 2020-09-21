package com.juniorstart.juniorstart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JobOfferRequirements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String textRequirement;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade =  CascadeType.ALL)
    @JoinColumn(name = "jobOffer_id", nullable = true)
    private JobOffer jobOffer;




}
