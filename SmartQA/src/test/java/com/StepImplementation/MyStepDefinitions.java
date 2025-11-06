
package com.StepImplementation;

import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.Hooks.Hooks;
import com.ObjectRepository.HomePage;
import com.ObjectRepository.LoginPage;
import com.ObjectRepository.ManageSettings;
import com.Pages.Login_Page_Actions;
import com.driverUtility.driverUtil;
import com.qa.util.DriverFactory;
import com.qa.util.ProjectStatus;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MyStepDefinitions {

	// private Settings_list msettingslist;
	private WebDriver driver;
	private static int Number_of_months;
	private String Downloadpath = "C:\\Users\\gouse";
	private LoginPage login;
	private Login_Page_Actions lpactions;

	private ProjectStatus status;
	private driverUtil utility;

	HomePage home;
	private ProjectStatus pstatus;

	private static final Logger logger = LoggerFactory.getLogger(MyStepDefinitions.class);
	public static HashMap<String, String> MastersettingMap = new HashMap<>();
	public static boolean settingpresence;
	// private ShowHideGoogleMap SHGmap;
	// private ForgotPassword fpassword;

	// private CommunityDataManager cdm;

	// private ManageSettings msettings;

	public MyStepDefinitions() {
		// this.driver = DriverFactory.getDriver();
		this.login = new LoginPage(DriverFactory.getDriver());
		this.driver = DriverFactory.getDriver();
		this.utility = new driverUtil();
		this.lpactions = new Login_Page_Actions();
		// this.SHGmap = new ShowHideGoogleMap(DriverFactory.getDriver());

		this.home = new HomePage(driver);

		// msettings = new ManageSettings(DriverFactory.getDriver());
		pstatus = new ProjectStatus();
		// this.msettingslist = new Settings_list();

	}
	/**/

	@When("^I click on the Login button without entering the login credentials$")
	public void i_click_on_login_button_without_entering_the_login_credentials() {
		if (driver.getCurrentUrl().contains(browserOpen_StepDef.currentURL)) {
			logger.error("Skip");
		}

		else {

			login.getContinue_btn().click();

			pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());

		}
	}

	@Then("^I should see user validation text messages$")
	public void i_should_see_user_validation_text_messages() {
		if (driver.getCurrentUrl().contains(browserOpen_StepDef.currentURL)) {
			logger.error("Skip");
		}

		else {
			login.getpassWord_validation_txt();
		}

	}

	@When("^I click on the product type dropdown list$")
	public void customer_click_on_product_type_dropdown_list() {
		HomePage home = new HomePage(driver);

		home.getproducttype_dropdown().click();

		pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());

	}

	@Then("^I should be able to see the list of product type options$")
	public void customer_should_able_to_see_list_of_product_type() {
		HomePage home = new HomePage(driver);
		home.verify_product_type_dropdownlist();

	}

	@Then("^Select the '(.+)' product type from the list$")
	public void select_operations_option_from_that_list(String Product_Type) {
		home.select_product_type(Product_Type);

	}

	@Then("^I should be able to see the list of MPC$")
	public void i_should_be_able_to_see_the_list_of_MPC() {
		home.verify_MPC_dropdown_list();

	}

	@When("^I clicks on the MPC dropdown list$")
	public void click_on_the_mpc() {
		HomePage home = new HomePage(DriverFactory.getDriver());
		home.getSelect_MPC_dropdown().click();

	}

	@When("^I click on the communities dropdown list$")
	public void customer_click_on_communities_dropdown_list() {
		// HomePage home = new HomePage(DriverFactory.getDriver());
		home.getcommunities_dropdown().click();

	}

	@When("^I click on the manage settings icon$")
	public void customer_click_on_manage_settings_icon() {
		HomePage home = new HomePage(driver);
		try {
			((new WebDriverWait(driver, Duration.ofSeconds(5))))
					.until(ExpectedConditions.elementToBeClickable(home.getsetting_list()));

			home.getsetting_list().click();
			settingpresence = true;
			pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
			// DriverUtil.click_Using_javaScript(home.getsetting_list());
		} catch (Exception e) {
			logger.info("setting option is not showing, check permissions in manage role page");
			settingpresence = false;
		}

	}

	@Then("^I should able to see the list of settings options$")
	public void customer_should_able_to_see_the_list_of_settings_options() {
		if (settingpresence == true) {
			HomePage home = new HomePage(driver);

			List<String> uiSettingsList = new ArrayList<>();
			try {
				List<WebElement> Settingslist = home.Setting_Options_list.findElements(By.tagName("a"));
				for (int i = 0; i < Settingslist.size(); i++) {

					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
							Settingslist.get(i));
					Thread.sleep(1500);

					String settingName = Settingslist.get(i).getText();
					if (!settingName.isEmpty()) {
						uiSettingsList.add(settingName);
						logger.info(" Extracted UI Settings : {}  ", settingName);
					}
				}

				// Step 2: Convert List<String> to String[]
				String[] uiSettingsArray = uiSettingsList.toArray(new String[0]);

				// utility.compareSettings(uiSettingsList, msettingslist.getOperation());

				// MastersettingMap.putAll(settingMap);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}

	}

	@When("I selects the {string} setting option from the list")
	public void check_and_select_setting_option(String Setting_Option) {
		HomePage home = new HomePage(driver);

		try {
			List<WebElement> settingsList = home.Setting_Options_list.findElements(By.tagName("a"));
			// WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			boolean isSettingFound = false;

			for (WebElement setting : settingsList) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", setting);
				// Thread.sleep(1500);

				// Wait until the element is visible
				// wait.until(ExpectedConditions.visibilityOf(setting));

				if (setting.getText().trim().contains(Setting_Option)) {
					// Thread.sleep(1500);
					logger.info("Setting Option '{}' is found. Selecting it...", Setting_Option);
					setting.click();

					// Check the server status
					pstatus.check_if_a_website_server_is_up_or_not(driver.getCurrentUrl());
					logger.info("Selected Setting Option: '{}'", Setting_Option);
					isSettingFound = true;
					break;
				}
			}

			if (!isSettingFound) {
				logger.error("Setting Option '{}' is NOT present in the list for URL: {}", Setting_Option,
						driver.getCurrentUrl());
			}
		} catch (Exception e) {
			logger.error("An error occurred while checking or selecting the setting option: {}", e.getMessage());
		}
	}

