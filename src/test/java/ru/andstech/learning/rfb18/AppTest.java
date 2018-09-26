package ru.andstech.learning.rfb18;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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

    public void init()
    {
        // System.out.println( "++ driver starts..." );
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\and\\Downloads\\chromedriver_win32\\chromedriver.exe");

        ChromeOptions co = new ChromeOptions();
        String profilePath = "C:\\Users\\and\\AppData\\Local\\Google\\Chrome\\User Data";


        co.addArguments("user-data-dir=" + profilePath);
        co.setExperimentalOption("useAutomationExtension", false);
        co.addArguments("--start-maximized");


        driver = new ChromeDriver(co);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver,5);

        System.out.println( "++ driver started." + driver );
    }

    @Test
    public void shouldAnswerWithTrue() throws InterruptedException {
        driver.get("http://localhost:8383/HTML5App3/index.html");

        WebElement slider = driver.findElement(By.id("price"));

        Actions actions = new Actions(driver);

        actions.moveToElement(slider,0,0).click().
                sendKeys(Keys.ARROW_RIGHT).
                sendKeys(Keys.ARROW_RIGHT).
                sendKeys(Keys.ARROW_RIGHT).
                sendKeys(Keys.ARROW_RIGHT).build().perform();

        Thread.sleep(4000);

    }

    @Test
    public void loginTest() throws InterruptedException {
        driver.get("http://localhost:8383/HTML5App3/login.html");

        LoginData ldata = new LoginData("vpetrov521","vP123456");

        LoginPage lPage = new LoginPage(driver, ldata);
        if(lPage.isLoggedIn()) lPage.logOut();

        assertTrue(lPage.LoginIsTrue());


        if(lPage.isLoggedIn()) lPage.logOut();
        ldata = new LoginData("vpetrov521","vP12345678");
        lPage = new LoginPage(driver, ldata);

        assertTrue(lPage.LoginIsFailed());

    }


    @AfterClass
    public void free() throws InterruptedException {

        wait = null;
        if(driver != null) {
            TimeUnit.SECONDS.sleep(2);

            driver.close(); driver.quit();
            System.out.println("-- driver free ok.");}


    }
}
