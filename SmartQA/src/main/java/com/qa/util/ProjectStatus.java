package com.qa.util;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectStatus {

	private String[] recipientEmails = { "gshaik@ecisolutions.com", "spanda@ecisolutions.com",
			"svandarolu@ecisolutions.com" };

	private static final Logger logger = LoggerFactory.getLogger(ProjectStatus.class);

	public void check_if_a_website_server_is_up_or_not(String app_url) {

		try {
			Handling_Emails email = new Handling_Emails();
			URL obj = new URI(app_url).toURL();

			// Open a connection to the URL

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");

			// Connect to the server
			con.connect();

			// Get the response status code
			int statusCode = con.getResponseCode();
			logger.info("Status code: {}", statusCode);

			if (statusCode == HttpURLConnection.HTTP_OK) {
				logger.info("For {} the status code is {}; The Website is working fine", app_url, statusCode);
			} else {
				logger.error("For {} the status code is {}; The Website is not working ", app_url, statusCode);

				File scrFile = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
				String timestamp = new SimpleDateFormat("yyyy_MM_dd__hh_mm_ss").format(new Date());

				FileUtils.copyFile(scrFile,
						new File(System.getProperty("user.dir") + "\\screenshots" + timestamp + ".png"));

				email.sendemailnotification(recipientEmails, app_url, statusCode, scrFile);
			}

			// con.disconnect();
		} catch (IOException | URISyntaxException e) {
			logger.warn("Error while checking website: " + e.getMessage());

		}

	}

}
