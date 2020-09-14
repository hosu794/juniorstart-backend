package com.juniorstart.juniorstart.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

/** Service class for sending emails 10-09-2020.
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class MailService {

    private JavaMailSender javaMailSender;
    private MailTemplateCreator mailCreatorService;

    /** Creates an object with data for sending email.
     * @param javaMailSender class with method to send emails.
     * @param mailCreatorService class creating template by thymeleaf.
     */
    public MailService(JavaMailSender javaMailSender, MailTemplateCreator mailCreatorService) {
        this.javaMailSender = javaMailSender;
        this.mailCreatorService = mailCreatorService;
    }

    /** Method sending email by JavaMailSender interface.
     * @param mail holding data for createMimeMessage() method.
     */
    public void send(final Mail mail) {
        log.info("Starting email preparation");
        try {
            MimeMessagePreparator mailMessage = createMimeMessage(mail);
            javaMailSender.send(mailMessage);
            log.info("Email has been sent");
        } catch (MailException e) {
            log.error("Failed to process email sending: ", e);
        }
    }

    /** Method for creating MimeMessagePreparator object for JavaMailSender.send().
     * @param mail class with data for MimeMessagePreparator and MailTemplateCreator.
     * @return MimeMessagePreparator interface for the preparation of JavaMail MIME messages.
     * */
    private MimeMessagePreparator createMimeMessage(final Mail mail) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(mail.getMailTo());
            messageHelper.setSubject(EmailMassageTemplate.topic(mail.getTemplateValues().getChangedData()));
            messageHelper.setText(mailCreatorService.buildEmailTemplate(mail.getTemplateValues()), true);
        };
    }
}
