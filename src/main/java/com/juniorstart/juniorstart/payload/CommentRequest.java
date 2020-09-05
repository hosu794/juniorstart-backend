package com.juniorstart.juniorstart.payload;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    private String body;


}
