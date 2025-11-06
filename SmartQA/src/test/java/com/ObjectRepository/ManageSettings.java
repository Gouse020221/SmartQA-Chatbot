package com.ObjectRepository;

import java.sql.Driver;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.driverUtility.driverUtil;


public class ManageSettings {
	private String Date = "";

	private static final Logger logger = LoggerFactory.getLogger(ManageSettings.class);

	public ManageSettings(WebDriver driver) {
		PageFactory.initElements(driver, this);

	}

	@FindBy(partialLinkText = "Data Refresh")
	private WebElement data_refresh;

	@FindBy(xpath = "//*[@id=\"content\"]/h6/b")
	private WebElement date;

	@FindBy(id = "data_refresh")
	private WebElement Manual_data_refresh;

	@FindBy(xpath = "//h6[@class='text-center']")
	private WebElement last_data_refresh;

	@FindBy(partialLinkText = "Manage Color Schemes")
	private WebElement Manage_Color_Schemes;

	@FindBy(partialLinkText = "Manage Construction Start Date")
	private WebElement Manage_Construction_Start_Date;

	@FindBy(partialLinkText = "Manage Filter")
	public WebElement Manage_Filter;

	@FindBy(partialLinkText = "Back to map")
	private WebElement Back_to_map_lnk;

	@FindBy(partialLinkText = "Manage Roles")
	private WebElement Manage_Roles;

	@FindBy(xpath = "//a[contains(.,'Manage Lot Collections') or contains(.,'View Lot Collections')]")
	private WebElement View_Lot_Collections;

	@FindBy(partialLinkText = "Set Time Zone")
	private WebElement set_time_zone;

	@FindBy(xpath = ".//a[@href='/active_lot_history']")
	private WebElement active_lot_history;

	@FindBy(xpath = "//button[@class='close pull-right']")
	private WebElement Close_btn;

	@FindBy(xpath = "//span[@class='active_lots']")
	private WebElement active_lots;

	@FindBy(partialLinkText = "Back to Lot Collection")
	private WebElement Back_to_Lot_Collection;

	@FindBy(xpath = ".//*[@ng-model='selected_time_zone']")
	private WebElement time_zone;

	@FindBy(xpath = "//tr[1]//td[1]")
	private WebElement current_time_zone;

	@FindBy(partialLinkText = "Save")
	private WebElement save_btn;

	@FindBy(partialLinkText = "Manage Users")
	private WebElement Manage_Users;

	@FindBy(id = "create_user")
	private WebElement create_user;

	@FindBy(partialLinkText = "Edit")
	public WebElement edit_btn;

	@FindBy(partialLinkText = "Manage Warranty Date")
	public WebElement Manage_Warranty_Date;

	// @FindBy(xpath = "//*[@id=\"content\"]/div[2]/div/div[1]")
	// updated by swapna on 20-1-2025
	@FindBy(xpath = "//*[@id=\"content\"]/div[contains(@class,'center-table')]/div/div[1]")
	private WebElement table_title;

	@FindBy(xpath = "//label[@for='lotvue_data']")
	public WebElement lotvue_data;

	@FindBy(xpath = "//label[@for='ihms_data']")
	public WebElement ihms_data;

	@FindBy(id = "manage_warranty_date_number_of_months")
	public WebElement manage_warranty_date_number_of_months_txtbox;

	@FindBy(partialLinkText = "Manage Lot Text Color")
	public WebElement Manage_Lot_Text_Color;

	@FindBy(partialLinkText = "Manage Website Filter")
	public WebElement Manage_Website_Filter;

	@FindBy(partialLinkText = "Embed Codes")
	public WebElement Embed_Codes;

	@FindBy(partialLinkText = "Manage Lot Basis")
	public WebElement manage_Lot_Basis;

	@FindBy(partialLinkText = "Status Mapping")
	public WebElement Status_Mapping;

	@FindBy(partialLinkText = "View Lead Details")
	public WebElement view_Lead_Details;

	@FindBy(partialLinkText = "Sales Price Override")
	public WebElement Sales_Price_Override;

	@FindBy(partialLinkText = "Manage Lot Description")
	public WebElement Manage_Lot_Description;

	@FindBy(partialLinkText = "Manage Estimated Completion Date")
	public WebElement manage_Estimated_Completion_Date;

	@FindBy(partialLinkText = "Show/Hide Future Lots")
	public WebElement Show_Hide_Future_Lots;

