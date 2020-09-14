package com.juniorstart.juniorstart.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class EmileMassagesTest {

    @Test
    @DisplayName("Topic message")
    void topic() {
        //When
        String topicMessage = EmileMassages.topic("Test Data");

        //Then
        assertThat(topicMessage).isEqualTo("Your Test Data has been changed");
    }

    @Test
    @DisplayName("Welcome message")
    void welcome() {
        //When
        String welcomeMessage = EmileMassages.welcome("Test Name");

        //Then
        assertThat(welcomeMessage).isEqualTo("Welcome : Test Name.");
    }

    @Test
    @DisplayName("Email message")
    void message() {
        //When
        String emileMessage = EmileMassages.message("Test Data", "Test Value");

        //Then
        assertThat(emileMessage).isEqualTo("Your Test Data has been changed " +
                LocalDate.now().toString() + "to : Test Value.");
    }

    @Test
    @DisplayName("Button sign")
    void buttonName() {
        //When
        String buttonName = EmileMassages.buttonName("Test Data");

        //Then
        assertThat(buttonName).isEqualTo("Change : Test Data.");
    }

    @Test
    @DisplayName("Link to change data")
    void changeLink() {
        //When
        String changeLink = EmileMassages.changeLink("Test link");

        //Then
        assertThat(changeLink).isEqualTo("Test link");
    }

    @Test
    @DisplayName("Goodbye message")
    void goodbye() {
        //When
        String goodbyeMessage = EmileMassages.goodbye();

        //Then
        assertThat(goodbyeMessage).isEqualTo("Best regards,");
    }

    @Test
    @DisplayName("Display app name")
    void appName() {
        //When
        String appName = EmileMassages.appName();

        //Then
        assertThat(appName).isEqualTo("Junior Start app");
    }
}