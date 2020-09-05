package com.juniorstart.juniorstart.payload;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectTodoRequest {

    private String title;

}
