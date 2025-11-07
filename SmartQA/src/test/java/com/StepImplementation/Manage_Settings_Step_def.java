package com.StepImplementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ObjectRepository.HomePage;
import com.qa.util.DriverFactory;
import com.qa.util.ProjectStatus;


import io.cucumber.java.en.And;

public class Manage_Settings_Step_def  {

	HomePage home = new HomePage(DriverFactory.getDriver());
	ArrayList originalList = new ArrayList();
	ArrayList tempList = new ArrayList();
	ProjectStatus pstatus = new ProjectStatus();
	
	private static final Logger logger = LoggerFactory.getLogger(Manage_Settings_Step_def.class);

	@And("^The setting options list should be in sorting order$")
	public void Check_Options_Should_be_in_sorting_order() {

		List<WebElement> list = home.getproduct_type_list().findElements(By.tagName("a"));

		for (WebElement Ele : list) {

			originalList.add(Ele.getText().trim());
			tempList.add(Ele.getText().trim());
		}

		Collections.sort(tempList);
		logger.info("This is original List after sort: {}", originalList);

		logger.info("This is original List after sort: {}", tempList);
		if (tempList == originalList) {
			logger.info("Options are in sorting order");

		} else {
			logger.error("Options not sorted");
		}

	}

	@And("I selects the {string} Setting Option from the list")
	public void Select_Setting_Option(String SettingTye) {
		boolean found = false;
		List<WebElement> Options = home.getSetting_Options_list().findElements(By.tagName("a"));
		for (WebElement Ele : Options) {

			if (Ele.getText().trim().toString().equalsIgnoreCase(SettingTye)) {
				found = true;
				Ele.click();
				
				pstatus.check_if_a_website_server_is_up_or_not(DriverFactory.getDriver().getCurrentUrl());
				break;

			}
		}
		
		if(found) {
			logger.info("Value exists");
			
		}else {
			logger.error("Value not exists");
		}

	}
}
//