	@FindBy(partialLinkText = "Show/Hide Google Maps")
	public WebElement Show_Hide_Google_Maps;

	@FindBy(partialLinkText = "Manage Spec Aging")
	private WebElement Manage_Spec_Aging;

	public WebElement getManage_Spec_Aging() {
		driverUtil.ElementVisibility(Manage_Spec_Aging);
		return Manage_Spec_Aging;
	}

	@FindBy(partialLinkText = "Manage Lot Types")
	public WebElement manage_lot_type;

	@FindBy(partialLinkText = "Manage Lot Fields")
	public WebElement manage_lot_field;

	@FindBy(partialLinkText = "Manage Visitor IPs")
	public WebElement manage_visitor_ips;

	public WebElement getmanage_visitor_ips() {
		driverUtil.ElementVisibility(manage_visitor_ips);
		return manage_visitor_ips;
	}

	public WebElement getmanage_lot_field() {
		driverUtil.ElementVisibility(manage_lot_field);
		return manage_lot_field;
	}

	public WebElement getmanage_lot_type() {
		driverUtil.ElementVisibility(manage_lot_type);
		return manage_lot_type;
	}

	@FindBy(partialLinkText = "Manage Contact Form")
	public WebElement manage_Contact_Form;

	public WebElement getManage_Estimated_Completion_Date() {
		driverUtil.ElementVisibility(manage_Estimated_Completion_Date);
		return manage_Estimated_Completion_Date;
	}

	public WebElement getManage_Contact_Form() {
		driverUtil.ElementVisibility(manage_Contact_Form);
		return manage_Contact_Form;

	}

	public WebElement getShow_Hide_Google_Maps() {
		driverUtil.ElementVisibility(Show_Hide_Google_Maps);
		return Show_Hide_Google_Maps;
	}

	public WebElement getShow_Hide_Future_Lots() {
		driverUtil.ElementVisibility(Show_Hide_Future_Lots);
		return Show_Hide_Future_Lots;
	}

	public WebElement getManage_Lot_Description() {
		driverUtil.ElementVisibility(Manage_Lot_Description);
		return Manage_Lot_Description;
	}

	public WebElement getSales_Price_Override() {
		driverUtil.ElementVisibility(Sales_Price_Override);
		return Sales_Price_Override;
	}

	public WebElement getView_Lead_Details() {
		driverUtil.ElementVisibility(view_Lead_Details);
		return view_Lead_Details;
	}

	public WebElement getStatus_Mapping() {
		driverUtil.ElementVisibility(Status_Mapping);
		return Status_Mapping;
	}

	public WebElement getManage_Lot_Basis() {
		driverUtil.ElementVisibility(manage_Lot_Basis);
		return manage_Lot_Basis;
	}

	public WebElement getManage_Website_Filter() {
		driverUtil.ElementVisibility(Manage_Website_Filter);
		return Manage_Website_Filter;
	}

	public WebElement getManage_Filter() {
		driverUtil.ElementVisibility(Manage_Filter);
		return Manage_Filter;
	}

	public WebElement getManage_Lot_Text_Color() {
		driverUtil.ElementVisibility(Manage_Lot_Text_Color);
		return Manage_Lot_Text_Color;
	}

	public WebElement getEmbed_Codes() {
		driverUtil.ElementVisibility(Embed_Codes);
		return Embed_Codes;
	}

	public WebElement getmanage_warranty_date_number_of_months() {
		driverUtil.ElementVisibility(manage_warranty_date_number_of_months_txtbox);
		return manage_warranty_date_number_of_months_txtbox;
	}

	public WebElement gettable_title() {
		driverUtil.ElementVisibility(table_title);
		return table_title;
	}

	public WebElement getManage_Warranty_Date() {
		driverUtil.ElementVisibility(Manage_Warranty_Date);
		return Manage_Warranty_Date;
	}

	public WebElement getEdit_btn() {
		driverUtil.ElementVisibility(edit_btn);
		return edit_btn;
	}

	public WebElement getcreate_user() {
		driverUtil.ElementVisibility(create_user);
		return create_user;
	}

	public WebElement getManage_Users() {
		driverUtil.ElementVisibility(Manage_Users);
		return Manage_Users;
	}

	public WebElement getsave_btn() {
		driverUtil.ElementVisibility(save_btn);
		return save_btn;
	}

	public WebElement getcurrent_time_zone() {
		driverUtil.ElementVisibility(current_time_zone);
		return current_time_zone;
	}

