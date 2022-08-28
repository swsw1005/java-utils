package kr.swim.util.process;


import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProcessExecutor {

    public static String runSimpleCommand(String command) throws IOException, InterruptedException {
        String[] arr = {command};
        return runSimpleCommand(arr);
    }

    public static List<String> runCommand(String command) throws IOException, InterruptedException {
        String[] arr = {command};
        return runCommand(arr);
    }


    public static String runSimpleCommand(String[] command) throws IOException, InterruptedException {
        List<String> list = runCommand(command);
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s + "\n");
        }
        return sb.toString();
    }

    public static List<String> runCommand(String[] command) throws IOException, InterruptedException {

        Process process = null;
        BufferedReader stdReader = null;
        BufferedReader errReader = null;
        InputStreamReader inputReader = null;
        InputStreamReader errorReader = null;
        InputStream is1 = null;
        InputStream is2 = null;

        List<String> standardOutput = new ArrayList<>();
        List<String> errorOutput = new ArrayList<>();

        try {
            log.debug("run command : " + new Gson().toJson(command));
            process = Runtime.getRuntime().exec(command);

            if (process != null) {

                is1 = process.getInputStream();
                is2 = process.getErrorStream();

                inputReader = new InputStreamReader(is1);
                errorReader = new InputStreamReader(is2);

                stdReader = new BufferedReader(inputReader);
                errReader = new BufferedReader(errorReader);

                String line = null;

                while ((line = stdReader.readLine()) != null) {
                    standardOutput.add(line);
                }

                line = null;
                while ((line = errReader.readLine()) != null) {
                    errorOutput.add(line);
                }

            }

            process.waitFor();
            log.debug("exitValue : " + process.exitValue());
            if (process.exitValue() != 0) {
                throw new IOException("Exit Code is not normal");
            }

            if (log.isDebugEnabled()) {
                for (String s : standardOutput) {
                    log.debug(s);
                }
            }

        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
            for (String s : errorOutput) {
                log.warn("\t" + s);
            }
            throw e;
        } catch (IOException e) {
            log.error("IOException", e);
            for (String s : errorOutput) {
                log.warn("\t" + s);
            }
            throw new IOException(e);
        } catch (Exception e) {
            log.error("Unexpected Error", e);
            for (String s : errorOutput) {
                log.warn("\t" + s);
            }
            throw new IOException(e);
        } finally {
            IOUtils.closeQuietly(is1);
            IOUtils.closeQuietly(is2);
            IOUtils.closeQuietly(inputReader);
            IOUtils.closeQuietly(errorReader);
            IOUtils.closeQuietly(stdReader);
            IOUtils.closeQuietly(errReader);
            if (process != null) {
                process.destroy();
            }
        }

        return standardOutput;
    }


}