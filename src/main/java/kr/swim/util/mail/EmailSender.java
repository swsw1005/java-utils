package kr.swim.util.mail;

import com.google.gson.Gson;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * 이메일 전송하는 client 객체
 * <p>
 * ex)
 * <PRE>
 * EmailSender emailSender = new EmailSender();
 * emailSender.setTitle(title);
 * .
 * . (set other params)
 * .
 * emailSender.send();
 *
 * </PRE>
 */
@Getter
@Setter
public class EmailSender {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("EmailSender");

    private final String UTF8 = String.valueOf(StandardCharsets.UTF_8);

    public static final int DEFAULT_SMTP_PORT = 25;
    public static final int DEFAULT_IMPLIED_SSL_PORT = 465;
    public static final int DEFAULT_EXPLICIT_SSL_PORT = 587;

    private String smtpUser = null;
    private String smtpPassword = null;
    private String smtpHost = null;
    private int smtpPort = 25;
    private boolean authEnable = false;
    private boolean sslEnable = false;
    private boolean tlsEnable = false;
   public final Set<String> recipientTypeTO = new HashSet<>();
    public final Set<String> recipientTypeCC = new HashSet<>();
    public final Set<String> recipientTypeBCC = new HashSet<>();
    String title = "";
    String body = "";
    EmailBodyType bodyType = EmailBodyType.text;
    final List<File> files = new ArrayList<>();

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
        if (bodyType == null) {
            log.warn("EmailBodyType is null ... set Default");
            bodyType = EmailBodyType.text;
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
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUser, smtpPassword);
                }
            });

            if (smtpUser == null || smtpPassword == null || smtpUser.isEmpty() || smtpPassword.isEmpty()) {
                session = Session.getInstance(prop);
            }

            // Authenticator auth = Authentica
            // Session session = Session.getDefaultInstance(prop, auth);

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
                message.addRecipients(Message.RecipientType.TO, ccInternetAddress);
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
            message.setText(body, UTF8, bodyType.name());

            if (!files.isEmpty()) {

                log.debug("attach file count  " + files.size());

                Multipart mp = new MimeMultipart();

                for (File file : files) {

                    if (!file.exists()) {
                        throw new FileNotFoundException(file.getAbsolutePath());
                    }

                    String htmlBody = ""; // ...
                    byte[] attachmentData = Files.readAllBytes(file.toPath());

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
            throw new EmailSendException("이메일 발송 실패 " + e + "  " + e.getMessage());
        }

    }

}
