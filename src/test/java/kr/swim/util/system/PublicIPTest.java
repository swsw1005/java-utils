package kr.swim.util.system;

import org.junit.Test;

public class PublicIPTest {

    @Test
    public void ipTest() {

        String ip = PublicIpInfo.getPublicIp();

        System.out.println("ip = " + ip);

    }

}
