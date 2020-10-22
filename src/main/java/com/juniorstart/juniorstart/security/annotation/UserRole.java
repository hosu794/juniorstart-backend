package com.juniorstart.juniorstart.security.annotation;


import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Annotation for authorize USER role.
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('USER')")
public @interface UserRole {
}
