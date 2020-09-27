package com.juniorstart.juniorstart.email;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/** Class for inject values for thymeleaf context 12-09-2020.
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@Getter
@Builder
@ToString
public class TemplateValues {
    private String changedData;
    private String dataValue;
    private String name;
    private String changeDataLink;

    /** Creates an object with data for sending email.
     * @param changedData name of changed data.
     * @param dataValue value of changed data.
     * @param name user name.
     * @param changeDataLink link for changing data.
     */
    public TemplateValues( String changedData,  String dataValue,  String name,  String changeDataLink) {
        this.changedData = changedData;
        this.dataValue = dataValue;
        this.name = name;
        this.changeDataLink = changeDataLink;
    }
}
