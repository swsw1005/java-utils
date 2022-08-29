package kr.swim.util.mail;

import com.google.gson.Gson;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * 이메일 전송하는 client 객체
 * <p>
 * ex)
 * <PRE>
 * EmailSender emailSender = EmailSenderFactory.***
 * {@link kr.swim.util.mail.EmailSenderFactory}
 * <p>
 * emailSender.setTitle(title);
 * emailSender.setBody(body);
 * .
 * . (set other params)
 * .
 * emailSender.send();
 *
 *
 *
 * </PRE>
 */
@Getter
@Builder
public class EmailSender {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("EmailSender");

    private final String UTF8 = String.valueOf(StandardCharsets.UTF_8);

    public static final int DEFAULT_SMTP_PORT = 25;
    public static final int DEFAULT_IMPLIED_SSL_PORT = 465;
    public static final int DEFAULT_EXPLICIT_SSL_PORT = 587;

    @Setter
    private String smtpUser;
    @Setter
    private String smtpPassword;
    @Setter
    private String smtpHost;
    @Setter
    private int smtpPort;
    @Setter
    private boolean authEnable = false;
    @Setter
    private boolean sslEnable = false;
    @Setter
    private boolean tlsEnable = false;
    @Builder.Default
    public final Set<String> recipientTypeTO = new HashSet<>();
    @Builder.Default
    public final Set<String> recipientTypeCC = new HashSet<>();
    @Builder.Default
    public final Set<String> recipientTypeBCC = new HashSet<>();
    @Setter
    String title = "";
    @Setter
    String body = "";
    @Builder.Default
    public final List<File> files = new ArrayList<>();

    private void validate() {
        if (title == null || title.isEmpty()) {
            throw new InvalidParameterException("title is null or empty");
        }
        if (smtpUser == null || smtpUser.isEmpty()) {
            throw new InvalidParameterException("SMTP_USER is null or empty");
        }
        if (smtpHost == null || smtpHost.isEmpty()) {
            throw new InvalidParameterException("SMTP_HOST is null or empty");
        }
        if (body == null || body.isEmpty()) {
            log.warn("mail body is null ... set empty body");
            body = "";
        }
        if (recipientTypeTO.isEmpty()) {
            throw new InvalidParameterException("empty TO email ... ");
        }

        final boolean noPassword = smtpPassword == null || smtpPassword.isEmpty();

        if (authEnable && noPassword) {
            throw new InvalidParameterException("SMTP_PASSWORD is null or empty");
        } else if (!authEnable && !noPassword) {
            log.warn("you enter password and (authEnable == false) ... might be invalid setting");
        }

        if (sslEnable && tlsEnable) {
            log.warn("both ssl and tls enabled ... might be invalid setting");
        }

    }


    public void send() throws EmailSendException {

        try {
            validate();

            Properties prop = new Properties();
            if (tlsEnable) {
                prop.put("mail.smtp.starttls.enable", "true");
            }
            prop.put("mail.smtp.host", smtpHost);
            prop.put("mail.smtp.port", smtpPort);

            if (authEnable) {
                prop.put("mail.smtp.auth", "true");
            }
            if (sslEnable) {
                prop.put("mail.smtp.ssl.enable", "true");
            }

            log.debug("send mail... " + smtpUser + " [ " + smtpHost + " : " + smtpPort + " ] " + new Gson().toJson(prop));

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUser, smtpPassword);
                }
            });

            if (smtpUser == null || smtpPassword == null || smtpUser.isEmpty() || smtpPassword.isEmpty()) {
                session = Session.getInstance(prop);
            }

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUser));

            int toCnt = 0;
            int ccCnt = 0;
            int bccCnt = 0;

            InternetAddress[] toInternetAddress = null;
            InternetAddress[] ccInternetAddress = null;
            InternetAddress[] bccInternetAddress = null;

            if (recipientTypeTO != null) {
                toInternetAddress = new InternetAddress[recipientTypeTO.size()];

                for (String mail : recipientTypeTO) {
                    InternetAddress temp = new InternetAddress(mail);
                    toInternetAddress[toCnt] = temp;
                    toCnt++;
                }
                message.addRecipients(Message.RecipientType.TO, toInternetAddress);
            }
            if (recipientTypeCC != null) {
                ccInternetAddress = new InternetAddress[recipientTypeCC.size()];

                for (String mail : recipientTypeCC) {
                    InternetAddress temp = new InternetAddress(mail);
                    ccInternetAddress[ccCnt] = temp;
                    ccCnt++;
                }
                message.addRecipients(Message.RecipientType.CC, ccInternetAddress);
            }
            if (recipientTypeBCC != null) {
                bccInternetAddress = new InternetAddress[recipientTypeBCC.size()];

                for (String mail : recipientTypeBCC) {
                    InternetAddress temp = new InternetAddress(mail);
                    bccInternetAddress[bccCnt] = temp;
                    bccCnt++;
                }
                message.addRecipients(Message.RecipientType.BCC, bccInternetAddress);
            }

            log.debug(" mail send  TO : " + toCnt + "   CC : " + ccCnt + "   BCC : " + bccCnt);

            // Subject
            message.setSubject(title, UTF8);
            // Text
            message.setText(body, UTF8, "html");

            if (!files.isEmpty()) {

                log.debug("attach file count  " + files.size());

                Multipart mp = new MimeMultipart();

                for (File file : files) {

                    if (!file.exists()) {
                        throw new FileNotFoundException(file.getAbsolutePath());
                    }

                    String htmlBody = "";

                    MimeBodyPart htmlPart = new MimeBodyPart();
                    htmlPart.setContent(htmlBody, "text/html");
                    mp.addBodyPart(htmlPart);

                    MimeBodyPart mimeBodyPart = new MimeBodyPart();

                    DataSource dataSource = new FileDataSource(file.getAbsolutePath());

                    mimeBodyPart.setDataHandler(new DataHandler(dataSource));
                    mimeBodyPart.setFileName(file.getName());

                    mp.addBodyPart(mimeBodyPart);

                }
                message.setContent(mp);
                // [END multipart_example]
            }

            Transport.send(message);

        } catch (FileNotFoundException e) {
            log.error("try attach file ... file not found ! " + e.getMessage());
            throw new EmailSendException("이메일 발송 실패 " + e + "  " + e.getMessage());
        } catch (Exception e) {
            log.error(e + "  " + e.getMessage() + "  title : " + title, e);
            throw new EmailSendException(e);
        }

    }

}
