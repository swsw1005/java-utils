package kr.swim.util.system;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SystemInfo {
    private final String uptime = SystemInfoExtractor.getUptime();
    private final String localIpAddress = SystemInfoExtractor.getLocalIpAddress();
    private final List<FileSystemInfo> fileSystemInfos = SystemInfoExtractor.getFileSystemInfo();
    private final List<NetworkInterfaceInfo> networkInterfaceInfos = NetworkInterfaceInfo.getNetworkInterfaces();
    private final List<CpuInfo> cpuInfos = SystemInfoExtractor.getcpuinfo();
}
