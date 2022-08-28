package kr.swim.util.enc;


import kr.swim.util.base64.Base64Utils;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;

/**
 * AES256Util
 * <p>
 * 암호화와 복호화 과정에서 동일한 키를 사용하는 대칭 키 알고리즘
 */
@Slf4j
public class AesUtils {

    public static final String ENC_KEY = "b00fbYHa07K7444K87e63B23135ED9c113b07380d757774aER8d8ac73a98hla382";

    private static final String ALG = "AES/CBC/PKCS5Padding";

    private static final String ALGORITHM_NAME = "AES";

    private static final String ENCODING = String.valueOf(StandardCharsets.UTF_8);

    public static final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    // 암호화
    public static String encrypt(String str) throws EncodingException {
        return encrypt(str, ENC_KEY);
    }

    public static String encrypt(String str, String key) throws EncodingException {
        try {
            validateParam(str, key);
            String secretKey = key.substring(0, 32);
            byte[] textBytes = str.getBytes(ENCODING);

            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(secretKey.getBytes(ENCODING), ALGORITHM_NAME);
            Cipher cipher = null;
            cipher = Cipher.getInstance(ALG);
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
            return Base64Utils.encode2String(cipher.doFinal(textBytes));
        } catch (EncodingException e) {
            throw e;
        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                log.error(e + "  " + e.getMessage(), e);
            }
            throw new EncodingException(e);
        }

    }

    // 복호화
    public static String decrypt(String str) throws EncodingException {
        return decrypt(str, ENC_KEY);
    }

    public static String decrypt(String str, String key) throws EncodingException {
        try {
            validateParam(str, key);
            String secretKey = key.substring(0, 32);
            byte[] textBytes = Base64Utils.decode(str);

            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(secretKey.getBytes(ENCODING), ALGORITHM_NAME);
            Cipher cipher = Cipher.getInstance(ALG);
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            return new String(cipher.doFinal(textBytes), ENCODING);

        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                log.error(e + "  " + e.getMessage(), e);
            }
            throw new EncodingException(e);
        }
    }

    private static void validateParam(String str, String key) throws EncodingException {
        if (str == null) {
            throw new EncodingException("target string null");
        }
        if (key == null) {
            throw new EncodingException("key string null");
        }
        if (key.length() < 32) {
            throw new EncodingException("key string should longer than 32");
        }
    }

}