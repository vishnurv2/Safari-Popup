
import com.lambdatest.tunnel.Tunnel;
import cucumber.api.java.eo.Se;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class demoDesktop {

    public String username = System.getProperty("LT_USERNAME");
    public String accesskey = System.getProperty("LT_ACCESS_KEY");
    public String gridURL = System.getProperty("GRID_URL");

    public RemoteWebDriver driver;
    String status;
    String hub;
    String bhub;
    SessionId sessionId;


    @org.testng.annotations.Parameters(value = {"browser", "version", "platform","fixedip"})
    @BeforeTest
    public void setUp(String browser, String version, String platform,String fixedip) throws Exception {
        for (int i = 0; i < 50; i++) {

            try {

                DesiredCapabilities caps = new DesiredCapabilities();

                caps.setCapability("platformName", "Big Sur");
                caps.setCapability("browserName", "safari");
                caps.setCapability("version", "latest");
                caps.setCapability("fixedIP", fixedip);
                caps.setCapability("build", "Safari Automation Pop-up : Big Sur");
                caps.setCapability("name", fixedip + " - Count : " + i);


                StopWatch driverStart = new StopWatch();
                driverStart.start();

                hub = "https://" + username + ":" + accesskey + "@" + gridURL + "/wd/hub";

                System.out.println("Hub URL ---> " + hub);

                driver = new RemoteWebDriver(new URL(hub), caps);

                System.out.println("caps : - - " + caps + "----------------");
                sessionId = driver.getSessionId();
                System.out.println("Starting Session iD ---------- > " + sessionId);
                driverStart.stop();
                float timeElapsed = driverStart.getTime() / 1000f;
                System.out.println("Driver initiate time" + "   " + timeElapsed);
                ArrayList<Float> TotalTimeDriverSetup = new ArrayList<Float>();
                TotalTimeDriverSetup.add(timeElapsed);
                System.out.println(TotalTimeDriverSetup);
                DesktopScript();
                tearDown();

            } catch (MalformedURLException e) {
                System.out.println("Invalid grid URL");
            } catch (Exception f) {
                System.out.println(f);
            }
        }
    }

//    @Test
    public void DesktopScript() {
        try {

            driver.get("https://duckduckgo.com");
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            WebDriverWait el = new WebDriverWait(driver,10);
            el.until(ExpectedConditions.visibilityOf(driver.findElementById("search_form_input_homepage")));
            driver.findElementById("search_form_input_homepage").sendKeys("lambdatest");
            driver.findElementById("search_form_input_homepage").sendKeys(Keys.ENTER);
            driver.get("https://apple.com");
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            ((JavascriptExecutor) driver).executeScript("lambda-status=passed");

        } catch (Exception e) {
            System.out.println(e);
            ((JavascriptExecutor) driver).executeScript("lambda-status=failed");

        }
    }


//    @AfterTest
    public void tearDown() throws Exception {
        if (driver != null) {
//            ((JavascriptExecutor) driver).executeScript("lambda-status=" + status);
            System.out.println("!!! Quitting session : ------- "+sessionId);
            driver.quit();
        }
    }
}

