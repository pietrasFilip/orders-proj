package com.app.service.email;


public interface EmailService {
    void sendWithAttachmentAndNotification(String recipient, String notificationRecipient, String subject,
                                           String content, String filename, byte[] attachment);
    void send(String recipient, String subject, String content);
}
