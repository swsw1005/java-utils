package kr.swim.util.enc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@Slf4j
public class AesTest {


    @Test
    public void test() throws EncodingException, IOException, NoSuchAlgorithmException {


        long a = SecureRandom.getInstanceStrong().nextLong();
        long b = SecureRandom.getInstanceStrong().nextLong();

        String c = String.valueOf(a).substring(1, 10) + String.valueOf(b).substring(1, 10);
        c = c.substring(0, 16);

        final byte[] iv = c.getBytes();
        final String key = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");


        final String filePath = "/usr/local/aesTest.txt";

        for (int i = 0; i < 4; i++) {

            log.debug("=== " + i + " ===========================================");

            List<String> originList = generateTestList();
            List<String> encList = new ArrayList<>();

            log.debug("=== 암호화 ===========================================");

            for (String s : originList) {

                String temp = null;
                switch (i) {
                    case 0:
                        temp = AesUtils.encrypt(s);
                        break;
                    case 1:
                        temp = AesUtils.encrypt(s, key);
                        break;
                    case 2:
                        temp = AesUtils.encrypt(s, iv);
                        break;
                    case 3:
                        temp = AesUtils.encrypt(s, key, iv);
                        break;
                }


                log.debug(s + "\t => \t" + temp);

                encList.add(temp);
            }

            log.debug("=== 파일 저장 ===========================================");

            FileUtils.writeLines(new File(filePath), String.valueOf(StandardCharsets.UTF_8), encList, "\n");

            List<String> encReadList = FileUtils.readLines(new File(filePath), String.valueOf(StandardCharsets.UTF_8));

            log.debug("=== 복호화 ===========================================");


            for (int j = 0; j < encReadList.size(); j++) {

                String s = encReadList.get(j);

                final String origin = originList.get(j);

                String temp = null;
                switch (i) {
                    case 0:
                        temp = AesUtils.decrypt(s);
                        break;
                    case 1:
                        temp = AesUtils.decrypt(s, key);
                        break;
                    case 2:
                        temp = AesUtils.decrypt(s, iv);
                        break;
                    case 3:
                        temp = AesUtils.decrypt(s, key, iv);
                        break;
                }

                log.debug(s + "\t => \t" + temp);

                assertEquals(temp, origin);
            }


            new File(filePath).delete();


        }


        System.out.println("key = " + key);


    }


    List<String> generateTestList() {
        List<String> list = new ArrayList<>();
        list.add("swim");
        list.add("1111");
        list.add("어느날 짜라투스트라는 이렇게 말했다.");
        list.add("양꼬치");
        list.add("컴퓨터");
        return list;
    }


}
