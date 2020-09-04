package com.juniorstart.juniorstart.helpers;

import com.juniorstart.juniorstart.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class getLogedUserId {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User customUser = (User)authentication.getPrincipal();

    public Long userID =   customUser.getPublicId();
}
