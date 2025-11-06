package com.StepImplementation;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qa.util.DriverFactory;
import com.qa.util.ExecutionState;
import com.qa.util.ProjectStatus;


import io.cucumber.java.en.Given;

public class browserOpen_StepDef {

	private Set<String> visitedurls;
	public static String currentURL;
	private ProjectStatus status;
	
	private Logger logger = LoggerFactory.getLogger(browserOpen_StepDef.class);

	public String getCurrentURL() {

		return currentURL;
	}

	@SuppressWarnings("unused")
	private void setCurrentURL(String URL) {
		currentURL = URL;

	}

	public browserOpen_StepDef() {
		this.visitedurls = new HashSet<String>();
		this.status = new ProjectStatus();

	}

	/* Modified by Gouse, 8/27/2024 */
	@Given("I open the {string} browser")
	public void open_the_browser(String browserName) {
		DriverFactory.openbrowser(browserName);
		// Added multiple browsers and changed the stepDef

		/* Modified by Gouse, 8/27/2024 */

	}

	@Given("Enter url of the website {string}")
	public void enter_the_url_of_website_url(String URL) {

		currentURL = URL;

		if (!visitedurls.contains(getCurrentURL())) {
			visitedurls.add(getCurrentURL());
			DriverFactory.getDriver().navigate().to(getCurrentURL());
			status.check_if_a_website_server_is_up_or_not(getCurrentURL());

		}
	}

	@Given("^Enter url of the website preview page '(.+)'$")
	public void enter_the_url_of_websitepreview_page(String URL) {
		try {
			DriverFactory.getDriver().navigate().to(URL);
			DriverFactory.getDriver().manage().timeouts().implicitlyWait(Duration.ofMinutes(2));
			List<WebElement> iframes = DriverFactory.getDriver().findElements(By.tagName("iframe"));
			logger.info("iframe size : {}", iframes.size());
			DriverFactory.getDriver().switchTo().frame(0);
		
		} catch (SessionNotCreatedException ex) {
			logger.info(ex.getMessage().toString());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
