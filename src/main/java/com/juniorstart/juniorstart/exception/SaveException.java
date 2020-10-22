package com.juniorstart.juniorstart.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Class for Save exception.
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@Getter
@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class SaveException extends RuntimeException {
    private Object fieldValue;
    private String fieldName;

    /** Creates an exception of Jpa save error.
     * @param fieldName of save object.
     * @param fieldValue of saved object.
     */
    public SaveException(String fieldName, Object fieldValue) {
        super(String.format("%s save error of : '%s'", fieldName, fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
