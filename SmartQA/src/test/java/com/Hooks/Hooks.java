package com.Hooks;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.StepImplementation.CustomLogCollector;
import com.qa.util.DriverFactory;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

public class Hooks {

	// public Scenario scenario;

	private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

	@AfterStep
	public void afterStep(Scenario scenario) {
		// ExecutionState.markStepAsExecuted(scenario.getName());

		List<String> logs = CustomLogCollector.getLogs();

		if (!logs.isEmpty()) {
			scenario.attach(String.join("\n", logs), "text/plain", "Custom Logs");
			// Clear the logs after attaching to the report
			CustomLogCollector.clearLogs();
		}

	}

	@After
	public void tearDown(Scenario scenario) {
		// driverUtil.VerifyPageStatus();

		if (scenario.isFailed()) {
			try {
				logger.error("{} : has failed", scenario.getName());

				byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
				String testName = scenario.getName();
				logger.info("{} Status - {}.", testName, scenario.getStatus());
				scenario.attach(screenshot, "image/png", testName);
			} catch (WebDriverException somePlatformsDontSupportScreenshots) {
				logger.info(somePlatformsDontSupportScreenshots.getMessage());
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	}

	// @BeforeAll
	public static void before_all() {
		try {

			//ScreenRecorderUtil.startRecord("Feature");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// @AfterAll
	public static void after_all() {
		try {

		//	ScreenRecorderUtil.stopRecord();
			File folder = new File("./test-recordings/");
			File[] files = folder.listFiles();

			if (files != null && files.length > 0) {
				// Find the last modified file (which should be the video for this scenario)
				File latestFile = files[files.length - 1]; // assuming this is the video just recorded

				// Attach the video to the Cucumber report
				Path path = Paths.get(latestFile.getAbsolutePath());
				String absolutePath = path.toString();

				// Create a link to the video (make sure to use relative paths for portability)
				logger.info("Video Recording available at: {} ", absolutePath);

				// Attach the link to the Cucumber report
				// scenario.attach(message.getBytes(), "text/html", "Video Recording");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

//