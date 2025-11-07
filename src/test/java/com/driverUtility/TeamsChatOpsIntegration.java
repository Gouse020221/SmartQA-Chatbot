package com.driverUtility;

import com.automation.config.ConfigReader;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * Microsoft Teams ChatOps Integration
 * 
 * Features:
 * 1. Send test execution summaries to Teams channel via webhook
 * 2. Receive commands from Teams bot to trigger Jenkins builds
 * 3. Two-way communication between Teams and Jenkins
 */
public class TeamsChatOpsIntegration {
    private static final Logger logger = LoggerFactory.getLogger(TeamsChatOpsIntegration.class);
    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * Send test execution summary to Microsoft Teams channel
     */
    public static void sendTestReportToTeams(TestSummary summary) {
        try {
            String webhookUrl = ConfigReader.getProperty("teams.webhook.url");
            
            if (webhookUrl == null || webhookUrl.isEmpty() || webhookUrl.equals("your-teams-webhook-url")) {
                logger.warn("Teams webhook URL not configured. Skipping Teams notification.");
                return;
            }

            JsonObject messageCard = createTeamsMessageCard(summary);
            
            RequestBody body = RequestBody.create(
                messageCard.toString(),
                MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(webhookUrl)
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    logger.info("Test report successfully sent to Microsoft Teams");
                } else {
                    logger.error("Failed to send Teams notification. Response code: {}, Message: {}", 
                            response.code(), response.body() != null ? response.body().string() : "No response body");
                }
            }
        } catch (Exception e) {
            logger.error("Error sending test report to Teams: {}", e.getMessage(), e);
        }
    }

    /**
     * Create Teams Adaptive Card with test summary
     */
    private static JsonObject createTeamsMessageCard(TestSummary summary) {
        JsonObject card = new JsonObject();
        card.addProperty("@type", "MessageCard");
        card.addProperty("@context", "https://schema.org/extensions");
        card.addProperty("summary", "Test Execution Summary");
        
        // Theme color based on pass rate
        String themeColor = summary.passRate >= 80 ? "#27ae60" : 
                           summary.passRate >= 50 ? "#f39c12" : "#e74c3c";
        card.addProperty("themeColor", themeColor);
        
        // Title
        card.addProperty("title", "🤖 AI-Powered Test Execution Report");
        
        // Summary text
        String summaryText = String.format(
            "**Total Tests:** %d | **Passed:** %d | **Failed:** %d | **Pass Rate:** %.1f%%",
            summary.totalTests, summary.passed, summary.failed, summary.passRate
        );
        card.addProperty("text", summaryText);
        
        // Sections
        com.google.gson.JsonArray sections = new com.google.gson.JsonArray();
        
        // Main metrics section
        JsonObject metricsSection = new JsonObject();
        metricsSection.addProperty("activityTitle", "📊 Test Metrics");
        metricsSection.addProperty("activitySubtitle", 
            String.format("Execution completed at %s", 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        
        com.google.gson.JsonArray facts = new com.google.gson.JsonArray();
        
        JsonObject fact1 = new JsonObject();
        fact1.addProperty("name", "Total Tests");
        fact1.addProperty("value", String.valueOf(summary.totalTests));
        facts.add(fact1);
        
        JsonObject fact2 = new JsonObject();
        fact2.addProperty("name", "✅ Passed");
        fact2.addProperty("value", String.valueOf(summary.passed));
        facts.add(fact2);
        
        JsonObject fact3 = new JsonObject();
        fact3.addProperty("name", "❌ Failed");
        fact3.addProperty("value", String.valueOf(summary.failed));
        facts.add(fact3);
        
        JsonObject fact4 = new JsonObject();
        fact4.addProperty("name", "⏱️ Duration");
        fact4.addProperty("value", summary.duration);
        facts.add(fact4);
        
        JsonObject fact5 = new JsonObject();
        fact5.addProperty("name", "📈 Pass Rate");
        fact5.addProperty("value", String.format("%.1f%%", summary.passRate));
        facts.add(fact5);
        
        metricsSection.add("facts", facts);
        sections.add(metricsSection);
        
        // Failures section (if any)
        if (summary.failed > 0 && summary.failureDetails != null && !summary.failureDetails.isEmpty()) {
            JsonObject failuresSection = new JsonObject();
            failuresSection.addProperty("activityTitle", "❌ Failures & Root Causes");
            failuresSection.addProperty("text", summary.failureDetails);
            sections.add(failuresSection);
        }
        
        // Recommendations section
        if (summary.recommendations != null && !summary.recommendations.isEmpty()) {
            JsonObject recommendationsSection = new JsonObject();
            recommendationsSection.addProperty("activityTitle", "💡 Recommendations");
            recommendationsSection.addProperty("text", summary.recommendations);
            sections.add(recommendationsSection);
        }
        
        // Action buttons
        com.google.gson.JsonArray potentialAction = new com.google.gson.JsonArray();
        
        // View Comprehensive Report
        JsonObject viewReportAction = new JsonObject();
        viewReportAction.addProperty("@type", "OpenUri");
        viewReportAction.addProperty("name", "📄 View Comprehensive Report");
        com.google.gson.JsonArray targets = new com.google.gson.JsonArray();
        JsonObject target = new JsonObject();
        target.addProperty("os", "default");
        String reportUrl = ConfigReader.getProperty("report.base.url");
        if (reportUrl == null || reportUrl.isEmpty()) {
            reportUrl = "file:///" + System.getProperty("user.dir").replace("\\", "/") + "/target/cucumber-reports/Comprehensive-AI-Report.html";
        } else {
            reportUrl = reportUrl + "/Comprehensive-AI-Report.html";
        }
        target.addProperty("uri", reportUrl);
        targets.add(target);
        viewReportAction.add("targets", targets);
        potentialAction.add(viewReportAction);
        
        // Trigger Jenkins Build (if configured)
        String jenkinsUrl = ConfigReader.getProperty("jenkins.base.url");
        if (jenkinsUrl != null && !jenkinsUrl.isEmpty()) {
            JsonObject triggerBuildAction = new JsonObject();
            triggerBuildAction.addProperty("@type", "HttpPOST");
            triggerBuildAction.addProperty("name", "🚀 Trigger Jenkins Build");
            triggerBuildAction.addProperty("target", jenkinsUrl + "/job/" + 
                ConfigReader.getProperty("jenkins.job.name") + "/build");
            potentialAction.add(triggerBuildAction);
        }
        
        card.add("sections", sections);
        card.add("potentialAction", potentialAction);
        
        return card;
    }

    /**
     * Trigger Jenkins build from Teams command
     * This can be called by a Teams bot webhook endpoint
     */
    public static BuildTriggerResult triggerJenkinsBuild(String jobName, String parameters) {
        try {
            String jenkinsUrl = ConfigReader.getProperty("jenkins.base.url");
            String jenkinsUser = ConfigReader.getProperty("jenkins.username");
            String jenkinsToken = ConfigReader.getProperty("jenkins.api.token");
            
            if (jenkinsUrl == null || jenkinsUrl.isEmpty()) {
                return new BuildTriggerResult(false, "Jenkins URL not configured");
            }
            
            // Build Jenkins API URL
            String buildUrl = jenkinsUrl + "/job/" + jobName + "/build";
            if (parameters != null && !parameters.isEmpty()) {
                buildUrl += "WithParameters?" + parameters;
            }
            
            Request.Builder requestBuilder = new Request.Builder().url(buildUrl);
            
            // Add authentication if provided
            if (jenkinsUser != null && !jenkinsUser.isEmpty() && 
                jenkinsToken != null && !jenkinsToken.isEmpty()) {
                // Create Basic Auth header manually
                String auth = jenkinsUser + ":" + jenkinsToken;
                String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
                requestBuilder.header("Authorization", "Basic " + encodedAuth);
            }
            
            Request request = requestBuilder.post(RequestBody.create("", MediaType.get("application/json")))
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.code() == 201 || response.code() == 200) {
                    String queueUrl = response.header("Location");
                    logger.info("Jenkins build triggered successfully. Queue URL: {}", queueUrl);
                    return new BuildTriggerResult(true, "Build triggered successfully. Queue: " + queueNameFromUrl(queueUrl));
                } else {
                    String errorMsg = response.body() != null ? response.body().string() : "Unknown error";
                    logger.error("Failed to trigger Jenkins build. Response code: {}, Message: {}", 
                            response.code(), errorMsg);
                    return new BuildTriggerResult(false, "Failed to trigger build: " + errorMsg);
                }
            }
        } catch (Exception e) {
            logger.error("Error triggering Jenkins build: {}", e.getMessage(), e);
            return new BuildTriggerResult(false, "Error: " + e.getMessage());
        }
    }

    /**
     * Extract queue name from Jenkins Location header
     */
    private static String queueNameFromUrl(String queueUrl) {
        if (queueUrl == null) return "Unknown";
        try {
            int lastSlash = queueUrl.lastIndexOf('/');
            return lastSlash > 0 ? queueUrl.substring(lastSlash + 1) : queueUrl;
        } catch (Exception e) {
            return queueUrl;
        }
    }

    /**
     * Process Teams bot command and execute appropriate action
     */
    public static String processTeamsCommand(String command, String userId, String channelId) {
        command = command.toLowerCase().trim();
        
        if (command.startsWith("build") || command.startsWith("trigger")) {
            // Extract job name from command: "build job-name" or "trigger job-name"
            String[] parts = command.split("\\s+");
            String jobName = parts.length > 1 ? parts[1] : ConfigReader.getProperty("jenkins.job.name");
            
            if (jobName == null || jobName.isEmpty()) {
                return "❌ Error: Jenkins job name not specified. Use: build <job-name>";
            }
            
            BuildTriggerResult result = triggerJenkinsBuild(jobName, null);
            if (result.success) {
                return "✅ " + result.message + "\n\nJob: " + jobName + "\nTriggered by: " + userId;
            } else {
                return "❌ Failed to trigger build: " + result.message;
            }
        } else if (command.startsWith("status") || command.startsWith("report")) {
            return "📊 To view the latest test report, check the comprehensive report link in the previous notification.";
        } else if (command.startsWith("help")) {
            return "🤖 **Available Commands:**\n\n" +
                   "• `build <job-name>` - Trigger Jenkins build\n" +
                   "• `status` - Get test execution status\n" +
                   "• `help` - Show this help message";
        } else {
            return "❓ Unknown command. Type `help` for available commands.";
        }
    }

    // Inner classes
    public static class TestSummary {
        public int totalTests;
        public int passed;
        public int failed;
        public int errors;
        public double passRate;
        public String duration;
        public String failureDetails;
        public String recommendations;
    }

    public static class BuildTriggerResult {
        public boolean success;
        public String message;

        public BuildTriggerResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}

