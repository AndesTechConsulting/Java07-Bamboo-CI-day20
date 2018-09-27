package ru.andstech.learning.rfb18;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    //public final static String BASE_ADDRESS="http://localhost:8383/HTML5App3/";

    public final static String BASE_ADDRESS="http://192.168.56.1:8383/HTML5App3/";
    //public final static String BASE_ADDRESS="http://andestech.org/learning/rfb18/";
    public final static String LOGIN_ADDRESS=BASE_ADDRESS+"login.html";

    private WebDriver driver;
    private LoginData loginData;
    private WebDriverWait wait;

    public LoginPage(WebDriver driver, LoginData loginData){

        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.loginData = loginData;
        wait = new WebDriverWait(driver,10);

    }

    @FindBy(id = "login")
    private WebElement login;

    @FindBy(id = "pass")
    private WebElement password;

    @FindBy(name = "submit")
    private WebElement submit;

    @FindBy(name = "reset")
    private WebElement reset;

    @FindBy(linkText = "Logout")
    private WebElement logout;


    private void processLogin() throws InterruptedException {
        if(!driver.getCurrentUrl().contains(LOGIN_ADDRESS)) driver.get(LOGIN_ADDRESS);
        reset.click();
      //  Thread.sleep(30000);
        login.sendKeys(loginData.getLogin());
        password.sendKeys(loginData.getPassword());
        submit.click();}

      public boolean LoginIsFailed() throws InterruptedException {
        processLogin();
        wait.until(ExpectedConditions.alertIsPresent());
        String alertText = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();
        return(alertText.matches("^Неверный.*"));

    }

     public void logOut(){
        logout.click();
        driver.switchTo().alert().accept();
     }

     public boolean isLoggedIn(){
       // Изменить на проверку заголовка с подстрокой логина
       Cookie ck = driver.manage().getCookieNamed("loginOk");
       if(ck==null) return false;
       return(ck.getValue().equals(loginData.getLogin()));
     }

     public boolean LoginIsTrue() throws InterruptedException {
        processLogin();
        // один из многих вариантов
        wait.until(ExpectedConditions.titleContains("Моя страница"));
        return(true);
    }

}
