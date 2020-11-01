package com.juniorstart.juniorstart.payload;

import com.juniorstart.juniorstart.model.UserProfile;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmploymentHistoryRequest {

    @NotBlank
    private String companyName;

    private String tasksAtWork;

    private java.sql.Date dateStartOfEmployment;

    private java.sql.Date dateEndOfEmployment;

    private boolean isCurrentEmployment;

    private UserProfile userProfile;

}
