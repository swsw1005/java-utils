package kr.swim.util.io;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HttpsURLConnection;
import java.io.Closeable;
import java.net.HttpURLConnection;
import java.net.URLConnection;

@Slf4j
public class CloseResourceHelper {

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (RuntimeException e) {
            log.warn(e + "  " + e.getMessage());
        } catch (Exception e) {
            log.warn(e + "  " + e.getMessage());
        }
    }

    public static void close(HttpURLConnection connection) {
        try {
            if (connection != null) {
                connection.disconnect();
            }
        } catch (RuntimeException e) {
            log.warn(e + "  " + e.getMessage());
        } catch (Exception e) {
            log.warn(e + "  " + e.getMessage());
        }
    }

    public static void close(HttpsURLConnection connection) {
        try {
            if (connection != null) {
                connection.disconnect();
            }
        } catch (RuntimeException e) {
            log.warn(e + "  " + e.getMessage());
        } catch (Exception e) {
            log.warn(e + "  " + e.getMessage());
        }
    }

}
