package com.lanihuang.simplewebapp.scrapper;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ChromeDriver extends Driver {
    private final String USER_NAME = "admin@summitperformance.com.au";
    private final String PASSWORD = "fiver2018";

    @Override
    protected void initDriver() 
    {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("window-size-0,0");
        chromeOptions.addArguments("export DISPLAY=:0 ");
        String chromepath = "ChromeDriver/chromedriver.exe";
        String osName = System.getProperty("os.name");
        if (osName.equals("Linux") || osName.contains("Linux")) 
        {
            chromepath = "ChromeDriver/chromedriver";
        }
        System.setProperty("webdriver.chrome.driver", chromepath);
        driver = new org.openqa.selenium.chrome.ChromeDriver(chromeOptions);
    }
    public void initSignInSystem() {
        login();
        WebDriverWait wait = new WebDriverWait(driver,25);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("li-clients")));
        driver.navigate().to("https://summitperformance.trainerize.com/app/group/client.aspx?level=trainerClients");
        WebElement nextElement = driver.findElement(By.id("btn_clients_next"));
        String className = nextElement.getAttribute("class");
        while (!className.contains("disabled")){
            nextElement = driver.findElement(By.id("btn_clients_next"));
            className = nextElement.getAttribute("class");
            try {
                scrollIntoView(nextElement);
                nextElement.click();
                sleep(500);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void login() {
        driver.navigate().to("https://summitperformance.trainerize.com/app/logon.aspx");
        driver.findElement(By.id("t_user")).sendKeys(USER_NAME);
        driver.findElement(By.id("t_pwd")).sendKeys(PASSWORD);
        driver.findElement(By.id("b_logon")).click();
        //driver.
    }
    public void scrollIntoView(WebElement ele) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(" + ele.getLocation().x + "," + ele.getLocation().y + ")");
    }
}