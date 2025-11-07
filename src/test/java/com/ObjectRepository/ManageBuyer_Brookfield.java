package com.ObjectRepository;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.qa.util.DriverFactory;

public class ManageBuyer_Brookfield {
	
	DriverFactory driverFactory;

	public ManageBuyer_Brookfield() {
		this.driverFactory = new DriverFactory();
		PageFactory.initElements(driverFactory.getDriver(), this);
	}
	
	@FindBy(xpath="//input[@id='buyer_first_name']")
	public WebElement buyer_firstName;
	
	@FindBy(xpath="//input[@id='buyer_last_name']")
	public WebElement buyer_lastName;
	
	@FindBy(xpath="//input[@id='buyer_email']")
	public WebElement buyer_email;
	
	@FindBy(xpath="//input[@id='buyer_phone']")
	public WebElement buyer_phoneNumber;
	
	@FindBy(xpath="//input[@id='buyer_partner_first_name']")
	public WebElement partner_firstName;
	
	@FindBy(xpath="//input[@id='buyer_partner_last_name']")
	public WebElement partner_lastName;
	
	@FindBy(xpath="//input[@id='buyer_country']")
	public WebElement buyer_Country;
	
	@FindBy(xpath="//input[@id='buyer_zip']")
	public WebElement buyer_zip;
	
	@FindBy(xpath="//input[@id='buyer_address']")
	public WebElement buyer_address;
	
	@FindBy(xpath="//input[@id='buyer_notes']")
	public WebElement buyer_notes;
	
	@FindBy(xpath="//select[@id='buyer_lead_source_of_buyer_id']")
	public WebElement leadSource_dropdown;
	
	@FindBy(xpath="//select[@id='buyer_manage_builder_id']")
	public WebElement builder_dropdown;
 
	@FindBy(xpath="//button[@type='submit']")
	public WebElement saveBtn;
	
	@FindBy(xpath="//a[@id='checkbefore']")
	public WebElement cancelBtn;
	
	@FindBy(xpath="//a[normalize-space()='Add Buyer']")
	public WebElement addbuyer_btn;
	
	@FindBy(xpath="//select[@id='buyer_state']")
	public WebElement state_dropdown;
	
}
