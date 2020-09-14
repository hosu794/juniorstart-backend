package com.juniorstart.juniorstart.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import static org.mockito.Mockito.*;

/** Test for MailServiceTest.class 13-09-2020.
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @InjectMocks
    MailService mailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    @DisplayName("send method")
    void testSend() {
        //Given
        TemplateValues values = TemplateValues.builder()
                .changeDataLink("test link")
                .changedData("test Data")
                .dataValue("test value")
                .name("test name").build();

        Mail mail = new Mail("test@tes.com",values);

        //When
        mailService.send(mail);

        //Then
        verify(javaMailSender, times(1)).send(any(MimeMessagePreparator.class));
    }
}