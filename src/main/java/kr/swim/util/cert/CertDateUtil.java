package kr.swim.util.cert;

import kr.swim.util.http.SSLCertCheckUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Calendar;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CertDateUtil {

    public static final Calendar[] GET_CERT_DATE(final String domain) throws IOException {
        try {
            SSLCertCheckUtils.CertInfo var1 = SSLCertCheckUtils.sslCheck(domain);
            return new Calendar[]{var1.getBefore(), var1.getAfter()};
        } catch (RuntimeException e) {
            log.warn(e + " | " + e.getMessage());
            throw new IOException(e);
        } catch (Exception e) {
            log.warn(e + " | " + e.getMessage(), e);
            throw new IOException(e);
        }
    }
}
