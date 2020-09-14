package com.juniorstart.juniorstart.email;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/** Class for creating email message template 10-09-2020.
 * @author Dawid Wit
 * @version 1.0
 * @since 1.0
 */
@Service
public class MailTemplateCreator {

    private final TemplateEngine templateEngine;

    public MailTemplateCreator(@Qualifier("templateEngine") TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /** Method for inject variables for thymeleaf html template.
     * @param values class for inject values for thymeleaf context.
     * @return values inject into Email-alert-template.html.
     */
    public String buildEmailTemplate(TemplateValues values){
        Context context = new ContextBuilder()
                .addVariable("topic",EmileMassages.topic(values.getChangedData()))
                .addVariable("welcome",EmileMassages.welcome(values.getName()))
                .addVariable("message",EmileMassages.message(values.getChangedData(),values.getDataValue()))
                .addVariable("button",EmileMassages.buttonName(values.getChangedData()))
                .addVariable("change_url",EmileMassages.changeLink(values.getChangeDataLink()))
                .addVariable("goodbye", EmileMassages.goodbye())
                .addVariable("app_name",EmileMassages.appName()).build();

        return templateEngine.process("email/Email-alert-template.html",context);
    }
}
