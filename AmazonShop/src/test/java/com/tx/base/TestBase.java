package com.tx.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.TestNG;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import io.github.bonigarcia.wdm.WebDriverManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;
public class TestBase {

	public static WebDriver driver;
	public static Properties envConfig;
	WebDriverWait wait;
	private static final String START_MAXIMIZED = "--start-maximized";
	// Environment value fetched from POM
	public static final String ENV = System.getProperty("env", "Demo");

	// BROWSER value fetched from POM with Chrome being the default value
	private static final String BROWSER = System.getProperty("browser", "Chrome");

	// Extent report
	public static ExtentSparkReporter htmlSparkReporter;
	public static ExtentReports extent;
	public static ExtentTest test;

	// Automation suite setup method to configure and instantiate a particular
	// browser
	@BeforeSuite
	public void suiteSetup() throws Exception {

		// Browser configuration - can add more browsers and remote driver here
		if (BROWSER.equals("Firefox")) {
			WebDriverManager.firefoxdriver().setup(); // can also use set property method for browser executables
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.addArguments(START_MAXIMIZED);
			firefoxOptions.setLogLevel(FirefoxDriverLogLevel.INFO);
			driver = new FirefoxDriver(firefoxOptions);
		} else if (BROWSER.equals("Chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments(START_MAXIMIZED);

			chromeOptions.addArguments("--disable-infobars");
			chromeOptions.addArguments("--disable-notifications");
			chromeOptions.addArguments("--disable-extensions");
			chromeOptions.addArguments("--disable-session-crashed-bubble");
			chromeOptions.addArguments("--disable-save-password-bubble");
			chromeOptions.addArguments("test-type");
			chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			chromeOptions.setExperimentalOption("useAutomationExtension", false);

			chromeOptions.addArguments("--disable-in-process-stack-traces");
			chromeOptions.addArguments("--disable-logging");
			chromeOptions.addArguments("--disable-dev-shm-usage");
			chromeOptions.addArguments("--log-level=3");
			chromeOptions.addArguments("--output=/dev/null");
			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);
			chromeOptions.setExperimentalOption("prefs", prefs);
			chromeOptions.setExperimentalOption("excludeSwitches", Arrays.asList("disable-popup-blocking"));
			driver = new ChromeDriver(chromeOptions);
		} else if (BROWSER.equals("EDGE")) {
			WebDriverManager.edgedriver().setup();
			EdgeOptions edgeOptions = new EdgeOptions();
			edgeOptions.addArguments(START_MAXIMIZED);
			driver = new EdgeDriver(edgeOptions);
		} else {
			throw new RuntimeException("Browser type unsupported");
		}

		// Setting implicit wait
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		driver.manage().window().maximize();

		// Setting WebDriverWait with max timeout value of 20 seconds
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		// Environment specific properties file loading
		InputStream configFile = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\test\\java\\com\\tx\\config\\" + ENV + ".properties");
		envConfig = new Properties();
		envConfig.load(configFile);

		// Disable TestNG report
		TestNG testNG = new TestNG();
		testNG.setUseDefaultListeners(false);

		// Extent report
		// initialize the HtmlReporter
		htmlSparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/ExtentReports/testReport.html");

		// initialize ExtentReports and attach the HtmlReporter
		extent = new ExtentReports();
		extent.attachReporter(htmlSparkReporter);

		// configuration items to change the look and feel
		// add content, manage tests etc
		htmlSparkReporter.config().setDocumentTitle("TX Automation Report");
		htmlSparkReporter.config().setReportName("Sanity Test");
		// htmlSparkReporter.config().setTheme(Theme.DARK);
		htmlSparkReporter.config().setTheme(Theme.STANDARD);
		htmlSparkReporter.config().setEncoding("utf-8");
		htmlSparkReporter.config().setJs("js-string");
		htmlSparkReporter.config().setProtocol(Protocol.HTTPS);
		htmlSparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

	}

	@BeforeMethod()
	public void loadBaseUrl(Method method) {

		driver.get(envConfig.getProperty("baseUrlShop"));

	}

	@AfterMethod
	public void deleteCookies() {
		// Deleting cookies
		driver.manage().deleteAllCookies();
		test.log(Status.INFO, "Finished");

	}

	public static String CaptureScreenshot(ITestResult testResult) throws IOException {
		// Taking screenshot in case of failure
		String myScreenshootPath = "";
		if (testResult.getStatus() == ITestResult.FAILURE) {
			String FileSeparator = System.getProperty("file.separator");
			String Extent_report_path = "." + FileSeparator + "ExtentReports";
			String ScreenshotPath = Extent_report_path + FileSeparator + "screenshots";
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String screenshotName = "screenshot" + testResult.getName() + "-"
					+ Arrays.toString(testResult.getParameters()) + ".jpg";
			String screenshotpath = ScreenshotPath + FileSeparator + screenshotName;

			FileUtils.copyFile(scrFile, new File(screenshotpath));
			myScreenshootPath = "." + FileSeparator + "screenshots" + FileSeparator + screenshotName;
		}

		return myScreenshootPath;
	}

	@AfterSuite
	public void suiteTearDown() {
		extent.flush();
		driver.quit();
	}

	public void refreshPage() {
		driver.navigate().refresh();
	}

}
