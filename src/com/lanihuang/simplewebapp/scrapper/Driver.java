package com.lanihuang.simplewebapp.scrapper;

import org.openqa.selenium.WebDriver;

public abstract class Driver {
    protected WebDriver driver;
    public Driver() {
        initDriver();
    }
    protected abstract void initDriver();
    protected void sleep(long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
