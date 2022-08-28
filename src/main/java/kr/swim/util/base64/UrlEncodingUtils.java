package kr.swim.util.base64;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlEncodingUtils {


    public static byte[] decode(byte[] origin) {
        try {
            return Base64Utils.decoder.decode(origin);
        } catch (RuntimeException e) {
            log.warn(e + "  " + e.getMessage());
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error(e + "  " + e.getMessage(), e);
            } else {
                log.warn(e + "  " + e.getMessage());
            }
        }
        return new byte[0];
    }

    public static byte[] decode(String origin) {
        try {
            return Base64Utils.decoder.decode(origin.getBytes(StandardCharsets.UTF_8));
        } catch (RuntimeException e) {
            log.warn(e + "  " + e.getMessage());
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error(e + "  " + e.getMessage(), e);
            } else {
                log.warn(e + "  " + e.getMessage());
            }
        }
        return new byte[0];
    }

    public static String decode2String(byte[] origin) {
        try {
            return String.valueOf(decode(origin));
        } catch (RuntimeException e) {
            log.warn(e + "  " + e.getMessage());
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error(e + "  " + e.getMessage(), e);
            } else {
                log.warn(e + "  " + e.getMessage());
            }
        }
        return null;
    }

    public static String decode2String(String origin) {
        try {
            return String.valueOf(decode(origin.getBytes(StandardCharsets.UTF_8)));
        } catch (RuntimeException e) {
            log.warn(e + "  " + e.getMessage());
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error(e + "  " + e.getMessage(), e);
            } else {
                log.warn(e + "  " + e.getMessage());
            }
        }
        return null;
    }

    public static byte[] encode(byte[] origin) {
        try {
            return Base64Utils.encoder.encode(origin);
        } catch (RuntimeException e) {
            log.warn(e + "  " + e.getMessage());
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error(e + "  " + e.getMessage(), e);
            } else {
                log.warn(e + "  " + e.getMessage());
            }
        }
        return new byte[0];
    }

    public static byte[] encode(String origin) {
        try {
            return Base64Utils.encoder.encode(origin.getBytes(StandardCharsets.UTF_8));
        } catch (RuntimeException e) {
            log.warn(e + "  " + e.getMessage());
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error(e + "  " + e.getMessage(), e);
            } else {
                log.warn(e + "  " + e.getMessage());
            }
        }
        return new byte[0];
    }

    public static String encode2String(byte[] origin) {
        try {
            return new String(encode(origin));
        } catch (RuntimeException e) {
            log.warn(e + "  " + e.getMessage());
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error(e + "  " + e.getMessage(), e);
            } else {
                log.warn(e + "  " + e.getMessage());
            }
        }
        return null;
    }

    public static String encode2String(String origin) {
        try {
            return String.valueOf(encode(origin.getBytes(StandardCharsets.UTF_8)));
        } catch (RuntimeException e) {
            log.warn(e + "  " + e.getMessage());
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.error(e + "  " + e.getMessage(), e);
            } else {
                log.warn(e + "  " + e.getMessage());
            }
        }
        return null;
    }


}
