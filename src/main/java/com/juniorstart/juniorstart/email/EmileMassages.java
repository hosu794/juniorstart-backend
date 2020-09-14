package com.juniorstart.juniorstart.email;

import java.io.Serializable;
import java.time.LocalDate;

/** Singleton class with static methods for build email 12-09-2020.
 * @author Dawid Wit
 * @implement Serializable need for protecting class from serialization.
 * @version 1.0
 * @since 1.0
 */
public class EmileMassages implements Serializable {

    private static EmileMassages instance = new EmileMassages();

    private EmileMassages() {
    }

    public static String topic(String changedData) {
        return "Your " + changedData +" has been changed";
    }

    public static String welcome(String name) {
        return "Welcome : " + name + ".";
    }

    public static String message(String changedData, String dataValue) {
        return "Your " + changedData + " has been changed " +
                LocalDate.now().toString() + "to : " + dataValue + ".";
    }

    public static String buttonName(String changedData) {
        return "Change : " + changedData + ".";
    }

    public static String changeLink(String link) {
        return link;
    }

    public static String goodbye(){
        return "Best regards,";
    }

    public static String appName(){
        return "Junior Start app";
    }

    /** Method for protecting class from serialization.
     */
    protected Object readResolve() {
        return instance;
    }
}