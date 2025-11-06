package com.StepImplementation;

import static org.testng.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.Locale;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ObjectRepository.LoginPage;
import com.Pages.Login_Page_Actions;
import com.driverUtility.driverUtil;
import com.google.zxing.Result;
import com.qa.util.DriverFactory;
import com.qa.util.ProjectStatus;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginPageSteps {

	private BufferedImage bufferedImage;
	private Result result;
	private String QRCode = null;
	private String secretKey = null;
	private String OTP = null;
	private String Email = null;
	private FileInputStream fi = null;
	private FileOutputStream fo = null;
	private File file;
	private Properties prop;
	private LoginPage login;

//	private DriverUtil utility;
	private Login_Page_Actions loginactions;
	private ProjectStatus pstatus;

	private static final Logger logger = LoggerFactory.getLogger(LoginPageSteps.class);

	public LoginPageSteps() {
		this.login = new LoginPage(DriverFactory.getDriver());
		this.loginactions = new Login_Page_Actions();
		this.pstatus = new ProjectStatus();

	}

	@When("I enter the uname {string}")
	public void Enter_the_userName(String username) {

		if (DriverFactory.getDriver().getCurrentUrl().contains(browserOpen_StepDef.currentURL)) {

			if (!driverUtil.existsElement("//li[contains(@class,'hide-sm')]")) {
				loginactions.Enter_Email_ID(username);
				login.getContinue_btn().click();
				//
				pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
			} else {
				logger.warn("Skipped:: The user is already logged in to the application.");

			}
		}
	}

	@Then("I should see {string} page with all the details")
	public void user_should_able_to_see_the_customer_login_page_with_all_the_details(String Page) {

		if (DriverFactory.getDriver().getCurrentUrl().contains(browserOpen_StepDef.currentURL)) {
			logger.warn(browserOpen_StepDef.currentURL);

			if (!driverUtil.existsElement("//li[contains(@class,'hide-sm')]")) {
				if (Page.equalsIgnoreCase("Customer Login")) {
					loginactions.check_customer_login_page_Details();
					// loginactions.CopyRights_Validation();
				} else if (Page.equalsIgnoreCase("Login")) {
					loginactions.Verify_Login_Page_UI();
					loginactions.click_on_login_button_without_entering_the_password();
					loginactions.CopyRights_Validation();
				} else {
					logger.error("Page is invalid");
				}
			} else {
				logger.error("Skipped:: The user is already logged in to the application.");
			}

		}

	}

	@When("^I enter the password '(.+)' and click on Login button$")
	public void Enter_the_password(String password) {

		if (DriverFactory.getDriver().getCurrentUrl().contains(browserOpen_StepDef.currentURL)) {
			if (!driverUtil.existsElement("//li[contains(@class,'hide-sm')]")) {

				try {

					if (login.getpassWord_txtbox().isDisplayed()) {
						loginactions.Enter_Password(password);
						logger.info("Password Entered Successfully");
						login.getContinue_btn().click();

						pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
					}
				} catch (Exception E) {
					logger.warn(
							"For Not Registered user and support user we are not displaying the enter password field");

				}
			}

			else {
				logger.warn("Skipped:: The user is already logged in to the application.");
			}
		}
	}

	public void Click_on_Continue_Without_entering_login_details() {

		login.getContinue_btn().click();

		pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
//		if (Email_txtbox.isDisplayed()) {
//			logger.info("Password validation text messages are showing properly");
//			logger.info("User validation text messages are showing properly");
//		}

	}

	@Then("^based on the entered login details it should show Error Message or Username$")
	public void based_on_the_entered_login_details_it_should_show_error_message_or_username() {

		// login.Verify_login_functionality();
		loginactions.verify_login_flow();
		// login.user_icon_open.click();
	}

	@When("^I click on the Continue button without entering the email ID$")
	public void i_click_on_continue_button_without_entering_the_email_ID() {

		login.getContinue_btn().click();

		pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());

	}

	@Then("I should see an {string} validation text message")
	public void i_should_see_the_validation_text_message(String text) {

		DriverFactory.getDriver().getPageSource().contains(text);

	}

	@And("^Click on Continue button$")
	public void i_click_on_continue_button() {

		login.getContinue_btn().click();

		pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());

	}

	@Then("^click request password button$")
	public void i_clickon_requestpwd() {
		login.getContinue_btn().click();

		pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
	}

	@Then("^based on the entered email id it should navigate to the login page or should display the error message$")
	public void verifyNavigationOrErrorMessage() {
		WebDriver driver = DriverFactory.getDriver();
		String currentUrl = driver.getCurrentUrl();

		try {
			// ✅ Step 1: Validate correct URL
			if (!currentUrl.contains(browserOpen_StepDef.currentURL)) {
				logger.error("The current URL does not match the expected login URL.");
				Assert.fail("Invalid navigation URL: " + currentUrl);
			}

			// ✅ Step 2: Check if already logged in
			if (driverUtil.existsElement("//li[contains(@class,'hide-sm')]")) {
				logger.warn("Skipped: The user is already logged into the application.");
				return;
			}

			// ✅ Step 3: Handle Invalid / Unregistered Email
			if (driverUtil.existsElement("//div[contains(@class,'invalid-email')]")) {
				String actualMsg = login.getinvalidemail_validation_txt().getText().trim();
				String expectedMsg = "The email address you entered is not registered, Please enter a registered email address";
				Assert.assertEquals(actualMsg, expectedMsg, "Validation message mismatch!");
				logger.warn("Invalid/unregistered email entered. Validation message displayed.");
				return;
			}

			// ✅ Step 4: Handle Different Login Types
			String pageTitle = login.getlogin_title_txt().getText().trim();
			logger.info("Detected login page title: {}", pageTitle);

			switch (pageTitle) {
			case "Customer Login":
				Assert.assertTrue(login.getforgotpassword_lnk().isDisplayed(), "Forgot password link not visible!");
				logger.info("User entered a valid Customer email address.");
				loginactions.Verify_Login_Page_UI();
				loginactions.click_on_login_button_without_entering_the_password();
				loginactions.i_should_see_user_validation_text_messages();
				break;

			case "Support Login":
				Assert.assertTrue(login.have_a_password_link.isDisplayed(), "Support login link not visible!");
				logger.info("User entered a valid Support email address.");
				loginactions.Verify_support_login_Page_UI();
				break;

			case "Insearch Login":
				Assert.assertTrue(login.have_a_password_link.isDisplayed(), "Insearch login link not visible!");
				logger.info("User entered a valid Insearch email address.");
				loginactions.Verify_insearch_login_Page_UI();
				break;

			default:
				logger.warn("Unexpected login page title encountered: {}", pageTitle);
				Assert.fail("Unexpected login title: " + pageTitle);
			}

		} catch (Exception e) {
			logger.error("Error while verifying navigation/login flow: ", e);
			Assert.fail("Test failed due to exception: " + e.getMessage());
		}
	}

}
