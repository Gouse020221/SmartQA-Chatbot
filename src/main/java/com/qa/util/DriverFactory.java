package com.qa.util;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
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
			        ChromeOptions chromeOptions = new ChromeOptions();
			        chromeOptions.addArguments("--headless");
			        chromeOptions.addArguments("--no-sandbox");
			        chromeOptions.addArguments("--disable-dev-shm-usage");
			       // chromeOptions.addArguments("--window-size=1920,1080");
			        driver = new ChromeDriver(chromeOptions);
			        break;

			    case "firefox":
			        WebDriverManager.firefoxdriver().setup();
			        FirefoxOptions firefoxOptions = new FirefoxOptions();
			        //firefoxOptions.(true); // simpler API for Firefox
			       // firefoxOptions.addArguments("--width=1920");
			        //firefoxOptions.addArguments("--height=1080");
			        driver = new FirefoxDriver(firefoxOptions);
			        break;

			    case "edge":
			        WebDriverManager.edgedriver().setup();
			        EdgeOptions edgeOptions = new EdgeOptions();
			        edgeOptions.addArguments("--headless");
			        edgeOptions.addArguments("--disable-gpu"); // required for Edge headless
			       // edgeOptions.addArguments("--window-size=1920,1080");
			        driver = new EdgeDriver(edgeOptions);
			        break;

			    default:
			        throw new IllegalArgumentException("Unsupported browser: " + browser);
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
