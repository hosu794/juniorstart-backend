package com.juniorstart.juniorstart.payload;

import com.juniorstart.juniorstart.model.TechnologyType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.awt.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnologyResponse {

    private Long id;

    private String title;

    private String description;

    private String technologyType;

}
