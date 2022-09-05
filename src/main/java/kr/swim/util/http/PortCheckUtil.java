package kr.swim.util.http;

import kr.swim.util.io.CloseResourceHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PortCheckUtil {

    public static boolean using(int port) {
        return !available(port);
    }

    public static boolean available(int port) {

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            if (port < 10 || port > 60000) {
                throw new IllegalArgumentException("Invalid start port: " + port);
            }
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;

        } catch (BindException e) {
            log.warn(e + " this port already used by other process :: " + port);
        } catch (SocketException e) {
            log.warn(e + "  " + e.getMessage());
        } catch (IOException e) {
            log.warn(e + "  " + e.getMessage());
        } catch (Exception e) {
            log.warn(e + "  " + e.getMessage(), e);
        } finally {
            CloseResourceHelper.close(ds);
            CloseResourceHelper.close(ss);
        }

        return false;
    }

}

