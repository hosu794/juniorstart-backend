package com.juniorstart.juniorstart.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
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
public class MailService {

    private JavaMailSender javaMailSender;
    private MailTemplateCreator mailCreatorService;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMailMessage.class);

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
        LOGGER.info("Starting email preparation");
        try {
            MimeMessagePreparator mailMessage = createMimeMessage(mail);
            javaMailSender.send(mailMessage);
            LOGGER.info("Email has been sent");
        } catch (MailException e) {
            LOGGER.error("Failed to process email sending: ", e);
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
            messageHelper.setSubject(EmileMassages.topic(mail.getTemplateValues().getChangedData()));
            messageHelper.setText(mailCreatorService.buildEmailTemplate(mail.getTemplateValues()), true);
        };
    }
}
