package com.Pages;

import static org.testng.Assert.assertEquals;

import java.time.Duration;
import java.util.Locale;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Hooks.Hooks;
import com.ObjectRepository.LoginPage;
import com.StepImplementation.browserOpen_StepDef;
import com.driverUtility.driverUtil;
import com.qa.util.DriverFactory;

public class Login_Page_Actions {

	private WebDriver driver;
	private driverUtil utility;
	private LoginPage login;

	private static final Logger logger = LoggerFactory.getLogger(Login_Page_Actions.class);
	// WebDriver driver;
	private String Emailtxt = "Enter Your Email";
	private String Passwordtxt = "Enter Your Password";
	private String ACTUAL_COPY_RIGHTS_YEAR = "Copyright ?? ECI Software Solutions, Inc. 2024. All rights reserved.";
	private String ACTUAL_URL = "www.ecisolutions.com/lotvue";
	private String ACTUAL_LOGIN_PAGE_TXT = "Customer Login";
	private String LOGIN_FAILED_TXT = "Invalid Email or password.";

	public Login_Page_Actions() {
		this.driver = DriverFactory.getDriver();
		this.utility = new driverUtil();
		this.login = new LoginPage(driver);

	}

	public void check_customer_login_page_Details() {

		login.getEmail_txtbox();
		login.getContinue_btn();
		login.getlogin_title_txt();
		assertEquals("Customer Login", login.getlogin_title_txt().getText().trim().toString(), "Both are not matching");

	}

	public void Verify_Login_Page_UI() {
		// login.getEmail_txtbox().i;
		// login.getpassWord_txtbox();
		// login.getLogin_btn();
		// login.getforgotpassword_lnk();
		login.getlogin_header();
		assertEquals("Customer Login", login.getlogin_title_txt().getText().trim().toString(), "Both are not matching");

	}

	public void Verify_support_login_Page_UI() {

		login.gethave_a_password_lnk();
		assertEquals("Support Login", login.getlogin_title_txt().getText().trim().toString(), "Both are not matching");

	}

	public void Verify_insearch_login_Page_UI() {

		login.gethave_a_password_lnk();
		assertEquals("Insearch Login", login.getlogin_title_txt().getText().trim().toString(), "Both are not matching");

	}

	public void Enter_Email_ID(String UName) {
		login.getEmail_txtbox().sendKeys(UName);
	}

	public void Enter_Password(String Password) {

		login.getpassWord_txtbox().sendKeys(Password);
	}

	public void click_on_login_button_without_entering_the_password() {
		try {

			if (driver.getCurrentUrl().contains(browserOpen_StepDef.currentURL)) {
				logger.info("Skip");
			} else {

				if (login.getContinue_btn().isDisplayed()) {
					logger.info("Login Button displayed");
					driverUtil.click_Using_javaScript(login.getContinue_btn());
				}
			}
		} catch (Exception E) {
			logger.error("Login Button is not presented");

		}

	}

	public void i_should_see_user_validation_text_messages() {

		if (driver.getCurrentUrl().contains(browserOpen_StepDef.currentURL)) {
			logger.info("Skip");
		} else {

			assertEquals("Enter Your Password", login.getpassWord_validation_txt().getText().trim().toString());
			logger.info("User successfully able to see the Enter Your Password validation text message");
		}

	}

	public void Verify_login_functionality() {

		try {

			if (driverUtil.existsElement("//div[@class='error_msg']")) {
				logger.error("Login is failed");
				logger.error("Error message: {}",
						login.geterror_msg_txt().getText().trim().toString().toUpperCase(Locale.ENGLISH));
			} else if (login.getinvalidemail_validation_txt().isDisplayed()) {
				logger.error("Login is Failed: {}",
						login.getinvalidemail_validation_txt().getText().trim().toString().toUpperCase(Locale.ENGLISH));

			}

			else {

				if (driverUtil.existsElement("//li[contains(@class,'hide-sm')]")) {

					logger.info("logged in successfully");
					((new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10))))
							.until(ExpectedConditions.visibilityOf(login.getuser_icon()));
					login.getuser_icon().click();

					logger.info("User Name : {}",
							login.getuser_name().getText().trim().toString().toUpperCase(Locale.ENGLISH));

				}

			}
		} catch (NoSuchElementException E) {
			logger.info("User entered support user email");
		}
	}

	public void CopyRights_Validation() {
		assertEquals(ACTUAL_COPY_RIGHTS_YEAR, login.getCopyright_txt().getText().trim().toString(),
				"Both Copy Right's are not matching");
		assertEquals(ACTUAL_URL, login.getECI_url().getText().toString(), "Both are not matching");

	}

	public String getTitle() {
		return DriverFactory.getDriver().getTitle().trim().toString();
	}

	public String getCurrentPageURL() {
		return DriverFactory.getDriver().getCurrentUrl().trim().toString();
	}

	public void verify_login_flow() {

		if (driverUtil.existsElement("//li[contains(@class,'hide-sm')]")) {

			((new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10))))
					.until(ExpectedConditions.visibilityOf(login.user_icon));
			login.getuser_icon().click();

			logger.info("User Name::: {}",
					login.getuser_name().getText().trim().toString().toUpperCase(Locale.ENGLISH));

			/*
			 * logger.error("Login is Failed: {}",
			 * login.geterror_msg_txt().getText().trim().toString().toUpperCase(Locale.
			 * ENGLISH)); logger.error("User entered wrong password");
			 */
		}

		else if (driverUtil.existsElement(
				"//*[contains(text(),'The email address you entered is not registered, Please enter a registered email address')]")) {
			logger.error("Login is Failed: {}",
					login.getinvalidemail_validation_txt().getText().trim().toString().toUpperCase(Locale.ENGLISH));
			logger.info("User entered not registered email");

		}

		else {

			if (driverUtil.existsElement("//h2[contains(text(),'Login')]")) {
				if (login.getlogin_title_txt().getText().equalsIgnoreCase("Support Login"))
					logger.info("user added support email");
			}

		}
	}
}
