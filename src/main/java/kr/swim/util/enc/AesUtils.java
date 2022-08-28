package kr.swim.util.enc;


import kr.swim.util.base64.Base64Utils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * AES256Util
 * <p>
 * 암호화와 복호화 과정에서 동일한 키를 사용하는 대칭 키 알고리즘
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AesUtils {

    protected static final String ENC_KEY = "b00fbYHa07K7444K87e63B23135ED9c113b07380d757774aER8d8ac73a98hla382";

    protected static final String AES_CBC_PKCS_5_PADDING = "AES/CBC/PKCS5Padding";

    protected static final String AES = "AES";

    protected static final String UTF_8 = String.valueOf(StandardCharsets.UTF_8);

    private static final boolean isDebug = log.isDebugEnabled();


    /**
     * <PRE>
     * AES256 암호화
     * 기본 key 사용
     * iv from key 사용
     * </PRE>
     *
     * @param str 암호화 대상 문자열
     * @return 암호화된 String
     * @throws EncodingException
     */
    public static String encrypt(String str) throws EncodingException {
        if (isDebug) {
            log.debug("encrypt | str ");
        }
        return encrypt(str, ENC_KEY, generateIvFromString(ENC_KEY));
    }

    /**
     * <PRE>
     * AES256 암호화
     * iv from key 사용
     * </PRE>
     *
     * @param str 암호화 대상 문자열
     * @param key String(32) 암호key
     * @return 암호화된 String
     * @throws EncodingException
     */
    public static String encrypt(String str, final String key) throws EncodingException {
        if (isDebug) {
            log.debug("encrypt | str | key");
        }
        return encrypt(str, key, generateIvFromString(key));
    }

    /**
     * <PRE>
     * AES256 암호화
     * 기본 key 사용
     * </PRE>
     *
     * @param str 암호화 대상 문자열
     * @param iv  byte[16] 암호화 iv
     * @return 암호화된 String
     * @throws EncodingException
     */
    public static String encrypt(String str, final byte[] iv) throws EncodingException {
        if (isDebug) {
            log.debug("encrypt | str | iv");
        }
        return encrypt(str, ENC_KEY, iv);
    }

    /**
     * <PRE>
     * AES256 암호화
     * </PRE>
     *
     * @param str 암호화 대상 문자열
     * @param key String(32) 암호key
     * @param iv  byte[16] 암호화 iv
     * @return 암호화된 String
     * @throws EncodingException
     */
    public static String encrypt(final String str, final String key, final byte[] iv) throws EncodingException {
        try {
            validateParam(str, key, iv);
            String secretKey = key.substring(0, 32);
            byte[] textBytes = str.getBytes(UTF_8);

            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec newKey = new SecretKeySpec(secretKey.getBytes(UTF_8), AES);
            Cipher cipher = null;
            cipher = Cipher.getInstance(AES_CBC_PKCS_5_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
            byte[] a = cipher.doFinal(textBytes);
            return Base64Utils.encode2String(a);
        } catch (EncodingException e) {
            throw e;
        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                log.error(e + "  " + e.getMessage(), e);
            }
            throw new EncodingException(e);
        }

    }


    /**
     * <PRE>
     * AES256 복호화
     * 기본 key 사용
     * iv from key 사용
     * </PRE>
     *
     * @param str 복호화 대상 문자열
     * @return
     * @throws EncodingException
     */
    public static String decrypt(String str) throws EncodingException {
        if (isDebug) {
            log.debug("decrypt | str");
        }
        return decrypt(str, ENC_KEY, generateIvFromString(ENC_KEY));
    }


    /**
     * <PRE>
     * AES256 복호화
     * iv from key 사용
     * </PRE>
     *
     * @param str 복호화 대상 문자열
     * @param key String(32) 복호화 key
     * @return
     * @throws EncodingException
     */
    public static String decrypt(String str, final String key) throws EncodingException {
        if (isDebug) {
            log.debug("decrypt | str | key");
        }
        return decrypt(str, key, generateIvFromString(key));
    }


    /**
     * <PRE>
     * AES256 복호화
     * 기본 key 사용
     * </PRE>
     *
     * @param str 복호화 대상 문자열
     * @param iv  byte[16] 복호화 iv
     * @return
     * @throws EncodingException
     */
    public static String decrypt(String str, final byte[] iv) throws EncodingException {
        if (isDebug) {
            log.debug("decrypt | str | iv");
        }
        return decrypt(str, ENC_KEY, iv);
    }

    /**
     * <PRE>
     * AES256 복호화
     * </PRE>
     *
     * @param str 복호화 대상 문자열
     * @param key String(32) 복호화 key
     * @param iv  byte[16] 복호화 iv
     * @return
     * @throws EncodingException
     */
    public static String decrypt(final String str, final String key, final byte[] iv) throws EncodingException {
        try {
            validateParam(str, key, iv);
            String secretKey = key.substring(0, 32);
            byte[] textBytes = Base64Utils.decode(str);

            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec newKey = new SecretKeySpec(secretKey.getBytes(UTF_8), AES);
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS_5_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            byte[] a = cipher.doFinal(textBytes);
            return new String(a, UTF_8);
        } catch (EncodingException e) {
            throw e;
        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                log.error(e + "  " + e.getMessage(), e);
            }
            throw new EncodingException(e);
        }
    }

    protected static byte[] generateIvFromString(final String key) throws EncodingException {

        if (key == null) {
            throw new EncodingException("key is null");
        }

        String var1 = key;
        while (true) {
            if (var1.length() >= 16) {
                break;
            }
            var1 += key;
        }
        var1 = var1.substring(0, 16);
        return var1.getBytes();
    }

    /**
     * <PRE>
     * AES256 암/복호화 parameter
     * validation
     * </PRE>
     *
     * @param str 암/복호화 대상 문자열
     * @param key String(32) 암/복호화 key
     * @param iv  byte[16] 암/복호화 iv
     * @throws EncodingException
     */
    protected static void validateParam(final String str, final String key, final byte[] iv) throws EncodingException {
        if (str == null) {
            throw new EncodingException("target string null");
        }
        if (key == null) {
            throw new EncodingException("key string null");
        }
        if (iv == null) {
            throw new EncodingException("byte[] iv null");
        }
        if (iv.length != 16) {
            throw new EncodingException("byte[] iv length not 16 ... it's [" + iv.length + "]");
        }
    }

}