	public WebElement getset_time_zone() {
		driverUtil.ElementVisibility(set_time_zone);
		return set_time_zone;
	}

	public WebElement gettime_zone() {
		driverUtil.ElementVisibility(time_zone);
		return time_zone;
	}

	public WebElement getBack_to_Lot_Collection() {
		driverUtil.ElementVisibility(Back_to_Lot_Collection);
		return Back_to_Lot_Collection;
	}

	public WebElement getactive_lots() {
		driverUtil.ElementVisibility(active_lots);
		return active_lots;
	}

	public WebElement getactive_lot_history() {
		driverUtil.ElementVisibility(active_lot_history);
		return active_lot_history;
	}

	public WebElement getView_Lot_Collections() {
		driverUtil.ElementVisibility(View_Lot_Collections);
		return View_Lot_Collections;
	}

	public WebElement getManage_Roles() {
		driverUtil.ElementVisibility(Manage_Roles);
		return Manage_Roles;
	}

	public WebElement getBack_to_map_lnk() {
		driverUtil.ElementVisibility(Back_to_map_lnk);
		return Back_to_map_lnk;
	}

	public WebElement getdata_refresh() {
		driverUtil.ElementVisibility(data_refresh);
		return data_refresh;
	}

	public WebElement getdate() {
		driverUtil.ElementVisibility(date);
		return date;
	}

	public WebElement getManual_data_refresh() {
		driverUtil.ElementVisibility(Manual_data_refresh);
		return Manual_data_refresh;
	}

	public WebElement getlast_data_refresh() {
		driverUtil.ElementVisibility(last_data_refresh);
		return last_data_refresh;
	}

	public WebElement getClose_btn() {
		driverUtil.ElementVisibility(Close_btn);
		return Close_btn;
	}

	@FindBy(xpath = "//*[@type='submit' or @id='filter_config']")
	private WebElement Save;

	public WebElement getSave() {
		driverUtil.ElementVisibility(Save);
		return Save;
	}

	@FindBy(id = "clk_Settlement_Date")
	public WebElement Settlement_date;

	@FindBy(id = "clk_Warranty_Date")
	public WebElement Warranty_Date;

	@FindBy(xpath = "//*[@id='clk_view_dom']/table[2]/tbody/tr[12]/td[1]")
	public WebElement Scroll_element;

	public void check_last_dat_refresh_time() {
		try {

			String input_date = getdate().getText().trim().toString();
			SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyy hh:mm:ss");
			Date d = dateformat.parse(input_date);
			Date = dateformat.format(d);
			logger.info("Last Data Refresh Date is : {} ", Date);
			String today = getToday("dd/MM/yyyy hh:mm:ss");
			logger.info("Today's Date is : {}", today);
			if (Date.compareTo(today) > 0) {
				logger.info("Last Date Refresh was occurs after today' date");
			} // compareTo method returns the value greater than 0 if this Date is after the
				// Date argument.
			else if (Date.compareTo(today) < 0) {
				logger.info("Last Date Refresh was occurs before today's date");
			} // compareTo method returns the value less than 0 if this Date is before the
				// Date argument;
			else if (Date.compareTo(today) == 0) {
				logger.info("Both are same dates");
			} // compareTo method returns the value 0 if the argument Date is equal to the
				// second Date;
			else {
				logger.info("You seem to be a time traveller !!");
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	private boolean isExpire(String date) {
		if (date.isEmpty() || date.trim().equals("")) {
			logger.info("no date");
			return false;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"); // Jan-20-2015 1:30:55 PM
			Date d = null;
			Date d1 = null;
			String today = getToday("dd/MM/yyyy hh:mm:ss");

			try {
				logger.info("expdate is : {} ", date);
				logger.info("today : {} ", today);
				d = sdf.parse(date);
				d1 = sdf.parse(today);
				if (d1.compareTo(d) < 0) {// not expired
					System.out.println();
					return false;
				} else if (d.compareTo(d1) == 0) {// both date are same
					if (d.getTime() < d1.getTime()) {// not expired
						return false;
					} else if (d.getTime() == d1.getTime()) {// expired
						return true;
					} else {// expired
						return true;
					}
				} else {// expired
					return true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	private static String getToday(String format) {
		Date date = new Date();

		return new SimpleDateFormat(format).format(date);
	}

	@FindBy(xpath = "//a[@class='handle3']")
	public WebElement Scrollbar;

}