//	/* --- Below method updated by shiva 09-01-2025 */
	@Then("^Check wheather setting '(.+)' is present or not$")
	public void check_whether_setting_option_present_or_not_under_the_setting_dropdown(String Setting_Option) {
		HomePage home = new HomePage(driver);

		try {
			List<WebElement> Settingslist = home.Setting_Options_list.findElements(By.tagName("a"));
			boolean isSettingPresent = false;

			for (WebElement setting : Settingslist) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", setting);
				Thread.sleep(500);

				if (setting.getText().trim().contains(Setting_Option)) {
					logger.error("Setting Option: '{}' is present for URL: {}", Setting_Option, driver.getCurrentUrl());
					isSettingPresent = true;
					break;
				}
			}

			if (!isSettingPresent) {
				logger.info("Setting Option: '{}' is NOT present for URL: {}", Setting_Option, driver.getCurrentUrl());
			}

		} catch (Exception e) {
			logger.error("An error occurred while checking for the setting option: {}", e.getMessage());
		}
	}

	@Then("^I should be able to see '(.+)' page with all the information$")
	public void customer_should_able_to_see_data_refresh_page_with_all_the_information(String Page) {
		ManageSettings settings = new ManageSettings(driver);

		// ManageRoles roles = new ManageRoles(driver);
		if (Page.equalsIgnoreCase("Data Refresh")) {

			pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
			settings.getManual_data_refresh();
			settings.getlast_data_refresh();

		}

	}

