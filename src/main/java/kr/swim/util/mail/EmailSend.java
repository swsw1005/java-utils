package kr.swim.util.mail;

import java.io.File;
import java.util.*;

public class EmailSend {

    private static final org.slf4j.Logger log
            = org.slf4j.LoggerFactory.getLogger("EmailSender");

    /**
     * <PRE>
     * 메일 보내는 코드 (tls 사용X)
     * </PRE>
     *
     * @param smtpUser         메일계정 ID
     * @param smtpPassword     메일계정 password
     * @param smtpHost         메일보내는 host 메일
     * @param smtpPort         메일서버 port
     * @param authEnable       Authentication 사용 여부
     * @param sslEnable        ssl 사용 여부
     * @param recipientTypeTO  수신 이메일 목록
     * @param recipientTypeCC  참조 이메일 목록
     * @param recipientTypeBCC 숨은참조 이메일 목록
     * @param title            제목
     * @param body             본문내용
     * @param bodyType         본문 타입
     *                         {@link EmailBodyType}
     * @param files            첨부파일 목록
     * @throws EmailSendException
     */
    @Deprecated
    public static void sendMultipartMail(
            final String smtpUser,
            final String smtpPassword,
            final String smtpHost,
            final int smtpPort,
            final Boolean authEnable,
            final Boolean sslEnable,
            Set<String> recipientTypeTO,
            Set<String> recipientTypeCC,
            Set<String> recipientTypeBCC,
            String title,
            String body,
            EmailBodyType bodyType,
            List<File> files) throws EmailSendException {
        sendMultipartMail(smtpUser, smtpPassword, smtpHost, smtpPort, authEnable, sslEnable, null, recipientTypeTO,
                recipientTypeCC, recipientTypeBCC, title, body, bodyType, files);
    }

    /**
     * <PRE>
     * send email with STARTTLS options
     * {@link EmailSender}
     * </PRE>
     *
     * @param smtpUser         메일계정 ID
     * @param smtpPassword     메일계정 password
     * @param smtpHost         메일보내는 host 메일
     * @param smtpPort         메일서버 port
     * @param authEnable       Authentication 사용 여부
     * @param sslEnable        ssl 사용 여부
     * @param tlsEnable        starttls 사용 여부
     * @param recipientTypeTO  수신 이메일 목록
     * @param recipientTypeCC  참조 이메일 목록
     * @param recipientTypeBCC 숨은참조 이메일 목록
     * @param title            제목
     * @param body             본문내용
     * @param bodyType         본문 타입
     *                         {@link EmailBodyType}
     * @param files            첨부파일 목록
     * @throws EmailSendException
     */
    @Deprecated
    public static void sendMultipartMail(
            final String smtpUser,
            final String smtpPassword,
            final String smtpHost,
            final int smtpPort,
            final Boolean authEnable,
            final Boolean sslEnable,
            final Boolean tlsEnable,
            Set<String> recipientTypeTO,
            Set<String> recipientTypeCC,
            Set<String> recipientTypeBCC,
            String title,
            String body,
            EmailBodyType bodyType,
            List<File> files) throws EmailSendException {

        EmailSender emailSender = new EmailSender();

        emailSender.setSmtpHost(smtpHost);
        emailSender.setSmtpUser(smtpUser);
        emailSender.setSmtpPassword(smtpPassword);
        emailSender.setSmtpPort(smtpPort);

        emailSender.setTitle(title);
        emailSender.setBody(body);
        emailSender.setBodyType(bodyType);

        if (authEnable != null) {
            emailSender.setAuthEnable(authEnable);
        }

        if (sslEnable != null) {
            emailSender.setSslEnable(sslEnable);
        }

        if (tlsEnable != null) {
            emailSender.setTlsEnable(tlsEnable);
        }

        if (recipientTypeTO != null) {
            emailSender.recipientTypeTO.addAll(recipientTypeTO);
        }
        if (recipientTypeCC != null) {
            emailSender.recipientTypeCC.addAll(recipientTypeCC);
        }
        if (recipientTypeBCC != null) {
            emailSender.recipientTypeBCC.addAll(recipientTypeBCC);
        }
        if (files != null) {
            emailSender.files.addAll(files);
        }

        emailSender.send();

    }

}
