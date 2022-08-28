package kr.swim.util.mail;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailSenderFactory {


    public static final EmailSender SimpleSmtpWithoutAuth() {
        return SimpleSmtpWithoutAuth(EmailSender.DEFAULT_SMTP_PORT);
    }

    public static final EmailSender SimpleSmtpWithoutAuth(final int port) {
        EmailSender emailSender = EmailSender.builder().smtpPort(port).authEnable(false).sslEnable(false).tlsEnable(false).build();
        return emailSender;
    }

    public static final EmailSender DefaultSSLSmtp() {
        return DefaultSSLSmtp(EmailSender.DEFAULT_IMPLIED_SSL_PORT);
    }

    public static final EmailSender DefaultSSLSmtp(final int port) {
        EmailSender emailSender = EmailSender.builder().smtpPort(port).authEnable(true).sslEnable(true).tlsEnable(false).build();
        return emailSender;
    }

    public static final EmailSender STARTTLSSmtp() {
        return STARTTLSSmtp(EmailSender.DEFAULT_EXPLICIT_SSL_PORT);
    }

    public static final EmailSender STARTTLSSmtp(final int port) {
        EmailSender emailSender = EmailSender.builder().smtpPort(port).authEnable(true).sslEnable(false).tlsEnable(true).build();
        return emailSender;
    }


}