//

	@When("^I click on lot collection history button$")
	public void customer_click_on_lot_collection_history_button() {
		ManageSettings settings = new ManageSettings(driver);
		settings.getactive_lot_history().click();

		pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
	}

	@Then("^it should redirect to lot collection history page$")
	public void it_should_redirect_to_lot_collection_history_page() {
		((new WebDriverWait(driver, Duration.ofSeconds(5))))
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='content']/div[2]/div/div[1]")));
		assertEquals(driver.getCurrentUrl().contains("active_lot_history"), true);
		logger.info("Redirected to Active Lot collection History Page");
	}

	@When("^I click on select time zone dropdown$")
	public void customer_click_on_select_time_zone_dropdown() {
		ManageSettings settings = new ManageSettings(driver);

		((new WebDriverWait(driver, Duration.ofSeconds(5))))
				.until(ExpectedConditions.visibilityOf(settings.gettime_zone()));
		settings.gettime_zone().click();

		pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());

	}

	@Then("^it should show list of available time zone options$")
	public void it_should_show_list_of_available_time_zone_options() {
		ManageSettings settings = new ManageSettings(driver);
		List<WebElement> dropdown_values = new Select(settings.gettime_zone()).getOptions();
		for (int i = 0; i < dropdown_values.size(); i++) {
			logger.info("Time zone options : {}", dropdown_values.get(i).getText());
		}

	}

	@When("^I click on edit button$")
	public void customer_click_on_edit_button() {

		ManageSettings settings = new ManageSettings(driver);
		settings.getEdit_btn().click();

		pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
		logger.info("Navigated to edit warranty details page");

	}

	@Then("^It should show both IHMS and LotVue radio buttons$")
	public void it_should_show_both_ihms_and_lotvue_radio_options_with_radio_buttons() {
		ManageSettings settings = new ManageSettings(driver);

		if ((settings.ihms_data.isDisplayed() && settings.ihms_data.isEnabled())
				&& (settings.lotvue_data.isDisplayed() && settings.lotvue_data.isEnabled())) {
			logger.info("Both Lotvue & IHMS radio buttons are displayed");

		} else {
			logger.warn("Select Warranty details options are not present");
		}
	}

	@When("^I select '(.+)' radio button$")
	public void customer_select_lotvue_radio_button(String buttonType) {
		ManageSettings settings = new ManageSettings(driver);

		if (buttonType.equalsIgnoreCase("LotVue")) {
			if (!(settings.lotvue_data.isSelected())) {
				logger.info("{} is not selected, user attempting to select the LotVue radio button", buttonType);
				settings.lotvue_data.click();
				logger.info("{} radio button selected successfully.", buttonType);

			} else {
				logger.info("Already {} radio button selected", buttonType);
			}
		} else if (buttonType.equalsIgnoreCase("IHMS")) {
			if (!(settings.ihms_data.isSelected())) {
				logger.info("{} is not selected, user attempting to select the IHMS radio button", buttonType);
				settings.ihms_data.click();
				logger.info("{} radio button selected successfully.", buttonType);
				settings.getSave().click();
				logger.info("User clicked on save button");
			} else {
				logger.info("Already {} radio button selected", buttonType);
			}
		} else {
			logger.error("Invalid radio button option provided: '{}'. Supported options are 'LotVue' or 'IHMS'.",
					buttonType);
		}
	}

	@Then("^I should able to see the enter number of months textbox$")
	public void customer_should_able_to_see_enter_number_of_months_textbox() {
		ManageSettings settings = new ManageSettings(driver);

		try {
			if (settings.manage_warranty_date_number_of_months_txtbox.isDisplayed()) {
				assertTrue(settings.manage_warranty_date_number_of_months_txtbox.isDisplayed(),
						"Enter # of months warranty textbox is present");
				logger.info("'Enter Number of Months' textbox is displayed.");
			} else {
				logger.info("User selected 'IHMS' Warranty option, textbox for number of months not required.");
			}
		} catch (Exception e) {
			logger.warn(
					"'Enter Number of Months' textbox not found in the DOM. Possibly hidden due to 'IHMS' selection.");
		}
	}

	@When("^I enters (\\d+) months as warranty period and save the details$")
	public void customer_enters_months_as_warranty_period(int Months) {
		Number_of_months = Months;
		ManageSettings settings = new ManageSettings(driver);

		try {
			if (settings.manage_warranty_date_number_of_months_txtbox.isDisplayed()) {

				settings.manage_warranty_date_number_of_months_txtbox.clear();
				settings.manage_warranty_date_number_of_months_txtbox.sendKeys(String.valueOf(Months));
				settings.getSave().click();
				logger.info("User clicked on save button");
				logger.info("Please Wait loader is loading");
				((new WebDriverWait(driver, Duration.ofSeconds(50))))
						.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id='ajax_loader_img']")));

				pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
				settings.getBack_to_map_lnk().click();
				logger.info("User clicked on back to map link");

				pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
			} else {
				logger.info("User Selected Warranty 'IHMS' radio button, Not Required to add the no.of months");
			}
		} catch (Exception e) {
			logger.warn(
					"'Enter Number of Months' textbox not found in the DOM. Possibly hidden due to 'IHMS' selection.");
		}
	}

}