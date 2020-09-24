package com.juniorstart.juniorstart.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JobOfferRequirements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String textRequirement;
}
