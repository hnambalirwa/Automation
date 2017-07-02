package Reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.DriverFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by Harriet Nambalirwa on 29/06/17.
 */
public class ExtentManager {

    private static ExtentReports extent;
    private static ExtentTest test;
    private static ExtentHtmlReporter htmlReporter;
    private static String filePath = "./extentreport.html";
    private static String ImagesPath = "ScreenShots";


    public static ExtentReports GetExtent()
    {
        if (extent != null)
            return extent; //avoid creating new instance of html file
        extent = new ExtentReports();
        extent.attachReporter(getHtmlReporter());
        return extent;
    }

    private static ExtentHtmlReporter getHtmlReporter() {

        htmlReporter = new ExtentHtmlReporter(filePath);

        // make the charts visible on report open
        htmlReporter.config().setChartVisibilityOnOpen(true);

        htmlReporter.config().setDocumentTitle("QA automation report");
        htmlReporter.config().setReportName("Regression cycle");
        return htmlReporter;
    }

    public static ExtentTest createTest(String name, String description){
        test = extent.createTest(name, description);
        return test;
    }

    public static void displayScreenShotOnReport(String screenShotName)
    {
        String imagePath = ImagesPath + "/" + screenShotName+".jpg";
        TakesScreenshot screenShot = (TakesScreenshot) DriverFactory.driver;
        File srcFile = screenShot.getScreenshotAs(OutputType.FILE);
        File destFile = new File(ImagesPath + "/" + screenShotName+".jpg");

        try
        {
            FileUtils.copyFile(srcFile, destFile);
            DriverFactory.test.addScreenCaptureFromPath(imagePath);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
