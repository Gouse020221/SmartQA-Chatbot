package com.ObjectRepository;

import static org.testng.Assert.assertEquals;

import java.time.Duration;
import java.time.Year;
import java.util.NoSuchElementException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.driverUtility.driverUtil;
import com.qa.util.DriverFactory;
import com.qa.util.ProjectStatus;

public class LoginPage {
	private WebDriver driver = DriverFactory.getDriver();
	private ProjectStatus pstatus = new ProjectStatus();

	// private final String Actual_Copyrights_year = "Copyright Â© ECI Software
	// Solutions, Inc. 2023. All rights reserved.";

	// private final String Actual_Copyrights_year = "Copyright © ECI Software
	// Solutions, Inc. 2023. All rights reserved.";

	private final String ECI_Actual_URL = "www.ecisolutions.com/lotvue";
	private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

	public LoginPage(WebDriver driver) {

		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "tbluser_email")
	private WebElement Email_txtbox;

	@FindBy(id = "tbluser_password")
	private WebElement passWord_txtbox;

	@FindBy(id = "signin_submit")
	private WebElement Continue_btn;;

	@FindBy(partialLinkText = "Forgot")
	private WebElement forgotpassword_lnk;

	@FindBy(xpath = "//span[contains(.,'Copyright')]")
	private WebElement Copyright_txt;

	@FindBy(partialLinkText = "ecisolutions")
	private WebElement ECI_url;

	@FindBy(xpath = "//*[@class='logo-margin-padding img-responsive zero-padding header_logo']")
	private WebElement Header_logo;

	@FindBy(xpath = "//form[@id='sign_in_check']//img")
	private WebElement login_header;

	@FindBy(xpath = "//h2[contains(text(),'Login')]")
	private WebElement login_title_txt;

	@FindBy(xpath = "//span[contains(text(),'Enter Your Email')]")
	private WebElement Email_validation_txt;

	@FindBy(xpath = "//span[contains(text(),'Enter Your Password')]")
	private WebElement passWord_validation_txt;

	@FindBy(xpath = "//div[@class='error_msg']")
	public WebElement error_msg_txt;

	@FindBy(xpath = "//span[@class='Pname']")
	private WebElement user_name;

	@FindBy(xpath = "//li[contains(@class,'hide-sm')]")
	public WebElement user_icon;

//	@FindBy(xpath = "//li[@class='dropdown hide-sm open open-submenu']")
//	public WebElement user_icon_open;

	@FindBy(xpath = "//*[@id=\"sign_in_check\"]/img")
	private WebElement template_logo;

	@FindBy(xpath = "//*[contains(text(),'The email address you entered is not registered, Please enter a registered email address')]")
	private WebElement Invalid_Email_validation_txt;

	@FindBy(xpath = "//*[contains(text(),'Have a password') or contains(@class,'support-link-button')]")
	public WebElement have_a_password_link;

	@FindBy(xpath = "//input[@id='signin_submit' and @value = 'Request Password']")
	public WebElement request_password_btn;

	@FindBy(xpath = "//h2[normalize-space()='Customer Login' or normalize-space()='Support Login' or normalize-space()='Insearch Login']")
	public WebElement login_page_title_txt;

	public WebElement gettemplate_logo() {
		driverUtil.ElementVisibility(template_logo);
		return template_logo;
	}

	public WebElement getuser_name() {
		driverUtil.ElementVisibility(user_name);
		return user_name;
	}

	public WebElement getuser_icon() {
		driverUtil.ElementVisibility(user_icon);
		return user_icon;
	}

	public WebElement getEmail_txtbox() {
		driverUtil.ElementVisibility(Email_txtbox);
		return Email_txtbox;

	}

	public WebElement getpassWord_txtbox() {
		driverUtil.ElementVisibility(passWord_txtbox);
		return passWord_txtbox;

	}

	public WebElement getContinue_btn() {
		driverUtil.ElementVisibility(Continue_btn);
		return Continue_btn;

	}

	public WebElement getforgotpassword_lnk() {
		driverUtil.ElementVisibility(forgotpassword_lnk);
		return forgotpassword_lnk;

	}

	public WebElement gethave_a_password_lnk() {
		driverUtil.ElementVisibility(have_a_password_link);
		return have_a_password_link;

	}

	public WebElement getCopyright_txt() {
		driverUtil.ElementVisibility(Copyright_txt);
		return Copyright_txt;

	}

	public WebElement getECI_url() {
		driverUtil.ElementVisibility(ECI_url);
		return ECI_url;

	}

	public WebElement getHeader_logo() {
		driverUtil.ElementVisibility(Header_logo);
		return Header_logo;

	}

	public WebElement getlogin_header() {
		driverUtil.ElementVisibility(login_header);
		return login_header;

	}

	public WebElement getlogin_title_txt() {
		driverUtil.ElementVisibility(login_title_txt);
		return login_title_txt;

	}

	public WebElement getEmail_validation_txt() {
		driverUtil.ElementVisibility(Email_validation_txt);
		return Email_validation_txt;

	}

	public WebElement getpassWord_validation_txt() {
		driverUtil.ElementVisibility(passWord_validation_txt);
		return passWord_validation_txt;

	}

	public WebElement geterror_msg_txt() {
		driverUtil.ElementVisibility(error_msg_txt);
		return error_msg_txt;

	}

	public WebElement getinvalidemail_validation_txt() {
		driverUtil.ElementVisibility(Invalid_Email_validation_txt);
		return Invalid_Email_validation_txt;
	}

	public void Verify_login_functionality() {
		if (driverUtil.existsElement("//div[@class='error_msg']")) {
			logger.error("Login is failed");
			logger.info(error_msg_txt.getText().trim().toString());

		} else {
			if (driverUtil.existsElement("//li[@class='dropdown hide-sm']")) {
				logger.info("logged in successfully");
				((new WebDriverWait(driver, Duration.ofMillis(5))))
						.until(ExpectedConditions.visibilityOf(getuser_icon()));
				getuser_icon().click();
				logger.info("User Name: {}", getuser_name().getText().trim());
				// driverUtil.VerifyPageStatus();
				pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
			}
		}

	}

	public void Verify_support_login_Page_UI() {

		gethave_a_password_lnk();
		assertEquals("Support Login", getlogin_title_txt().getText().trim().toString(), "Both are not matching");

	}

}
