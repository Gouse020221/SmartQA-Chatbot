package com.driverUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qa.util.DriverFactory;

public class driverUtil {

	public static WebDriver driver;

	private static final Logger logger = LoggerFactory.getLogger(driverUtil.class);

	public driverUtil() {
		this.driver = DriverFactory.getDriver();
	}

	public static WebElement ElementVisibility(WebElement Element) {

		try {

			if (Element.isDisplayed() && Element.isEnabled()) {

				logger.info("Element is Displayed: " + Element);

			} else {

				logger.error("Elemenet is Not found: " + Element);

			}

		} catch (ElementNotInteractableException e) {

			logger.info("{} Element Not interact: {}", Element, e.getMessage());

		} catch (StaleElementReferenceException ex) {

			logger.info("{} Element Not Attached to the page document, {}", Element, ex.getMessage());

		} catch (NoSuchElementException EX) {

			logger.info(Element + "{} Element was not found in DOM: {}", Element, EX.getStackTrace());

		}
		return Element;

	}

	public static void click_Using_javaScript(WebElement Ele) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", Ele);
	}

	public void Scroll_Using_javaScript(WebElement Element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Element);
	}

	public void ScrolldownToTillPageEnd() {

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript(
				"window.scrollTo(0,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));");
	}

	public static boolean existsElement(String xpath) {
		try {
			driver.findElement(By.xpath(xpath)).isDisplayed();
		} catch (Exception e) {
			logger.warn("locator is not present");
			return false;
		}
		return true;

	}

	public static void VerifyPageStatus() {

		if ((driver.getPageSource().contains("We're sorry, but something went wrong."))
				|| (driver.getPageSource().contains("img[src*='/assets/error/500']"))) {
			logger.error(" The Page is throwing an error message ");
			// driver.navigate().back();
		} else {
			logger.info("The Page is Loaded sucessfully");

		}
	}

	public static void SelectCheckBoxFromList(WebElement Ele, String valueToSelect) {
		// List<WebElement> filterlist =
		// Ele.findElements(By.xpath("//div[@class='legend_data_content']/span[1]"));
		List<WebElement> checkBoxList = Ele.findElements(By.tagName("input"));
		for (int j = 0; j < checkBoxList.size(); j++) {
			if (checkBoxList.get(j).getText().toString().trim().contains(valueToSelect)) {
				WebElement checkbox = checkBoxList.get(j);
				((JavascriptExecutor) driver).executeScript("arguments[0].click()", checkbox);
				logger.info("The Selected Filter option is: [} ", valueToSelect);
				break;
			}
		}
	}

	public void SelectFilter_Website_Preview_Page(WebElement Ele, String FilterName) {
		List<WebElement> filterlist = Ele.findElements(By.xpath("//div[@class='legend_data_content']/span[1]"));
		List<WebElement> checkBoxList = Ele.findElements(By.tagName("input"));
		for (int j = 0; j < filterlist.size(); j++) {
			if (filterlist.get(j).getText().toString().trim().contains(FilterName)) {
				WebElement checkbox = checkBoxList.get(j);
				((JavascriptExecutor) driver).executeScript("arguments[0].click()", checkbox);
				logger.info("The Selected Filter option is: {}", FilterName);
				break;
			}
		}
	}

	public void SwitchToIframe() {
		List<WebElement> iframe_List = driver.findElements(By.tagName("iframe"));
		for (int i = 0; i < iframe_List.size(); i++) {
			logger.info("Total Number of iframe:: {}", iframe_List.size());
			driver.switchTo().frame(0);

		}
	}

	public void Print_Legend_Filters(String locator) {
		WebElement Ele = driver.findElement(By.xpath(locator));
		List<WebElement> filters = Ele.findElements(By.xpath(
				".//*[@class='legend_text floor_plan_txt' or @class='legend_text show_more' or @class='lt_cnt']"));
		for (int i = 0; i < filters.size(); i++) {
			logger.info(filters.get(i).getText().trim().toString());
		}
	}

	public static void SelectCheckBoxFromtheList(WebElement Ele, String valueToSelect) {

		List<WebElement> checkBoxList = Ele.findElements(By.tagName("input"));
		logger.info("Total Number of Checkboxes: {}", checkBoxList.size());
		for (WebElement option : checkBoxList) {
			if (valueToSelect.equalsIgnoreCase(option.getAttribute("value").trim().toString())) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click()", option);
				break;

			}
		}
	}

	public void click(WebElement Ele) {
		if (Ele.isDisplayed()) {
			logger.info("Element Is Loaded:: {}", Ele);
			Ele.click();
		} else {
			logger.error("Element Is Not Loaded:: {} ", Ele);
		}
	}

	public void Readtabledata(String Locator) {

		String Rows = Locator + "/tbody[1]/tr";
		String Columns = Locator + "/tbody[1]/tr[1]/td";

		int rows_size = driver.findElements(By.xpath(Rows)).size();
		logger.info("No of Rows:: {}", rows_size);
		int columns_Size = driver.findElements(By.xpath(Columns)).size();

		String firstpart = "//*[@id='outer_table']/tbody/tr[";
		String secondpart = "]/td[";
		String thirdpart = "]";

		for (int i = 1; i <= rows_size; i++) {
			for (int j = 1; j <= columns_Size; j++) {
				String finalpart = firstpart + i + secondpart + j + thirdpart;
				String Datatable = driver.findElement(By.xpath(finalpart)).getText();
				logger.info(Datatable);
			}
			logger.info("");
			logger.info("");
		}
	}

	public void PrintAllFilters(By locator) {
		WebElement Legend = driver.findElement(locator);
		List<WebElement> filter_items = Legend.findElements(By.xpath(
				"//*[@class = 'legend_text show_more' or @class = 'numPos' or @class = 'legend_text floor_plan_txt' or @class ='legend_data_SalesStatus Sales_Status']"));
		for (int i = 0; i < filter_items.size(); i++) {
			logger.info(filter_items.get(i).getText());

		}

	}

	/* Added by swapna 1/8/2025 */
	public static void CheckDuplicateItems(ArrayList<String> comList) {
		// List<WebElement> menuItems = Element.findElements(by);

		// Create a Set to store unique menu names
		Set<String> menuNamesSet = new HashSet<>();
		boolean hasDuplicates = false;

		// Iterate through the menu items
		for (String item : comList) {
			String menuItemName = item;

			// Check if this menu item name is already in the set
			if (!menuNamesSet.add(menuItemName)) {
				logger.info(" Duplicate found: {} ", menuItemName);
				hasDuplicates = true;
			}
		}

		if (!hasDuplicates) {
			logger.info("No duplicates found.");
		}

	}
	/* Added by swapna 1/8/2025 */

	public void SelectDropdownValues(String Value, WebElement DropDownOption) {

		try {
			List<WebElement> Dropdownvalues = ((new Select(DropDownOption))).getOptions();
			@SuppressWarnings("unused")
			boolean isValid = false;

			for (WebElement Option : Dropdownvalues) {
				logger.info(Option.getText());

				if ((!Option.getText().trim().toString().isEmpty())
						&& (Option.getText().trim().toString().contains(Value))) {
					logger.info("The Drop-Down Options Are:: {}", Option.getText().toString().trim());
					isValid = true;
					break;
				}
			}
			if (isValid = true) {
				if (((new Select(DropDownOption))).getFirstSelectedOption().getText().equalsIgnoreCase(Value)) {
					logger.info("{} already selected", Value);
				} else {
					((new Select(DropDownOption))).selectByVisibleText(Value);
				}

			} else {
				logger.info("DropDown Options Are Not Present");

			}
		} catch (Exception e) {
			logger.error("{} The Option is not present in the dropdown list", Value);
		}
	}

	public static List<String> WebelementToString(List<WebElement> elementList) {
		List<String> stringList = new ArrayList<String>();
		for (WebElement element : elementList) {
			stringList.add(element.getText().toString().trim());
			stringList.sort(String::compareTo);
		}
		return stringList;
	}

	public static void verify_the_date_picker(String targetDate) {
		Calendar calendar = Calendar.getInstance();

		try {
			SimpleDateFormat targetDateFormat = new SimpleDateFormat("dd/MMM/yyyy"); // Corrected year format
			logger.info("Target date format: {}", targetDateFormat);
			targetDateFormat.setLenient(false);

			Date formattedTargetDate = targetDateFormat.parse(targetDate);
			calendar.setTime(formattedTargetDate);

			int targetMonth = calendar.get(Calendar.MONTH);
			int targetYear = calendar.get(Calendar.YEAR);
			int targetDay = calendar.get(Calendar.DAY_OF_MONTH);

			String currentDate = DriverFactory.getDriver().findElement(By.xpath(
					"//div[@class='datepicker']//table//tr//th[@class='picker-switch' and @title ='Select Month']"))
					.getText(); // Example format: "February 2025"

			Calendar currentCalendar = Calendar.getInstance();
			currentCalendar.setTime(new SimpleDateFormat("MMMM yyyy").parse(currentDate));
			int currentMonth = currentCalendar.get(Calendar.MONTH);
			int currentYear = currentCalendar.get(Calendar.YEAR);

			// Navigate to the correct month and year
			while (currentYear < targetYear || (currentYear == targetYear && currentMonth < targetMonth)) {
				DriverFactory.getDriver()
						.findElement(By.xpath("//div[@class='datepicker']//table//tr//th//span[@title='Next Month']"))
						.click();
				currentDate = DriverFactory.getDriver().findElement(By.xpath(
						"//div[@class='datepicker']//table//tr//th[@class='picker-switch' and @title ='Select Month']"))
						.getText();
				currentCalendar.setTime(new SimpleDateFormat("MMMM yyyy").parse(currentDate));
				currentMonth = currentCalendar.get(Calendar.MONTH);
				currentYear = currentCalendar.get(Calendar.YEAR);
			}

			while (currentYear > targetYear || (currentYear == targetYear && currentMonth > targetMonth)) {
				DriverFactory.getDriver()
						.findElement(
								By.xpath("//div[@class='datepicker']//table//tr//th//span[@title='Previous Month']"))
						.click();
				currentDate = DriverFactory.getDriver().findElement(By.xpath(
						"//div[@class='datepicker']//table//tr//th[@class='picker-switch' and @title ='Select Month']"))
						.getText();
				currentCalendar.setTime(new SimpleDateFormat("MMMM yyyy").parse(currentDate));
				currentMonth = currentCalendar.get(Calendar.MONTH);
				currentYear = currentCalendar.get(Calendar.YEAR);
			}

			// Selecting the correct day
			String dayXpath = "//td[not(contains(@class,'old')) and not(contains(@class,'new')) and text()='"
					+ targetDay + "']";
			WebElement dayElement = DriverFactory.getDriver().findElement(By.xpath(dayXpath));
			dayElement.click();

		} catch (ParseException e) {
			logger.warn("Invalid date provided. Please check the input date: {}", targetDate);
			e.printStackTrace();
		}
	}

	public static List<String> CommunitiesList(List<WebElement> Element) {
		List<String> communiieslist = driverUtil.WebelementToString(Element);

		for (String str : communiieslist) {
			logger.info(str);
			// System.err.println(str);

		}
		return communiieslist;
	}
	
	
	public static void selectcommunityfrommultiselectdropdown(WebElement multiselectdropdown, String communityName) {

		multiselectdropdown.click();

		List<WebElement> list = DriverFactory.getDriver().findElements(By.xpath("/html/body/ul/li//label//input"));
		for (int i = 0; i < list.size() + 1; i++) {
			// int i = list.size();
			// logger.info(i);
			List<WebElement> checkboxes = DriverFactory.getDriver()
					.findElements(By.xpath("/html/body/ul/li[" + i + "]//label//input"));
			for (int j = 0; j < checkboxes.size(); j++) {
				logger.info(checkboxes.get(j).getDomProperty("value"));

				if (checkboxes.get(j).getDomAttribute("value").contains(communityName)) {
					checkboxes.get(j).click();
					break;
				}

			}
		}

	}
	
	public static String rgbtohexconverter(String rgb) {
		rgb = rgb.replace("rgb(", "").replace("rgba(", "").replace(")", "").trim();
		String[] rgbValues = rgb.split(",");

		int r = Integer.parseInt(rgbValues[0].trim());
		int g = Integer.parseInt(rgbValues[1].trim());
		int b = Integer.parseInt(rgbValues[2].trim());

		return String.format("#%02X%02X%02X", r, g, b);
	}
	
    public static String toTitleCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char ch : input.toCharArray()) {
            if (ch == ' ') {
                result.append(ch);  // Keep spaces
                capitalizeNext = true;  // Mark next character for capitalization
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(ch)); // Capitalize first letter
                } else {
                    result.append(Character.toLowerCase(ch)); // Keep other letters lowercase
                }
                capitalizeNext = false; // Reset flag
            }
        }

        return result.toString();
    }
}


