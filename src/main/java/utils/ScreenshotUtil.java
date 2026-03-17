package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    // Captures screenshot and saves it to reports/screenshots folder
    public static String captureScreenshot(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotName = testName + "_" + timestamp + ".png";
        String screenshotPath = System.getProperty("user.dir") + "/reports/screenshots/" + screenshotName;

        try {
            Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/reports/screenshots"));
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), Path.of(screenshotPath));
        } catch (IOException e) {
            System.out.println("Screenshot capture failed: " + e.getMessage());
        }

        return screenshotPath;
    }
}
