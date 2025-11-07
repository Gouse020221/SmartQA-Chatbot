package com.runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.FEATURES_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "classpath:BusinessScenarios")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.StepImplementation,com.Hooks")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports/cucumber.html, json:target/cucumber-reports/Cucumber.json, message:target/cucumber-reports/cucumber.txt, com.driverUtility.ReportGeneratorPlugin")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@managebuyerinformation")
public class runnerTest {
}
