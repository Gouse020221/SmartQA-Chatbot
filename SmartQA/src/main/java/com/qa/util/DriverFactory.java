package com.qa.util;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;


import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
	private static WebDriver driver = null;
	private static boolean browserOpened = false;
	public static final String location = System.getProperty("user.home") + "\\Downloads\\";

	public static WebDriver openbrowser(String browser) {
		if (!browserOpened) {
			if (driver == null) {
				switch (browser.toLowerCase()) {
				case "chrome":
					WebDriverManager.chromedriver().setup();

					driver = new ChromeDriver();
					break;

				case "firefox":
					WebDriverManager.firefoxdriver().setup();

					driver = new FirefoxDriver();
					break;

				case "edge":
					WebDriverManager.edgedriver().setup();
					driver = new EdgeDriver();
					break;

				case "safari":
					// Safari doesn't need WebDriverManager setup as it comes with macOS.
					driver = new SafariDriver();
					break;

				default:
					throw new IllegalArgumentException("Browser \"" + browser + "\" is not supported.");
				}

				browserOpened = true;
				driver.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
				waitForPageLoad();
			}
		}
		return driver;
	}

	public static synchronized WebDriver getDriver() {
		return driver;

	}

	/**
	 * @param driver the driver to set
	 */
	public static void setDriver(WebDriver driver) {
		DriverFactory.driver = driver;
	}

	public static void waitForPageLoad() {

		ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";");

	}

}
