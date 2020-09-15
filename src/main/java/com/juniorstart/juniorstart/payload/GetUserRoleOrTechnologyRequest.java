package com.juniorstart.juniorstart.payload;

import lombok.Getter;
import java.util.List;

/** Class for method selectionForSearching from UserProfileService
 * @author Rafał Maduzia
 * @version 1.0
 */
@Getter
public class GetUserRoleOrTechnologyRequest {
    private final List<String> technology;
    private final List<String> userRole;

    /** Creates an object ot change email by UserService.changeEmail.
     * @param technology find user by technology.
     * @param userRole ind user by role.
     */
    public GetUserRoleOrTechnologyRequest(List<String> technology, List<String> userRole) {
        this.technology = technology;
        this.userRole = userRole;
    }
}

