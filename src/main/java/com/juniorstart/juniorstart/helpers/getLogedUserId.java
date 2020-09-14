package com.juniorstart.juniorstart.helpers;

import com.juniorstart.juniorstart.security.UserPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class getLogedUserId {

    UserPrincipal myUserDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    public UUID userId=myUserDetails.getId();
}
