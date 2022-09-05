package kr.swim.util.system;

import com.google.gson.Gson;
import kr.swim.util.http.PortCheckUtil;
import org.junit.Test;

public class PublicIPTest {

    @Test
    public void ipTest1() {

        String ip = PublicIpInfo.getPublicIp();

        System.out.println("ip = " + ip);

    }

    @Test
    public void ipTest2() {
        NetworkIpInfo ip = NetworkIpInfo.getInstance();
        System.out.println("ip = " + new Gson().toJson(ip));
    }


    @Test
    public void portCheck() {

        boolean available = PortCheckUtil.available(5432);

        boolean use = PortCheckUtil.using(5432);


        System.out.println("available = " + available + "  " + "use = " + use);
    }


}
