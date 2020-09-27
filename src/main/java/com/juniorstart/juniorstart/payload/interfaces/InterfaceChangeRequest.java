package com.juniorstart.juniorstart.payload.interfaces;

import java.util.UUID;

/** Interface common for all chane user data requests 05.09.2020;
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
public interface InterfaceChangeRequest {
    UUID getUserPrivateId();
    String getUserPassword();
}
