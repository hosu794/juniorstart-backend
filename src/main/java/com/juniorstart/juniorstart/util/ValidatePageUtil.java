package com.juniorstart.juniorstart.util;

import com.juniorstart.juniorstart.exception.BadRequestException;

/** Util class to validate a pageable object.
 * @author Grzegorz Szczęsny
 * @version 1.0
 * @since 1.0
 */
public class ValidatePageUtil {


    /** Retrieve cookies from request.
     * @param page The page's number.
     * @param size The page's size.
     * @return A cookie if exists, otherwise empty object.
     */
    public static void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
