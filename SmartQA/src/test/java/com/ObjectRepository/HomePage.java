package com.ObjectRepository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.driverUtility.driverUtil;
import com.qa.util.DriverFactory;
import com.qa.util.ProjectStatus;

import io.cucumber.datatable.DataTable;

public class HomePage {
	private WebDriver driver = DriverFactory.getDriver();
	private ProjectStatus pstatus = new ProjectStatus();
	private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
	public static String community;
	public static String producttype;
	//private final LotRenderingPage lotRenderingPage = new LotRenderingPage();

	public HomePage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "search-field")
	private WebElement Search_field;

	@FindBy(xpath = "//ul[@id='main_navigation']/li[1]/a")
	private WebElement producttype_dropdown;

	@FindBy(xpath = "//*[@id=\"main_navigation\"]/li[1]/a")
	private WebElement selected_producttype;

	@FindBy(xpath = "//*[@id=\"main_navigation\"]/li[2]/a")
	private WebElement selected_community;

	@FindBy(xpath = "//ul[@id='main_navigation']//li//a[contains(text(),'State')]")
	private WebElement State_dropdown;

	@FindBy(xpath = "//ul[@id='main_navigation']//li//a[contains(text(),'City')]")
	private WebElement city_dropdown;

	@FindBy(xpath = "//li[contains(@class,'communities')]")
	private WebElement communities_dropdown;

	@FindBy(xpath = "//ul[@id='main_navigation']/li[2]")
	private WebElement mpc_dropdown;

	@FindBy(xpath = "//ul[contains(@class,'scrollbox5')]//a")
	public List<WebElement> MPCDropdown_list;

	@FindBy(xpath = "//li[contains(@class,'settingList')]")
	private WebElement setting_list;

	@FindBy(xpath = "//*[@title='Change map style']")
	private WebElement mapstyle_dropdown;

	@FindBy(xpath = "//button[@title='Toggle fullscreen view']")
	private WebElement fullscreen_view_button;

	@FindBy(xpath = "//div[@title='Drag Pegman onto the map to open Street View']")
	private WebElement drag_pegman;

	@FindBy(id = "reset_map")
	private WebElement Resetmap_btn;

	@FindBy(xpath = "//*[@title='Zoom in']")
	private WebElement zoom_in_btn;

	@FindBy(xpath = "//*[@title='Zoom out']")
	private WebElement zoom_out_btn;

	@FindBy(id = "comm_google_map")
	private WebElement Google_map;

	@FindBy(xpath = "//div[@id='comm_google_map']/descendant::div[@class='gmnoprint']")
	private WebElement google_markers;

	@FindBy(id = "search_drp")
	private WebElement search_dropdown;

	@FindBy(xpath = "//span[contains(.,'Search by Address')]")
	private WebElement search_by_address;

	@FindBy(xpath = "//span[contains(.,'Search by Buyer')]")
	private WebElement Search_by_Buyer;

	@FindBy(xpath = "//span[contains(.,'Search by Job Number')]")
	private WebElement Search_by_JobNumber;

	@FindBy(xpath = "//*[@id='main_navigation']/li[1]/ul")
	private WebElement product_type_list;

	@FindBy(xpath = "//ul[contains(@class,'scrollbox5')]//a")
	public List<WebElement> communities_dropdown_list;

	@FindBy(css = "ul[class*='settings_ul']")
	// @FindBy(xpath="//ul[@class='sub-menu sub-menu dropdown-menu pull-right
	// settings_ul scrollbox3']")
	public WebElement Setting_Options_list;

	@FindBy(xpath = ".//*[@href='/tblusers/edit']")
	private WebElement updatepassword_btn;

	@FindBy(partialLinkText = "Sign Out")
	private WebElement signout_btn;

	@FindBy(xpath = "//*[@id='main_navigation']/li[3]/a")
	public WebElement phase_dropdown;

	@FindBy(xpath = "//*[@id='main_navigation']/li[3]/ul")
	public WebElement phase_list;

	public WebElement getsignout_btn() {
		driverUtil.ElementVisibility(signout_btn);
		return signout_btn;
	}

	public WebElement getresetpassword_btn() {
		driverUtil.ElementVisibility(updatepassword_btn);
		return updatepassword_btn;
	}

	public WebElement getSetting_Options_list() {
		driverUtil.ElementVisibility(Setting_Options_list);
		return Setting_Options_list;
	}

	public WebElement getcity_dropdown() {
		driverUtil.ElementVisibility(city_dropdown);
		return city_dropdown;
	}

	public List<WebElement> getcommunities_dropdown_list() {

		return communities_dropdown_list;
	}

	public WebElement getproduct_type_list() {
		driverUtil.ElementVisibility(product_type_list);
		return product_type_list;
	}

	public WebElement getSearch_by_Buyer() {
		driverUtil.ElementVisibility(Search_by_Buyer);
		return Search_by_Buyer;
	}

	public WebElement getsearch_by_address() {
		driverUtil.ElementVisibility(search_by_address);
		return search_by_address;
	}

	public WebElement getsearch_by_jobNumber() {
		driverUtil.ElementVisibility(Search_by_JobNumber);
		return Search_by_JobNumber;
	}

	public WebElement getsearch_dropdown() {
		driverUtil.ElementVisibility(search_dropdown);
		return search_dropdown;
	}

	public WebElement getSearch_field() {
		driverUtil.ElementVisibility(Search_field);
		return Search_field;
	}

	public WebElement getproducttype_dropdown() {
		driverUtil.ElementVisibility(producttype_dropdown);
		return producttype_dropdown;
	}

	public WebElement getcommunities_dropdown() {
		driverUtil.ElementVisibility(communities_dropdown);
		return communities_dropdown;
	}

	public WebElement getmapstyle_dropdown() {
		driverUtil.ElementVisibility(mapstyle_dropdown);
		return mapstyle_dropdown;
	}

	public WebElement getsetting_list() {

		driverUtil.ElementVisibility(setting_list);
		return setting_list;
	}

	public WebElement getfullscreen_view_button() {
		driverUtil.ElementVisibility(fullscreen_view_button);
		return fullscreen_view_button;
	}

	public WebElement getdrag_pegman() {
		driverUtil.ElementVisibility(drag_pegman);
		return drag_pegman;
	}

	public WebElement getResetmap_btn() {
		driverUtil.ElementVisibility(Resetmap_btn);
		return Resetmap_btn;
	}

	public WebElement getzoom_in_btn() {
		driverUtil.ElementVisibility(zoom_in_btn);
		return zoom_in_btn;
	}

	public WebElement getzoom_out_btn() {
		driverUtil.ElementVisibility(zoom_out_btn);
		return zoom_out_btn;
	}

	public WebElement getGoogle_map() {
		driverUtil.ElementVisibility(Google_map);
		return Google_map;
	}

	public WebElement getGoogle_Markers() {
		driverUtil.ElementVisibility(google_markers);
		return google_markers;
	}

	@FindBy(xpath = "//*[@id='ajax_loader_img']")
	public WebElement LoadingLoader;

	@FindBy(xpath = "//*[(local-name()='svg' and @id='svg2') or (@id='svg1' or @id='svg3336')]")
	// @FindBy(xpath = "//*[@id='google_map' or @id='svg2']")
	WebElement MapSVGID;

	//
	@FindBy(xpath = "//*[(local-name()='svg' and @id='svg2') or (@id='svg1' or @id='svg3336')]")
	public WebElement WebPreviewMap_locator;

	@FindBy(xpath = "//*[local-name()='svg']") // *[local-name()='g' and @id='g4858']"*/)
	public WebElement Map;

	/*
	 * public WebElement getMap() { driverUtil.ElementVisibility(Map); return Map; }
	 */

	@FindBy(xpath = "//div[contains(@id,'google_map')]")
	public WebElement Google_Layers;
	/*
	 * public WebElement getGoogleMaps() { driverUtil.ElementVisibility(GoogleMaps);
	 * return GoogleMaps; }
	 */

	public WebElement getLoadingLoader() {
		driverUtil.ElementVisibility(LoadingLoader);
		return LoadingLoader;
	}

	public WebElement getMapSvgID() {
		driverUtil.ElementVisibility(MapSVGID);
		return MapSVGID;
	}

	@FindBy(xpath = "//li[@class='dropdown phases']")
	public WebElement Phasedropdown;

	@FindBy(xpath = "//*[@id='main_navigation']/li[3]/ul")
	public WebElement PhaseDropDownlist;

	@FindBy(xpath = "//*[@id='main_navigation']/li[3]/ul//a")
	private List<WebElement> Phase_dropdown_list;

	public List<WebElement> getPhasedropdown_list() {
		return Phase_dropdown_list;
	}

	public void Click_Address_Buyer_Search() {
		if (Search_field.isDisplayed() && Search_field.isEnabled()) {
			logger.info("The Search icon is displayed");
			getSearch_field().click();
			// driverUtil.VerifyPageStatus();
			pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
			// getsearch_by_address();
			// getSearch_by_Buyer();
		} else {
			logger.error("The Search Icon is not loaded");
		}

	}

	public void verify_product_type_dropdownlist() {
		if (producttype_dropdown.isDisplayed() && producttype_dropdown.isEnabled()) {
			logger.info("The Product type dropdown field is displayed");
			// producttype_dropdown.click();
			// driverUtil.VerifyPageStatus();
			logger.info("Product Type options are::\n" + getproduct_type_list().getText().trim().toString());

		} else {
			logger.error("The Product type dropdown field is not loaded");
		}
	}

	/* Modified by Gouse, 8/28/2024 */
	public void check_communities_sorting_order() {

		((new WebDriverWait(driver, Duration.ofSeconds(20))))
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[contains(@class,'scrollbox5')]")));

		if (!communities_dropdown_list.isEmpty()) {
			// Extract the text from each option and store it in a list
			List<String> actualOrder = new ArrayList<>();
			for (WebElement option : communities_dropdown_list) {
				logger.info(option.getText().trim());
				actualOrder.add(option.getText().trim());

			}

			// Create a copy of the list and sort it
			List<String> sortedOrder = new ArrayList<>(actualOrder);
			Collections.sort(sortedOrder);

			// Compare the actual order with the sorted order
			if (actualOrder.equals(sortedOrder)) {
				logger.info("The dropdown options are in sorted order.");
			} else {
				logger.error("The dropdown options are NOT in sorted order.");
			}
		} else {
			logger.warn("The dropdown is empty. No options to check.");
		}
		/* Added by Gouse, 8/28/2024 */

	}

	public List<String> verify_communities_dropdown_list() {

		List<String> communities = null;

		((new WebDriverWait(driver, Duration.ofSeconds(20))))
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[contains(@class,'scrollbox5')]")));

		if (DriverFactory.getDriver().findElement(By.xpath("//ul[contains(@class,'scrollbox5')]")).isDisplayed()) {

			communities = driverUtil.WebelementToString(communities_dropdown_list);
			for (String str : communities) {
				logger.info(str);
			}

		} else {
			logger.warn("Communities list is not loaded");
		}
		return communities;
	}	
	
	public void verify_city_dropdown_list() {
		if (city_dropdown.isDisplayed()) {
			List<WebElement> name = city_dropdown.findElements(By.xpath("//parent::li/ul/li/a"));
			logger.info("::::The Total Cities are :::");
			for (WebElement i : name) {
				logger.info(i.getText().toString().trim());
			}
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			logger.warn("City list is not loaded");
		}
	}

	public void Verify_Setting_dropdown_list() {

		try {
			((new WebDriverWait(driver, Duration.ofSeconds(15))))
					.until((ExpectedConditions.visibilityOf(Setting_Options_list)));

			if (Setting_Options_list.isDisplayed()) {
				Thread.sleep(3000);
				logger.info("::::: Setting Options are ::\n" + Setting_Options_list.getText().trim().toString());

			} else
				logger.warn("**** Settings list is not loaded*****");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void select_product_type(String producttype) {
		String product_type = getproduct_type_list().getText().toString().trim();
		String s[] = product_type.split("\n");
		boolean found = false;
		for (int i = 0; i < s.length; i++) {
			if (s[i].equalsIgnoreCase(producttype)) {
				logger.info("Selected Product type is: {} .", producttype);
				WebElement Ele = driver.findElement(By.partialLinkText(producttype));
				HomePage home = new HomePage(DriverFactory.getDriver());
				if (!home.getproducttype_dropdown().getText().equalsIgnoreCase(product_type)) {
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", Ele);
				}
				// driverUtil.VerifyPageStatus();
				pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
				found = true;
				break;
			}
		}
		if (!found) {
			logger.error("Invalid Product type/Product type is disabled");
		}
	}

	public void Select_Community(String CommunityName) {

		try {
			for (int i = 0; i < communities_dropdown_list.size(); i++) {
				Thread.sleep(1000);
				((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].scrollIntoView(true);",
						communities_dropdown_list.get(i));

				if (communities_dropdown_list.get(i).getText().toString().equalsIgnoreCase(CommunityName)) {
					logger.info("Selected Community : {}.", CommunityName);
					communities_dropdown_list.get(i).click();
					// driverUtil.VerifyPageStatus();
					pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
					break;

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void Select_MPC(String mpcName) {

		try {
			for (int i = 0; i < MPCDropdown_list.size(); i++) {
				Thread.sleep(1000);
				((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].scrollIntoView(true);",
						MPCDropdown_list.get(i));

				if (MPCDropdown_list.get(i).getText().toString().equalsIgnoreCase(mpcName)) {
					logger.info("Selected MPC : {} .", mpcName);
					MPCDropdown_list.get(i).click();
					// driverUtil.VerifyPageStatus();
					pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
					break;

				}

			}

		} catch (InterruptedException e) {

			logger.info(e.getMessage().toString());
			logger.error("{} : is Not Present in the list", mpcName);
		}

	}

	public void SelectRegion(WebElement ClickOption, String Name, List<WebElement> MainDropDownOption) {

		try {
			int status = 0;
			ClickOption.click();
			// driverUtil.VerifyPageStatus();
			pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());

			for (int i = 0; i < MainDropDownOption.size(); i++) {

				logger.info(MainDropDownOption.get(i).getText());

				if (MainDropDownOption.get(i).getText().contains(Name)) {
					status = 1;
					MainDropDownOption.get(i).click();
					// driverUtil.VerifyPageStatus();
					pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());

					Thread.sleep(2000);

					// break;
				}
			}
			if (status == 0) {
				logger.error(Name + ":: Option Is Not Found");
			}
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

	public void selectcity(String city) {
		List<WebElement> name = city_dropdown.findElements(By.xpath("//parent::li/ul/li/a"));
		int status = 0;
		for (WebElement i : name) {
			if (i.getText().toString().trim().equalsIgnoreCase(city)) {
				i.click();
				// driverUtil.VerifyPageStatus();
				pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
				status = 1;
				break;
			}
		}
		if (status == 0)
			logger.error("{} :option is not present.", city);
	}

	

	public boolean VerifyMapPresence() {
		((new WebDriverWait(driver, Duration.ofMinutes(1)))).until(ExpectedConditions.invisibilityOf(LoadingLoader));

		// ((new WebDriverWait(driver,
		// Duration.ofSeconds(20)))).until(ExpectedConditions.visibilityOf(MapSVGID));
		// *[@id='google_map_div' ]/*[@id='alphasvg']

		boolean flag = VerifyGoogleMapLayers_CommunityMapSVGFile();
		return flag;
	}

	// Wait for Google Map layer (optional)
	public boolean waitForGoogleMapLayer(int timeoutSeconds) {
		try {
			((new WebDriverWait(driver, Duration.ofMinutes(1))))
					.until(ExpectedConditions.invisibilityOf(LoadingLoader));
			((new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(timeoutSeconds)))).until(
					ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@id,'google_map')]")));
			System.out.println("✅ Google Map layer loaded.");
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	private boolean VerifyGoogleMapLayers_CommunityMapSVGFile() {

		try {
			if ((Google_Layers != null && Google_Layers.isDisplayed()) || (Map != null && Map.isDisplayed())
					|| (getMapSvgID() != null && getMapSvgID().isDisplayed())) {
				logger.info("The Google Layers or Community Map (SVG) is loaded");
				return true;
			}
		} catch (Exception e) {
			logger.error("Neither Google Layers nor Community Map (SVG) is present");
			producttype = selected_producttype.getText().toString().trim();
			community = selected_community.getText().toString().trim();
		}
		return false;
	}

	

	public void select_setting_option(String Setting_Option) {
		List<WebElement> Setting_list = Setting_Options_list.findElements(By.tagName("a"));
		for (WebElement list : Setting_list) {
			if (!(list.getText().isEmpty()) && list.getText().equalsIgnoreCase(Setting_Option)) {
				driver.findElement(By.partialLinkText(Setting_Option)).click();
				// driverUtil.VerifyPageStatus();
				pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());

			}

		}

	}

	public void SelectCheckBoxFromList(WebElement Ele, String valueToSelect) {

		List<WebElement> checkBoxList = Ele.findElements(By.tagName("input"));
		logger.info("Total Number of Checkboxes: " + checkBoxList.size());
		for (WebElement option : checkBoxList) {
			if (valueToSelect.equalsIgnoreCase(option.getAttribute("value").trim())) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click()", option);
				logger.info("The Selected Filter Options is: " + option);
				// driverUtil.VerifyPageStatus();
				pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
				break;

			}
		}
	}

	/*
	 * public WebElement getsearchlist() { driverUtil.ElementVisibility(searchlist);
	 * return searchlist;
	 * 
	 * }
	 */

	@FindBy(xpath = "//div[@class='btn-group bootstrap-select form-control show-tick address_r_buyer_search open']//ul[@class='dropdown-menu inner']")
	private WebElement Address_buyer_dropdown_list;

	public WebElement getAddress_buyer_dropdown_list() {
		driverUtil.ElementVisibility(Address_buyer_dropdown_list);
		return Address_buyer_dropdown_list;
	}

	@FindBy(xpath = ".//div[@class='btn-group bootstrap-select form-control show-tick address_r_buyer_search open']//input[@class='form-control']")
	private WebElement Address_Search_txtbox;

	public WebElement getAddress_Search_txtbox() {
		driverUtil.ElementVisibility(Address_Search_txtbox);
		return Address_Search_txtbox;
	}

	@FindBy(xpath = "//*[@id=\"upload_data\"]/div[1]/div[3]/button")
	private WebElement ClickPopup_close_btn;

	public WebElement getClickPopup_close_btn() {
		driverUtil.ElementVisibility(ClickPopup_close_btn);
		return ClickPopup_close_btn;
	}

	@FindBy(xpath = "//*[@id=\"main_navigation\"]/li[2]/ul")
	private WebElement Regionlist;

	public WebElement getRegionlist() {
		driverUtil.ElementVisibility(Regionlist);
		return this.Regionlist;
	}

	public void CheckGoogleMarkers() {
		List<WebElement> Ele = driver
				.findElements(By.xpath("//img[@src='https://maps.gstatic.com/mapfiles/transparent.png']"));
		logger.info("Total Number of community Markers:  " + Ele.size());
		for (int i = 0; i < Ele.size(); i++) {
			((new Actions(driver))).moveToElement(Ele.get(i)).build().perform();
			logger.info("Name of the community Marker :  "
					+ driver.findElement(By.xpath("//div[@class='gm-style-iw-d']")).getText().trim().toString());
			break;
		}

	}

	@FindBy(xpath = "//*[@id='main_navigation']/li[2]/a")
	private WebElement Select_MPC_dropdown;

	public WebElement getSelect_MPC_dropdown() {
		driverUtil.ElementVisibility(Select_MPC_dropdown);
		return Select_MPC_dropdown;
	}

	@FindBy(xpath = "//a[@href='/users/my_profile' and not(contains(@class,' mr-5'))]")
	private WebElement My_Profile_btn;

	/**
	 * @return the my_Profile_btn
	 */
	public WebElement getMy_Profile_btn() {

		driverUtil.ElementVisibility(My_Profile_btn);
		return this.My_Profile_btn;
	}

	public void verify_MPC_dropdown_list() {

		((new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(20))))
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[contains(@class,'scrollbox5')]")));

		if (!MPCDropdown_list.isEmpty()) {

			List<String> actualOrder = new ArrayList<>();
			for (WebElement option : MPCDropdown_list) {
				logger.info(option.getText().trim());
				actualOrder.add(option.getText().trim());

			}

			// Create a copy of the list and sort it
			List<String> sortedOrder = new ArrayList<>(actualOrder);
			Collections.sort(sortedOrder);

			// Compare the actual order with the sorted order
			if (actualOrder.equals(sortedOrder)) {
				logger.info("The MPC dropdown options are in sorted order.");
			} else {
				logger.error("The MPC dropdown options are NOT in sorted order.");
			}
		} else {
			logger.warn("The MPC dropdown is empty. No options to check.");
		}

	}

	
	public void Select_Phase(String Phase) {

		try {
			for (int i = 0; i < Phase_dropdown_list.size(); i++) {
				Thread.sleep(1000);
				((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].scrollIntoView(true);",
						Phase_dropdown_list.get(i));

				if (Phase_dropdown_list.get(i).getText().toString().equalsIgnoreCase(Phase)) {
					logger.info("User Selected Phase :: {} ", Phase);
					Phase_dropdown_list.get(i).click();
					// driverUtil.VerifyPageStatus();
					pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
					break;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Phase Drop-down is not presented for : {} community.",
					getcommunities_dropdown().getText().trim().toString());
		}
	}

	@FindBy(xpath = "//*[@id='header']")
	public WebElement primarynav;

	@FindBy(xpath = "//*[@id='main_nav']")
	public WebElement secnav;

	@FindBy(xpath = "//img[contains(@src,'mapfiles/transparent')]")
	private List<WebElement> googlemarkers;

	public List<WebElement> getgooglemarkers() {
		return googlemarkers;
	}

	
}
