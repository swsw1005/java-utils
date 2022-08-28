package kr.swim.util.system;

import kr.swim.util.process.ProcessExecutor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SystemInfoExtractor {

    public static synchronized String getUptime() {
        String line = "";
        try {
            return ProcessExecutor.runSimpleCommand("uptime");
        } catch (IOException | InterruptedException e) {
            log.warn(e + " | " + e.getMessage());
        } catch (Exception e) {
            log.warn(e + " | " + e.getMessage(), e);
        }
        return line;
    }

    public static String getLocalIpAddress() {
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
            ip = getIpAddress();
        }
        if (ip == null || ip.trim().equals("127.0.0.1")) {
            return getIpAddress();
        }

        return ip;
    }

    public static String getIpAddress() {
        String ip = null;
        try {
            boolean isLB = true;
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();

            while (en.hasMoreElements()) {
                NetworkInterface ni = en.nextElement();
                if (ni.isLoopback()) {
                    continue;
                }

                Enumeration<InetAddress> ia = ni.getInetAddresses();
                while (ia.hasMoreElements()) {
                    InetAddress tmp = ia.nextElement();

                    if (tmp.getHostAddress() != null && tmp.getHostAddress().indexOf(".") > -1) {
                        ip = tmp.getHostAddress();
                        isLB = false;
                        break;
                    }
                }
                if (!isLB) {
                    break;
                }
            }
        } catch (SocketException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ip;
    }


    /**
     * 파일시스템 정보를 얻는다
     *
     * @return <PRE>
     *     List&lt;FileSystemInfo&gt;
     * {@link FileSystemInfo}
     * </PRE>
     */
    public static List<FileSystemInfo> getFileSystemInfo() {
        List<FileSystemInfo> fileSystemInfoList = new ArrayList<FileSystemInfo>();
        String lineAdd = "";

        StringBuilder sb = new StringBuilder();

        try {
            List<String> dfh = ProcessExecutor.runCommand("df -h");

            if (dfh != null) {

                for (String line : dfh) {
                    if (!line.startsWith("Filesystem")) {
                        StringTokenizer token = new StringTokenizer(line);
                        if (token.countTokens() == 6) {
                            lineAdd = "";
                            fileSystemInfoList.add(new FileSystemInfo(line));
                        } else {
                            sb.append(line);
                            StringTokenizer tokenAdd = new StringTokenizer(lineAdd);
                            if (tokenAdd.countTokens() == 6) {
                                fileSystemInfoList.add(new FileSystemInfo(lineAdd));
                            }
                        }
                    }
                }

            }
        } catch (IOException | InterruptedException e) {
            log.warn(e + " " + e.getMessage());
        } catch (Exception e) {
            log.error(e + " " + e.getMessage());
        }

        return fileSystemInfoList;
    }

    public static synchronized List<CpuInfo> getcpuinfo() {

        String line = "";
        CpuInfo cpuinfo = null;

        List<CpuInfo> cpuList = new ArrayList<>();

        try (FileInputStream file = new FileInputStream("/proc/cpuinfo"); InputStreamReader is = new InputStreamReader(file); BufferedReader in = new BufferedReader(is);) {

            try {
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("model name")) {
                        cpuinfo = new CpuInfo();
                        cpuinfo.setCpuName(getValue(line));
                    }
                    if (line.startsWith("cpu MHz")) {
                        cpuinfo.setCpuMhz(getValue(line));
                    }
                    if (line.startsWith("cache size")) {
                        cpuinfo.setCpuCache(getValue(line));
                        cpuList.add(cpuinfo);
                    }
                }
            } catch (NullPointerException e) {
                log.warn(e + "  " + e.getMessage());
            } catch (Exception e) {
                log.warn(e + "  " + e.getMessage(), e);
            }
        } catch (IOException e) {
            log.warn(e + "  " + e.getMessage());
        } catch (Exception e) {
            log.error(e + "  " + e.getMessage());
        }
        return cpuList;
    }


    private static String getValue(String line) {
        int index = line.indexOf(":");
        if (index != -1) {
            return line.substring(index + 1).trim();
        }
        return "";
    }

}
