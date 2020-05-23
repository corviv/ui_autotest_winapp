package com.github.corviv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Listeners(ListenerLogger.class)
public class CommandUtils {

    private static final Logger logger = LoggerFactory.getLogger("CommandUtils");

    public static String executeCmdCommand(String command) {
        return executeCommand("cmd /c " + command);
    }

    public static String executePyScript(String scriptPathName, String...args) {
        String argsLine = String.join(" ", args);
        return executeCommand("python " + scriptPathName + " " + argsLine);
    }

    public static String executeCommand(String command) {

        StringBuilder output = new StringBuilder();

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                logger.info("Command '{}' completed successfully!", command);
                logger.debug(output.toString());
            } else {
                logger.info("Command '{}' execution error!", command);
                logger.info("Exit code: {}", exitVal);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return output.toString();
    }
}
