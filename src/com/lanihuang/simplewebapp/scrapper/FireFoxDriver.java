package com.lanihuang.simplewebapp.scrapper;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class FireFoxDriver extends Driver {

    @Override
    protected void initDriver() {
        FirefoxProfile profile = new FirefoxProfile();
        String driverPath = "GecoDriver/geckodriver.exe";
        String osName = System.getProperty("os.name");
        if (osName.equals("Linux") || osName.contains("Linux")) {
            driverPath = "GecoDriver/geckodriver";
        }
        System.setProperty("webdriver.gecko.driver", driverPath);
        driver = new FirefoxDriver(profile);
    }
}
