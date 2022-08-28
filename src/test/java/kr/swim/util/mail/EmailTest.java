package kr.swim.util.mail;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class EmailTest {


    @Test
    public void googleMailTest() throws EmailSendException {

        EmailSender emailSender = EmailSenderFactory.STARTTLSSmtp();

        emailSender.setSmtpHost("smtp.gmail.com");
        emailSender.setSmtpUser("swsw1005@gmail.com");
        emailSender.setSmtpPassword("111111111");

        emailSender.recipientTypeTO.add("2222222@gmail.com");

        emailSender.setTitle("html body email test - real text");
        emailSender.setBody("html body email test - real text");
        emailSender.recipientTypeCC.add("swsw1005@gmail.com");
        emailSender.send();


    }

}
