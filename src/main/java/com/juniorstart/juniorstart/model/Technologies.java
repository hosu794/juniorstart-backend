package com.juniorstart.juniorstart.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Technologies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(unique=true, nullable = false)
    private String title;
}
