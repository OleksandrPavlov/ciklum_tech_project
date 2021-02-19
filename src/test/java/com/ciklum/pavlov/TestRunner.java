package com.ciklum.pavlov;

import com.ciklum.pavlov.jdbc.handler.HandlerFactory;
import org.apache.commons.io.FileUtils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) throws IOException {
        Result result = JUnitCore.runClasses(TestSuite.class);
        File file = new File("test_result.txt");
        result.getFailures().forEach(System.out::println);
        FileUtils.writeLines(file, result.getFailures());
        String notificationMsg = result.wasSuccessful() ? "All Tests were successfully passed" :
                "Tests were finished with error!";
        notificationMsg += String.format(" by [%s]", LocalDateTime.now());
        FileUtils.writeStringToFile(file, notificationMsg, "UTF-8");
    }
}
