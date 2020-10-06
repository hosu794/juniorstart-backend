package com.juniorstart.juniorstart.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/** Test for MailTemplateCreator.class 13-09-2020.
 * @author Noboseki
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class MailTemplateCreatorTest {

    @Autowired
    private MailTemplateCreator creator;

    @Test
    @DisplayName("Test buildEmailTemplate method")
    void testBuildEmailTemplate() {
        //Given
        TemplateValues values = TemplateValues.builder()
                .changeDataLink("test link")
                .changedData("test Data")
                .dataValue("test value")
                .name("test name").build();

        //When
        String emailHtml = creator.buildEmailTemplate(values);

        //Then
        assertThat(emailHtml).isNotEmpty();
    }
}