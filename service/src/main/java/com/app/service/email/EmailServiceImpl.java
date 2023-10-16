package com.app.service.email;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.stereotype.Service;

import static j2html.TagCreator.head;
import static j2html.TagCreator.html;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    final Mailer mailer;
    final EmailConfiguration emailConfiguration;
    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public void sendWithAttachmentAndNotification(String recipient, String notificationRecipient, String subject,
                                                  String content, String filename, byte[] attachment) {
        var email = EmailBuilder
                .startingBlank()
                .from(emailConfiguration.fromName, emailConfiguration.fromAddress)
                .to(recipient)
                .withSubject(subject)
                .withHTMLText(content)
                .withAttachment(filename, attachment, "application/pdf")
                .buildEmail();
        mailer.sendMail(email)
                .thenAccept(res -> {});

        mailSentNotification(notificationRecipient, recipient);
    }

    @Override
    public void send(String recipient, String subject, String content) {
        var email = EmailBuilder
                .startingBlank()
                .from(emailConfiguration.fromName, emailConfiguration.fromAddress)
                .to(recipient)
                .withSubject(subject)
                .withHTMLText(content)
                .buildEmail();
        mailer.sendMail(email)
                .thenAccept(res -> mailer.shutdownConnectionPool());
    }

    private void mailSentNotification(String notificationRecipient, String recipient) {
        send(notificationRecipient, "MAIL SENT NOTIFICATION",
                html(head("MAIL SENT TO: %s".formatted(recipient))).toString());
        logger.info("MAIL SENT TO: {}%n", recipient);
    }
}
