package com.examples;

import com.automation.config.AITestDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Example usage of the summarizeIssues() method
 * Demonstrates how to use Gemini API to summarize test execution issues
 */
public class IssueSummaryExample {
    
    private static final Logger logger = LoggerFactory.getLogger(IssueSummaryExample.class);
    
    public static void main(String[] args) {
        // Create AITestDataGenerator instance
        AITestDataGenerator aiGenerator = new AITestDataGenerator();
        
        // Example 1: Simple issue summary
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("Example 1: Basic Issue Summary");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        Map<String, Object> simpleIssues = new HashMap<>();
        simpleIssues.put("totalIssues", 3);
        simpleIssues.put("failedTests", 2);
        simpleIssues.put("errors", 1);
        
        String summary1 = aiGenerator.summarizeIssues(simpleIssues);
        System.out.println("Summary: " + summary1);
        
        // Example 2: Detailed issue summary with list
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("Example 2: Detailed Issue Summary with Error List");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        Map<String, Object> detailedIssues = new HashMap<>();
        detailedIssues.put("totalIssues", 5);
        detailedIssues.put("failedTests", 3);
        detailedIssues.put("errors", 2);
        
        // Create list of specific issues
        List<Map<String, String>> issuesList = new ArrayList<>();
        
        Map<String, String> issue1 = new HashMap<>();
        issue1.put("testName", "Create Valid Buyer #2");
        issue1.put("error", "NoSuchElementException: Unable to locate element: Add Buyer button");
        issue1.put("location", "Manage_Buyer_Brookfield_StepDef.java:356");
        issuesList.add(issue1);
        
        Map<String, String> issue2 = new HashMap<>();
        issue2.put("testName", "Login Functionality");
        issue2.put("error", "TimeoutException: Element not clickable after 20 seconds");
        issue2.put("location", "LoginPageSteps.java:125");
        issuesList.add(issue2);
        
        Map<String, String> issue3 = new HashMap<>();
        issue3.put("testName", "Data Validation Test");
        issue3.put("error", "AssertionError: Expected 'Success' but got 'Error'");
        issue3.put("location", "ValidationSteps.java:89");
        issuesList.add(issue3);
        
        detailedIssues.put("issuesList", issuesList);
        
        String summary2 = aiGenerator.summarizeIssues(detailedIssues);
        System.out.println("Detailed Summary:\n" + summary2);
        
        // Example 3: Real test execution scenario
        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("Example 3: Real Test Execution Scenario");
        System.out.println("═══════════════════════════════════════════════════════════\n");
        
        Map<String, Object> realScenario = createRealTestScenario();
        String summary3 = aiGenerator.summarizeIssues(realScenario);
        System.out.println("Real Scenario Summary:\n" + summary3);
    }
    
    /**
     * Create a realistic test execution scenario with multiple issues
     */
    private static Map<String, Object> createRealTestScenario() {
        Map<String, Object> scenario = new HashMap<>();
        scenario.put("totalIssues", 4);
        scenario.put("failedTests", 3);
        scenario.put("errors", 1);
        
        List<Map<String, String>> issues = new ArrayList<>();
        
        // Issue 1: Timing issue
        Map<String, String> timingIssue = new HashMap<>();
        timingIssue.put("testName", "Create Multiple Buyers");
        timingIssue.put("error", "org.openqa.selenium.NoSuchElementException: no such element: Unable to locate element: {\"method\":\"css selector\",\"selector\":\"#addBuyerBtn\"}");
        timingIssue.put("location", "ManageBuyerBrookfied.feature:23");
        issues.add(timingIssue);
        
        // Issue 2: Stale element
        Map<String, String> staleIssue = new HashMap<>();
        staleIssue.put("testName", "Edit Buyer Information");
        staleIssue.put("error", "org.openqa.selenium.StaleElementReferenceException: stale element reference: element is not attached to the page document");
        staleIssue.put("location", "Manage_Buyer_Brookfield_StepDef.java:245");
        issues.add(staleIssue);
        
        // Issue 3: Validation error
        Map<String, String> validationIssue = new HashMap<>();
        validationIssue.put("testName", "Validate Email Format");
        validationIssue.put("error", "AssertionError: Expected validation message not displayed");
        validationIssue.put("location", "Manage_Buyer_Brookfield_StepDef.java:412");
        issues.add(validationIssue);
        
        // Issue 4: Network timeout
        Map<String, String> networkIssue = new HashMap<>();
        networkIssue.put("testName", "Save Buyer Data");
        networkIssue.put("error", "org.openqa.selenium.TimeoutException: timeout: Timed out receiving message from renderer: 20.000");
        networkIssue.put("location", "Manage_Buyer_Brookfield_StepDef.java:365");
        issues.add(networkIssue);
        
        scenario.put("issuesList", issues);
        
        return scenario;
    }
}

