package ru.andstech.learning.rfb18;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
//import java.io.File
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private WebDriver driver;
    private Wait wait;

    @BeforeClass
    public void init() throws MalformedURLException {
        // System.out.println( "++ driver starts..." );
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\and\\Downloads\\chromedriver_win32\\chromedriver.exe");

        ChromeOptions co = new ChromeOptions();
        String profilePath = "C:\\Users\\and\\AppData\\Local\\Google\\Chrome\\User Data";
        String profilePath_linux = "/home/and/.config/google-chrome";


       // co.addArguments("user-data-dir=" + profilePath);
       // co.addArguments("user-data-dir=" + profilePath_linux);
        co.setExperimentalOption("useAutomationExtension", false);
        co.addArguments("--start-maximized");
        co.addArguments("--verbose");




        DesiredCapabilities caps = DesiredCapabilities.chrome();
        caps.setBrowserName("chrome" );
        caps.setCapability(ChromeOptions.CAPABILITY, co);
        caps.setPlatform(Platform.LINUX);
       // caps.setCapability("loggingPrefs", "{ 'browser':'ALL' }");
//        capability.setVersion("35.0.1");

      // driver = new ChromeDriver(co);
        driver = new RemoteWebDriver(new URL("http://192.168.56.101:4444/wd/hub"),caps);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver,5);

        System.out.println( "++ driver started." + driver );
    }


    @Test
    public void saver() throws FileNotFoundException, IOException, InterruptedException {

        driver.get("http://google.ru");

//     File myfile = new File("e:\\screens\\screen_\\" +
//             System.currentTimeMillis()+".png");

     File pic =   ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

     Path myfile2 = Paths.get("e:\\screens\\screen_" +
             System.currentTimeMillis()+".png");
     Path myfile3 = Paths.get("e:\\screens\\blog_" +
                System.currentTimeMillis()+".log");

     Files.copy(new FileInputStream(pic),myfile2);

     System.out.println(driver.manage().logs().getAvailableLogTypes());

      //  Files.write(myfile3, driver.manage().logs().get("browser"));



     List<String> logLines = new ArrayList<>();
     for(LogEntry le: driver.manage().logs().get("browser").getAll() )
     {
         String logStr = le.toString();
         logLines.add(logStr);
         System.out.println(le.toString());
     }

     Files.write(myfile3, logLines);

      String window1 =  driver.getWindowHandle();
      System.out.println(window1);
      Set<String> set1 = driver.getWindowHandles();

        System.out.println(set1);

     ((JavascriptExecutor) driver).executeScript("window.open();");

        Set<String> set2 = driver.getWindowHandles();

        System.out.println(set2);

        set2.removeAll(set1);
        String handler2 = (String) set2.toArray()[0];

        Thread.sleep(30000);

        driver.switchTo().window(handler2);
        driver.get("http://oracle.com");


    }

    @Test
    public void sliderTest() throws InterruptedException {
        driver.get(LoginPage.BASE_ADDRESS);

        WebElement slider = driver.findElement(By.id("price"));

        Actions actions = new Actions(driver);

          actions.moveToElement(slider,0,0).click();


          for(int i=0; i<8; i++){
          TimeUnit.SECONDS.sleep(1);
          actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).build().perform();}


    }

    /**
     * Тест нужно проводить когда пользователь dweider
     * уже создан.
     *
     * @throws InterruptedException
     */
    @Test
    public void loginTest() throws InterruptedException {
        driver.get(LoginPage.LOGIN_ADDRESS);


        LoginData ldata = new LoginData("dweider","dW654321");

        LoginPage lPage = new LoginPage(driver, ldata);
        if(lPage.isLoggedIn()) lPage.logOut();

        assertTrue(lPage.LoginIsTrue());


        if(lPage.isLoggedIn()) lPage.logOut();
        ldata = new LoginData("dweider","dW123456");
        lPage = new LoginPage(driver, ldata);

        assertTrue(lPage.LoginIsFailed());

    }


    @AfterClass
    public void tearDown() throws InterruptedException {

        wait = null;
        if(driver != null) {
            TimeUnit.SECONDS.sleep(2);

            driver.quit();
            System.out.println("-- driver free ok.");}


    }
}
