package kr.swim.util.system;

import com.google.gson.Gson;
import org.junit.Test;

public class SystemInfoTest {

    @Test
    public void systemInfo() {

        SystemInfo systemInfo = new SystemInfo();


        System.out.println();
        System.out.println();
        System.out.println(new Gson().toJson(systemInfo));
        System.out.println();
        System.out.println();

    }
}
