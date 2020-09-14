package com.juniorstart.juniorstart.email;

import lombok.Getter;

/** Class for holding data for sending email 10-09-2020.
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@Getter
public class Mail {
    private String mailTo;
    private TemplateValues templateValues;

    /** Creates an object with data for sending email.
     * @param mailTo recipient's email address.
     * @param templateValues values for thymeleaf template email build.
     */
    public Mail(String mailTo, TemplateValues templateValues) {
        this.mailTo = mailTo;
        this.templateValues = templateValues;
    }
}
