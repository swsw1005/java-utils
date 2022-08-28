package kr.swim.util;

import static org.junit.Assert.assertTrue;

import kr.swim.util.system.SystemInfo;
import com.google.gson.Gson;
import org.junit.Test;

public class AppTest {
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void systemInfo() {

        SystemInfo aa = new SystemInfo();

        System.out.println("SystemInfo = " + new Gson().toJson(aa));

    }
}
