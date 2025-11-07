package com.runner;

import com.automation.config.AITestDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Quick Test Runner for Issue Summarization
 * Run this to test the Gemini API integration
 */
public class TestIssueSummarization {
    
    private static final Logger logger = LoggerFactory.getLogger(TestIssueSummarization.class);
    
    public static void main(String[] args) {
        logger.info("Starting Issue Summarization Test...");
        
        try {
            // Create AITestDataGenerator instance
            AITestDataGenerator aiGenerator = new AITestDataGenerator();
            
            System.out.println("\n" + "=".repeat(70));
            System.out.println("TESTING GEMINI API - ISSUE SUMMARIZATION");
            System.out.println("=".repeat(70) + "\n");
            
            // Test 1: Simple Issue Summary
            testSimpleIssueSummary(aiGenerator);
            
            // Test 2: Detailed Issue Summary
            testDetailedIssueSummary(aiGenerator);
            
            System.out.println("\n" + "=".repeat(70));
            System.out.println("ALL TESTS COMPLETED");
            System.out.println("=".repeat(70) + "\n");
            
        } catch (Exception e) {
            logger.error("Error during test execution: {}", e.getMessage(), e);
            System.err.println("Test failed: " + e.getMessage());
        }
    }
    
    /**
     * Test 1: Simple issue summary with basic counts
     */
    private static void testSimpleIssueSummary(AITestDataGenerator aiGenerator) {
        System.out.println("TEST 1: Simple Issue Summary");
        System.out.println("-".repeat(70));
        
        Map<String, Object> simpleIssues = new HashMap<>();
        simpleIssues.put("totalIssues", 3);
        simpleIssues.put("failedTests", 2);
        simpleIssues.put("errors", 1);
        
        System.out.println("\nInput Data:");
        System.out.println("  Total Issues: 3");
        System.out.println("  Failed Tests: 2");
        System.out.println("  Errors: 1");
        
        System.out.println("\nCalling Gemini API...");
        long startTime = System.currentTimeMillis();
        
        String summary = aiGenerator.summarizeIssues(simpleIssues);
        
        long duration = System.currentTimeMillis() - startTime;
        
        System.out.println("\nAPI Response (took " + duration + "ms):");
        System.out.println("-".repeat(70));
        System.out.println(summary);
        System.out.println("-".repeat(70));
        
        if (summary.contains("Failed to generate")) {
            System.out.println("\n⚠️  WARNING: API call failed. Check your API key configuration.");
        } else {
            System.out.println("\n✅ Test 1 PASSED - Summary generated successfully!");
        }
        System.out.println("\n");
    }
    
    /**
     * Test 2: Detailed issue summary with error list
     */
    private static void testDetailedIssueSummary(AITestDataGenerator aiGenerator) {
        System.out.println("TEST 2: Detailed Issue Summary with Error List");
        System.out.println("-".repeat(70));
        
        Map<String, Object> detailedIssues = new HashMap<>();
        detailedIssues.put("totalIssues", 4);
        detailedIssues.put("failedTests", 3);
        detailedIssues.put("errors", 1);
        
        // Create realistic test failures from your actual project
        List<Map<String, String>> issuesList = new ArrayList<>();
        
        Map<String, String> issue1 = new HashMap<>();
        issue1.put("testName", "Create Valid Buyer #2");
        issue1.put("error", "org.openqa.selenium.NoSuchElementException: no such element: Unable to locate element: Add Buyer button");
        issue1.put("location", "ManageBuyerBrookfied.feature:23");
        issuesList.add(issue1);
        
        Map<String, String> issue2 = new HashMap<>();
        issue2.put("testName", "Edit Buyer Information");
        issue2.put("error", "org.openqa.selenium.StaleElementReferenceException: stale element reference: element is not attached to the page document");
        issue2.put("location", "Manage_Buyer_Brookfield_StepDef.java:245");
        issuesList.add(issue2);
        
        Map<String, String> issue3 = new HashMap<>();
        issue3.put("testName", "Save Buyer Data");
        issue3.put("error", "org.openqa.selenium.TimeoutException: timeout: Timed out receiving message from renderer: 20.000");
        issue3.put("location", "Manage_Buyer_Brookfield_StepDef.java:365");
        issuesList.add(issue3);
        
        detailedIssues.put("issuesList", issuesList);
        
        System.out.println("\nInput Data:");
        System.out.println("  Total Issues: 4");
        System.out.println("  Failed Tests: 3");
        System.out.println("  Errors: 1");
        System.out.println("\n  Issue Details:");
        for (int i = 0; i < issuesList.size(); i++) {
            Map<String, String> issue = issuesList.get(i);
            System.out.println("    " + (i+1) + ". " + issue.get("testName"));
            System.out.println("       Error: " + issue.get("error").substring(0, Math.min(80, issue.get("error").length())) + "...");
        }
        
        System.out.println("\nCalling Gemini API with detailed data...");
        long startTime = System.currentTimeMillis();
        
        String summary = aiGenerator.summarizeIssues(detailedIssues);
        
        long duration = System.currentTimeMillis() - startTime;
        
        System.out.println("\nAPI Response (took " + duration + "ms):");
        System.out.println("-".repeat(70));
        System.out.println(summary);
        System.out.println("-".repeat(70));
        
        if (summary.contains("Failed to generate")) {
            System.out.println("\n⚠️  WARNING: API call failed. Check your API key configuration.");
            System.out.println("\nTroubleshooting:");
            System.out.println("  1. Verify API key in config.properties");
            System.out.println("  2. Check if Generative Language API is enabled");
            System.out.println("  3. Ensure API key has no IP restrictions");
        } else {
            System.out.println("\n✅ Test 2 PASSED - Detailed summary generated successfully!");
        }
        System.out.println("\n");
    }
}

