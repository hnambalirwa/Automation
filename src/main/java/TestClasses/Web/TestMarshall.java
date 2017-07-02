package TestClasses.Web;


import entities.Enums;
import utils.SeleniumDriverUtility;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author HARRIET
 */
public class TestMarshall  {

    // Handles calling test methods based on test parameters , instantiates Selenium Driver object   
    //ExcelReaderClass excelInputReader;
    SeleniumDriverUtility SeleniumDriverInstance;
    //public TestReportEmailerUtility reportEmailer;
    PrintStream errorOutputStream;
    PrintStream infoOutputStream;
    private String dateTime;
    private Integer screenShotCounter = 1;

    public TestMarshall() {
//        inputFilePath = ApplicationConfig.InputFileName();
//        testDataList = new ArrayList<TestEntity>();
//        excelInputReader = new ExcelReaderClass();
        //reportGenerator = new ReportGenerator(inputFilePath, ApplicationConfig.ReportFileDirectory());
        //SeleniumDriverInstance = new SeleniumDriverUtility(browserType);

    }

    public TestMarshall(String inputFilePathIn) {
//        inputFilePath = inputFilePathIn;
//        testDataList = new ArrayList<TestEntity>();
//        excelInputReader = new ExcelReaderClass();
       // reportGenerator = new ReportGenerator(inputFilePath, ApplicationConfig.ReportFileDirectory());
       // SeleniumDriverInstance = new SeleniumDriverUtility(browserType);

    }
    
    
    public TestMarshall(String inputFilePathIn, Enums.BrowserType browserTypeOverride) {
//        inputFilePath = inputFilePathIn;
//        testDataList = new ArrayList<TestEntity>();
//        excelInputReader = new ExcelReaderClass();
//        browserType = browserTypeOverride;
        //reportGenerator = new ReportGenerator(inputFilePath, ApplicationConfig.ReportFileDirectory());
       // SeleniumDriverInstance = new SeleniumDriverUtility(browserType);

    }

    public void runKeywordDrivenTests() throws FileNotFoundException {

        int numberOfTest = 0;

        //testDataList = loadTestData(inputFilePath);
       // this.generateReportDirectory();
        this.redirectOutputStreams();

//        if (testDataList.size() < 1) {
//            System.err.println("Test data object is empty - spreadsheet not found or is empty");
//        } else {
//            // Each case represents a test keyword found in the excel spreadsheet
//            for (TestEntity testData : testDataList) {
//                testCaseId = testData.TestCaseId;
//                // Make sure browser is not null - could have thrown an exception and terminated
//                CheckBrowserExists();
//                // Skip test methods and test case id's starting with ';'
//                if (!testData.TestMethod.startsWith(";") && !testData.TestCaseId.startsWith(";")) {
//                    System.out.println("Executing test - " + testData.TestMethod);
//
//                  try{
//                    switch (testData.TestMethod) {
//                        // A login test starts with a fresh Driver instance
//
//                        case "Login to Mix Telematics Web Page":
//                        {
//                             ensureNewBrowserInstance();
////                             Login_to_MixTelematics MXTEL = new Login_to_MixTelematics(testData);
////                            reportGenerator.addResult(MXTEL.executeTest());
//                            numberOfTest++;
//                            break;
//                        }
//
//                    }
//
//                    }catch(Exception ex)
//                    {
//                      System.err.println("[ERROR] Exception was thrown TestMarhsall"+ex.getStackTrace());
//                    }
//
//                    System.out.println("Continuing to next test method");
//                    System.out.println("===========================================================================================================");
//                    SeleniumDriverUtility.resetScreenShotCounter(screenShotCounter);
//                    SeleniumDriverUtility.incrementScreenShotFolderCounter();
//                }
//            }
//            System.out.println("-- END OF TEST PACK EXECUTION --");
//            SeleniumDriverUtility.resetScreenShotFolderCounter(screenShotCounter);
//        }

        SeleniumDriverInstance.shutDown();

        PrintWriter pw = new PrintWriter(new File("NumberOfTestsRun.txt"));
        pw.write((numberOfTest - 1) + "");
        pw.close();

       // reportEmailer.SendResultsEmail();

        this.flushOutputStreams();

    }

//    private List<TestEntity> loadTestData(String inputFilePath) {
//        return excelInputReader.getTestDataFromExcelFile(inputFilePath);
//    }

    public  void CheckBrowserExists() {
        if (SeleniumDriverInstance == null) {
           // SeleniumDriverInstance = new SeleniumDriverUtility(browserType);
            SeleniumDriverInstance.startDriver();
        }
    }

    public  void ensureNewBrowserInstance() {
        if (SeleniumDriverInstance.isDriverRunning()) {
            SeleniumDriverInstance.shutDown();
        }
        SeleniumDriverInstance.startDriver();
    }

    public String generateDateTimeString() {
        Date dateNow = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
        dateTime = dateFormat.format(dateNow).toString();
        return dateTime;
    }

//    public void generateReportDirectory() {
//        reportDirectory = ApplicationConfig.ReportFileDirectory() + "\\" + resolveScenarioName() + "_" + this.generateDateTimeString();
//        String[] reportsFolderPathSplit = this.reportDirectory.split("\\\\");
//        this.currentTestDirectory = ApplicationConfig.ReportFileDirectory() + "\\" + reportsFolderPathSplit[reportsFolderPathSplit.length - 1];
//    }

    public void redirectOutputStreams() 
    {
//        try
//        {
////            File reportDirectoryFile = new File(reportDirectory);
////            reportDirectoryFile.mkdirs();
////
////            FileOutputStream infoFile = new FileOutputStream(reportDirectory + "\\" + "InfoTestLog.txt");
////            FileOutputStream errorFile = new FileOutputStream(reportDirectory + "\\" + "ErrorTestLog.txt");
//
//
//            System.setOut(infoOutputStream);
//            System.setErr(errorOutputStream);
//        }
//        catch (FileNotFoundException ex)
//        {
//            System.err.println("[Error] could not create log files - " + ex.getMessage());
//        }
    }

    public void flushOutputStreams() {

        errorOutputStream.flush();
        infoOutputStream.flush();

        errorOutputStream.close();
        infoOutputStream.close();

    }
}
