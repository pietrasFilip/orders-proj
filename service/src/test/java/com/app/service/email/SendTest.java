package com.app.service.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SendTest {
    @Mock
    Mailer mailer;

    @Test
    @DisplayName("When method is invoked exactly one time")
    void test1() {
        var emailConfig = new EmailConfiguration("test", "test");
        var emailService = new EmailServiceImpl(mailer, emailConfig);

        when(mailer.sendMail(any(Email.class)))
                .thenReturn(CompletableFuture.runAsync(() -> {}));

        emailService.send("test", "test", "test");
        verify(mailer, times(1)).sendMail(any(Email.class));
    }


}
