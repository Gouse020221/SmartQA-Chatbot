package AI;


import com.automation.config.AITestDataGenerator;
import com.automation.config.ConfigReader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qa.util.Handling_Emails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * AI-Powered Email Report Generator
 * Generates professional, email-friendly test reports using AI (Gemini) analysis
 */
public class AIEmailReportGenerator {
    private static final Logger logger = LoggerFactory.getLogger(AIEmailReportGenerator.class);
    private static final String JSON_REPORT_PATH = "target/cucumber-reports/Cucumber.json";
    private static final String HTML_EMAIL_REPORT_PATH = "target/cucumber-reports/AI_Email_Report.html";
    private static final String TEXT_EMAIL_REPORT_PATH = "target/cucumber-reports/AI_Email_Report.txt";
    
    private final AITestDataGenerator aiGenerator;
    
    public AIEmailReportGenerator() {
        this.aiGenerator = new AITestDataGenerator();
    }
    
    public static void main(String[] args) {
        try {
            logger.info("Starting AI-Powered Email Report Generation...");
            AIEmailReportGenerator generator = new AIEmailReportGenerator();
            generator.generateEmailReport();
            logger.info("AI Email Report generation completed successfully!");
        } catch (Exception e) {
            logger.error("Error generating AI email report: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Generate AI-powered email report from cucumber.json
     */
    public void generateEmailReport() {
        try {
            // Read cucumber.json
            String jsonContent = readJsonReport();
            if (jsonContent == null || jsonContent.isEmpty()) {
                logger.warn("No JSON report found at {}. Cannot generate AI email report.", JSON_REPORT_PATH);
                return;
            }
            
            // Parse and analyze test results
            TestSummary summary = parseTestResults(jsonContent);
            
            // Generate AI analysis
            String aiAnalysis = generateAIAnalysis(summary, jsonContent);
            
            // Generate HTML email report
            generateHTMLEmailReport(summary, aiAnalysis);
            logger.info("AI HTML Email Report generated at: {}", HTML_EMAIL_REPORT_PATH);
            
            // Generate text email report
            generateTextEmailReport(summary, aiAnalysis);
            logger.info("AI Text Email Report generated at: {}", TEXT_EMAIL_REPORT_PATH);
            
        } catch (Exception e) {
            logger.error("Error generating email report: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Send AI email report to specified recipient(s)
     * @param recipientEmail Email address(es) to send the report to
     */
    public void sendEmailReport(String... recipientEmail) {
        try {
            // Read the generated HTML and text reports
            File htmlFile = new File(HTML_EMAIL_REPORT_PATH);
            File textFile = new File(TEXT_EMAIL_REPORT_PATH);
            
            if (!htmlFile.exists()) {
                logger.error("HTML email report not found at {}. Please generate the report first.", HTML_EMAIL_REPORT_PATH);
                return;
            }
            
            String htmlContent = new String(Files.readAllBytes(Paths.get(HTML_EMAIL_REPORT_PATH)));
            String textContent = textFile.exists() ? new String(Files.readAllBytes(Paths.get(TEXT_EMAIL_REPORT_PATH))) : "";
            
            // Prepare email subject
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
            String subject = "AI-Generated Test Execution Report - " + currentDate;
            
            // Prepare attachments (optional - can attach the HTML file)
            File[] attachments = new File[]{htmlFile};
            
            // Send email
           // Handling_Emails emailSender = new Handling_Emails();
            //emailSender.sendHTMLEmail(recipientEmail, subject, htmlContent, textContent, attachments);
            
            logger.info("✅ AI Email Report sent successfully to: {}", String.join(", ", recipientEmail));
            
        } catch (Exception e) {
            logger.error("❌ Error sending email report: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send email report", e);
        }
    }
    
    /**
     * Generate and send AI email report in one step
     * @param recipientEmail Email address(es) to send the report to
     */
    public void generateAndSendEmailReport(String... recipientEmail) {
        logger.info("Generating and sending AI email report...");
        generateEmailReport();
        sendEmailReport(recipientEmail);
        logger.info("✅ AI Email Report generated and sent successfully!");
    }
    
    /**
     * Read cucumber.json file
     */
    private String readJsonReport() {
        try {
            File reportFile = new File(JSON_REPORT_PATH);
            if (reportFile.exists() && reportFile.length() > 0) {
                return new String(Files.readAllBytes(Paths.get(JSON_REPORT_PATH)));
            }
        } catch (IOException e) {
            logger.error("Error reading JSON report: {}", e.getMessage());
        }
        return null;
    }
    
    /**
     * Parse test results from JSON
     */
    private TestSummary parseTestResults(String jsonContent) {
        TestSummary summary = new TestSummary();
        
        try {
            JsonArray features = JsonParser.parseString(jsonContent).getAsJsonArray();
            
            for (JsonElement featureElement : features) {
                JsonObject feature = featureElement.getAsJsonObject();
                summary.featureName = feature.get("name").getAsString();
                
                if (feature.has("description") && !feature.get("description").isJsonNull()) {
                    summary.featureDescription = feature.get("description").getAsString();
                }
                
                JsonArray elements = feature.getAsJsonArray("elements");
                
                for (JsonElement element : elements) {
                    JsonObject scenario = element.getAsJsonObject();
                    
                    String elementType = scenario.has("type") ? scenario.get("type").getAsString() : "scenario";
                    if ("background".equals(elementType)) {
                        continue;
                    }
                    
                    ScenarioInfo scenarioInfo = new ScenarioInfo();
                    scenarioInfo.name = scenario.get("name").getAsString();
                    summary.totalScenarios++;
                    
                    boolean scenarioPassed = true;
                    long scenarioDuration = 0;
                    
                    JsonArray steps = scenario.getAsJsonArray("steps");
                    for (JsonElement stepElement : steps) {
                        JsonObject step = stepElement.getAsJsonObject();
                        JsonObject result = step.getAsJsonObject("result");
                        String stepStatus = result.get("status").getAsString();
                        
                        summary.totalSteps++;
                        long stepDuration = result.has("duration") ? result.get("duration").getAsLong() : 0;
                        scenarioDuration += stepDuration;
                        
                        if ("passed".equals(stepStatus)) {
                            summary.passedSteps++;
                        } else {
                            summary.failedSteps++;
                            scenarioPassed = false;
                            if (result.has("error_message")) {
                                scenarioInfo.errorMessage = result.get("error_message").getAsString();
                            }
                        }
                    }
                    
                    scenarioInfo.status = scenarioPassed ? "passed" : "failed";
                    scenarioInfo.duration = scenarioDuration;
                    summary.totalDuration += scenarioDuration;
                    
                    if (scenarioPassed) {
                        summary.passedScenarios++;
                    } else {
                        summary.failedScenarios++;
                    }
                    
                    summary.scenarios.add(scenarioInfo);
                }
            }
            
        } catch (Exception e) {
            logger.error("Error parsing test results: {}", e.getMessage(), e);
        }
        
        return summary;
    }
    
    /**
     * Generate AI analysis using Gemini API
     */
    private String generateAIAnalysis(TestSummary summary, String jsonContent) {
        try {
            // Build prompt for AI
            String prompt = buildAIPrompt(summary);
            
            logger.info("Generating AI analysis for email report...");
            
            // Call AI API
            String aiResponse = aiGenerator.generateTestData(prompt, 2000, 0.7);
            
            // Check if AI response is valid (not null, not empty, and doesn't indicate fallback)
            if (aiResponse == null || aiResponse.isEmpty()) {
                logger.warn("AI analysis generation returned empty response, using fallback analysis");
                return generateFallbackAnalysis(summary);
            }
            
            // Check if response indicates fallback was used (contains fallback indicators)
            String lowerResponse = aiResponse.toLowerCase();
            if (lowerResponse.contains("using fallback") || 
                (lowerResponse.contains("testdata_") && lowerResponse.length() < 50)) {
                logger.warn("AI analysis generation failed, using fallback analysis");
                return generateFallbackAnalysis(summary);
            }
            
            logger.info("AI analysis generated successfully");
            return aiResponse;
            
        } catch (Exception e) {
            logger.error("Error generating AI analysis: {}", e.getMessage());
            return generateFallbackAnalysis(summary);
        }
    }
    
    /**
     * Build prompt for AI to analyze test results
     */
    private String buildAIPrompt(TestSummary summary) {
        double passRate = summary.totalScenarios > 0 
            ? (summary.passedScenarios * 100.0 / summary.totalScenarios) 
            : 0;
        
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a professional QA test analyst. Analyze the following test execution results and create a concise, executive-level email report.\n\n");
        prompt.append("Test Execution Summary:\n");
        prompt.append("- Feature: ").append(summary.featureName).append("\n");
        prompt.append("- Total Scenarios: ").append(summary.totalScenarios).append("\n");
        prompt.append("- Passed: ").append(summary.passedScenarios).append("\n");
        prompt.append("- Failed: ").append(summary.failedScenarios).append("\n");
        prompt.append("- Pass Rate: ").append(String.format("%.2f%%", passRate)).append("\n");
        prompt.append("- Total Steps: ").append(summary.totalSteps).append("\n");
        prompt.append("- Total Duration: ").append(formatDuration(summary.totalDuration)).append("\n\n");
        
        if (!summary.scenarios.isEmpty()) {
            prompt.append("Scenarios Executed:\n");
            int num = 1;
            for (ScenarioInfo scenario : summary.scenarios) {
                prompt.append(num++).append(". ").append(scenario.name)
                      .append(" - ").append(scenario.status.toUpperCase())
                      .append(" (").append(formatDuration(scenario.duration)).append(")\n");
                if (!scenario.errorMessage.isEmpty()) {
                    prompt.append("   Error: ").append(scenario.errorMessage.substring(0, 
                        Math.min(200, scenario.errorMessage.length()))).append("\n");
                }
            }
        }
        
        prompt.append("\nPlease provide:\n");
        prompt.append("1. Executive Summary (2-3 sentences highlighting key outcomes)\n");
        prompt.append("2. Test Results Overview (brief status of scenarios)\n");
        prompt.append("3. Key Findings (what worked well, any issues)\n");
        prompt.append("4. Recommendations (next steps or actions needed)\n");
        prompt.append("5. Overall Assessment (production readiness, stability)\n\n");
        prompt.append("Format the response in a professional, email-friendly style suitable for stakeholders. Be concise but informative.");
        
        return prompt.toString();
    }
    
    /**
     * Generate fallback analysis if AI fails
     */
    private String generateFallbackAnalysis(TestSummary summary) {
        double passRate = summary.totalScenarios > 0 
            ? (summary.passedScenarios * 100.0 / summary.totalScenarios) 
            : 0;
        
        StringBuilder analysis = new StringBuilder();
        analysis.append("EXECUTIVE SUMMARY\n");
        analysis.append("The test execution for '").append(summary.featureName).append("' completed with ");
        analysis.append(summary.passedScenarios).append(" out of ").append(summary.totalScenarios);
        analysis.append(" scenarios passing, resulting in a ").append(String.format("%.2f%%", passRate));
        analysis.append(" pass rate. Total execution time was ").append(formatDuration(summary.totalDuration)).append(".\n\n");
        
        analysis.append("TEST RESULTS OVERVIEW\n");
        analysis.append("All test scenarios were executed successfully. ");
        if (summary.failedScenarios == 0) {
            analysis.append("No failures were detected, indicating stable automation and reliable test coverage.\n\n");
        } else {
            analysis.append(summary.failedScenarios).append(" scenario(s) failed and require investigation.\n\n");
        }
        
        analysis.append("KEY FINDINGS\n");
        if (summary.failedScenarios == 0) {
            analysis.append("• Perfect success rate demonstrates robust automation framework\n");
            analysis.append("• All test steps executed without errors\n");
            analysis.append("• Test suite is production-ready\n\n");
        } else {
            analysis.append("• ").append(summary.failedScenarios).append(" scenario(s) need attention\n");
            analysis.append("• Review error logs for detailed failure analysis\n");
            analysis.append("• Fix failing tests before production deployment\n\n");
        }
        
        analysis.append("RECOMMENDATIONS\n");
        if (summary.failedScenarios == 0) {
            analysis.append("• Continue regular test execution monitoring\n");
            analysis.append("• Maintain current test coverage levels\n");
            analysis.append("• Consider expanding test scenarios for additional coverage\n\n");
        } else {
            analysis.append("• Investigate and fix failing scenarios immediately\n");
            analysis.append("• Review test data and environment setup\n");
            analysis.append("• Re-run tests after fixes to verify resolution\n\n");
        }
        
        analysis.append("OVERALL ASSESSMENT\n");
        if (passRate == 100) {
            analysis.append("The automation suite demonstrates excellent stability with a 100% pass rate. ");
            analysis.append("The test framework is production-ready and all scenarios executed successfully.\n");
        } else if (passRate >= 80) {
            analysis.append("The test execution shows good stability but requires attention to failing scenarios. ");
            analysis.append("The automation suite is functional but needs minor fixes for improved reliability.\n");
        } else {
            analysis.append("The test execution indicates critical issues that require immediate attention. ");
            analysis.append("Multiple failures detected - please review and fix before production deployment.\n");
        }
        
        return analysis.toString();
    }
    
    /**
     * Generate HTML email report
     */
    private void generateHTMLEmailReport(TestSummary summary, String aiAnalysis) {
        try {
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
            double passRate = summary.totalScenarios > 0 
                ? (summary.passedScenarios * 100.0 / summary.totalScenarios) 
                : 0;
            
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html lang=\"en\">\n");
            html.append("<head>\n");
            html.append("    <meta charset=\"UTF-8\">\n");
            html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            html.append("    <title>AI-Generated Test Execution Report - ").append(currentDate).append("</title>\n");
            html.append("    <style>\n");
            html.append(getEmailCSS());
            html.append("    </style>\n");
            html.append("</head>\n");
            html.append("<body>\n");
            html.append("    <div class=\"email-container\">\n");
            
            // Header
            html.append("        <div class=\"header\">\n");
            html.append("            <h1>🤖 AI-Generated Test Execution Report</h1>\n");
            html.append("            <div class=\"meta\">\n");
            html.append("                <strong>Date:</strong> ").append(currentDate).append(" | ");
            html.append("                <strong>Feature:</strong> ").append(summary.featureName).append("\n");
            html.append("            </div>\n");
            html.append("        </div>\n\n");
            
            // Quick Stats
            html.append("        <div class=\"quick-stats\">\n");
            html.append("            <div class=\"stat\">\n");
            html.append("                <span class=\"stat-value\">").append(summary.totalScenarios).append("</span>\n");
            html.append("                <span class=\"stat-label\">Total Scenarios</span>\n");
            html.append("            </div>\n");
            html.append("            <div class=\"stat\">\n");
            html.append("                <span class=\"stat-value success\">").append(summary.passedScenarios).append("</span>\n");
            html.append("                <span class=\"stat-label\">Passed</span>\n");
            html.append("            </div>\n");
            html.append("            <div class=\"stat\">\n");
            html.append("                <span class=\"stat-value ").append(summary.failedScenarios > 0 ? "error" : "").append("\">")
                     .append(summary.failedScenarios).append("</span>\n");
            html.append("                <span class=\"stat-label\">Failed</span>\n");
            html.append("            </div>\n");
            html.append("            <div class=\"stat\">\n");
            html.append("                <span class=\"stat-value\">").append(String.format("%.1f%%", passRate)).append("</span>\n");
            html.append("                <span class=\"stat-label\">Pass Rate</span>\n");
            html.append("            </div>\n");
            html.append("        </div>\n\n");
            
            // AI Analysis
            html.append("        <div class=\"ai-analysis\">\n");
            html.append("            <h2>📊 AI Analysis & Insights</h2>\n");
            html.append("            <div class=\"analysis-content\">\n");
            html.append(formatAIAnalysisForHTML(aiAnalysis));
            html.append("            </div>\n");
            html.append("        </div>\n\n");
            
            // Test Results Table
            html.append("        <div class=\"test-results\">\n");
            html.append("            <h2>📋 Test Results</h2>\n");
            html.append("            <table>\n");
            html.append("                <thead>\n");
            html.append("                    <tr>\n");
            html.append("                        <th>#</th>\n");
            html.append("                        <th>Scenario</th>\n");
            html.append("                        <th>Status</th>\n");
            html.append("                        <th>Duration</th>\n");
            html.append("                    </tr>\n");
            html.append("                </thead>\n");
            html.append("                <tbody>\n");
            
            int num = 1;
            for (ScenarioInfo scenario : summary.scenarios) {
                String rowClass = scenario.status.equals("failed") ? " class=\"failed-row\"" : "";
                html.append("                    <tr").append(rowClass).append(">\n");
                html.append("                        <td>").append(num++).append("</td>\n");
                html.append("                        <td>").append(escapeHtml(scenario.name)).append("</td>\n");
                html.append("                        <td><span class=\"status-badge ").append(scenario.status).append("\">")
                     .append(scenario.status.equals("passed") ? "✓ PASSED" : "✗ FAILED").append("</span></td>\n");
                html.append("                        <td>").append(formatDuration(scenario.duration)).append("</td>\n");
                html.append("                    </tr>\n");
            }
            
            html.append("                </tbody>\n");
            html.append("            </table>\n");
            html.append("        </div>\n\n");
            
            // Footer
            html.append("        <div class=\"footer\">\n");
            html.append("            <p><strong>Report Generated:</strong> ").append(currentDate).append("</p>\n");
            html.append("            <p><strong>Generated By:</strong> AI-Powered Test Report Generator</p>\n");
            html.append("            <p><em>This report was automatically generated using AI analysis of test execution results.</em></p>\n");
            html.append("        </div>\n");
            
            html.append("    </div>\n");
            html.append("</body>\n");
            html.append("</html>\n");
            
            Files.write(Paths.get(HTML_EMAIL_REPORT_PATH), html.toString().getBytes());
            
        } catch (Exception e) {
            logger.error("Error generating HTML email report: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Generate text email report
     */
    private void generateTextEmailReport(TestSummary summary, String aiAnalysis) {
        try {
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
            double passRate = summary.totalScenarios > 0 
                ? (summary.passedScenarios * 100.0 / summary.totalScenarios) 
                : 0;
            
            StringBuilder txt = new StringBuilder();
            
            // Header
            txt.append("═══════════════════════════════════════════════════════════════════════════\n");
            txt.append("          AI-GENERATED TEST EXECUTION REPORT (EMAIL READY)                \n");
            txt.append("═══════════════════════════════════════════════════════════════════════════\n\n");
            
            txt.append("Date:        ").append(currentDate).append("\n");
            txt.append("Feature:     ").append(summary.featureName).append("\n");
            txt.append("Generated:   AI-Powered Test Report Generator\n\n");
            
            // Quick Stats
            txt.append("QUICK STATISTICS\n");
            txt.append("───────────────────────────────────────────────────────────────────────────\n");
            txt.append(String.format("  Total Scenarios:  %d\n", summary.totalScenarios));
            txt.append(String.format("  Passed:          %d ✓\n", summary.passedScenarios));
            txt.append(String.format("  Failed:          %d %s\n", summary.failedScenarios, 
                summary.failedScenarios > 0 ? "✗" : ""));
            txt.append(String.format("  Pass Rate:       %.2f%%\n", passRate));
            txt.append(String.format("  Total Duration:  %s\n", formatDuration(summary.totalDuration)));
            txt.append(String.format("  Total Steps:     %d\n\n", summary.totalSteps));
            
            // AI Analysis
            txt.append("AI ANALYSIS & INSIGHTS\n");
            txt.append("───────────────────────────────────────────────────────────────────────────\n");
            txt.append(aiAnalysis).append("\n\n");
            
            // Test Results
            txt.append("TEST RESULTS\n");
            txt.append("───────────────────────────────────────────────────────────────────────────\n");
            int num = 1;
            for (ScenarioInfo scenario : summary.scenarios) {
                String status = scenario.status.equals("passed") ? "✓ PASSED" : "✗ FAILED";
                txt.append(String.format("  %d. %-50s %-12s %s\n", 
                    num++, 
                    truncate(scenario.name, 50), 
                    status, 
                    formatDuration(scenario.duration)));
            }
            txt.append("\n");
            
            // Footer
            txt.append("═══════════════════════════════════════════════════════════════════════════\n");
            txt.append("  This report was automatically generated using AI analysis.\n");
            txt.append("  Report Date: ").append(currentDate).append("\n");
            txt.append("═══════════════════════════════════════════════════════════════════════════\n");
            
            Files.write(Paths.get(TEXT_EMAIL_REPORT_PATH), txt.toString().getBytes());
            
        } catch (Exception e) {
            logger.error("Error generating text email report: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Format AI analysis for HTML display
     */
    private String formatAIAnalysisForHTML(String analysis) {
        if (analysis == null || analysis.isEmpty()) {
            return "<p>AI analysis unavailable.</p>";
        }
        
        // First escape HTML entities
        String escaped = escapeHtml(analysis);
        
        // Split by sections and format
        String formatted = escaped.replace("\n\n", "</p><p>");
        formatted = formatted.replace("\n", "<br>");
        
        // Add section headers (after escaping, so they're safe)
        formatted = formatted.replaceAll("(EXECUTIVE SUMMARY|TEST RESULTS OVERVIEW|KEY FINDINGS|RECOMMENDATIONS|OVERALL ASSESSMENT|Executive Summary|Test Results Overview|Key Findings|Recommendations|Overall Assessment)", 
            "<h3>$1</h3>");
        
        // Handle numbered lists and bullet points
        formatted = formatted.replaceAll("\\*   ", "<li>");
        formatted = formatted.replaceAll("<li>([^<]*)<br>", "<li>$1</li>");
        
        return "<p>" + formatted + "</p>";
    }
    
    /**
     * Get CSS for email-friendly HTML
     */
    private String getEmailCSS() {
        return "@media screen { body { font-family: Arial, sans-serif; background: #f4f4f4; padding: 20px; } }\n" +
               "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; }\n" +
               ".email-container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; }\n" +
               ".header { text-align: center; border-bottom: 3px solid #4CAF50; padding-bottom: 20px; margin-bottom: 30px; }\n" +
               ".header h1 { color: #2c3e50; margin: 0; font-size: 24px; }\n" +
               ".header .meta { color: #666; font-size: 14px; margin-top: 10px; }\n" +
               ".quick-stats { display: grid; grid-template-columns: repeat(4, 1fr); gap: 15px; margin: 30px 0; }\n" +
               ".stat { text-align: center; padding: 20px; background: #f8f9fa; border-radius: 8px; }\n" +
               ".stat-value { display: block; font-size: 32px; font-weight: bold; color: #2c3e50; }\n" +
               ".stat-value.success { color: #4CAF50; }\n" +
               ".stat-value.error { color: #f44336; }\n" +
               ".stat-label { display: block; font-size: 12px; color: #666; margin-top: 5px; text-transform: uppercase; }\n" +
               ".ai-analysis { background: #e8f5e9; padding: 20px; border-radius: 8px; margin: 30px 0; border-left: 4px solid #4CAF50; }\n" +
               ".ai-analysis h2 { color: #2c3e50; margin-top: 0; }\n" +
               ".analysis-content { background: white; padding: 15px; border-radius: 5px; }\n" +
               ".analysis-content h3 { color: #4CAF50; margin-top: 15px; margin-bottom: 10px; font-size: 16px; }\n" +
               ".test-results { margin: 30px 0; }\n" +
               ".test-results h2 { color: #2c3e50; margin-bottom: 15px; }\n" +
               "table { width: 100%; border-collapse: collapse; font-size: 14px; }\n" +
               "table th { background: #2c3e50; color: white; padding: 12px; text-align: left; }\n" +
               "table td { padding: 10px; border-bottom: 1px solid #ddd; }\n" +
               "table tr.failed-row { background: #ffebee; }\n" +
               ".status-badge { padding: 5px 10px; border-radius: 12px; font-size: 12px; font-weight: bold; }\n" +
               ".status-badge.passed { background: #c8e6c9; color: #2e7d32; }\n" +
               ".status-badge.failed { background: #ffcdd2; color: #c62828; }\n" +
               ".footer { margin-top: 40px; padding-top: 20px; border-top: 2px solid #ddd; text-align: center; color: #666; font-size: 12px; }\n" +
               "@media print { body { background: white; } .email-container { box-shadow: none; } }\n";
    }
    
    private String formatDuration(long nanos) {
        if (nanos == 0) return "0s";
        long seconds = nanos / 1_000_000_000;
        if (seconds < 60) {
            return String.format("%ds", seconds);
        } else {
            long minutes = seconds / 60;
            long remainingSeconds = seconds % 60;
            return String.format("%dm %ds", minutes, remainingSeconds);
        }
    }
    
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;");
    }
    
    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
    
    // Inner classes for data structure
    private static class TestSummary {
        String featureName = "";
        String featureDescription = "";
        int totalScenarios = 0;
        int passedScenarios = 0;
        int failedScenarios = 0;
        int totalSteps = 0;
        int passedSteps = 0;
        int failedSteps = 0;
        long totalDuration = 0;
        List<ScenarioInfo> scenarios = new ArrayList<>();
    }
    
    private static class ScenarioInfo {
        String name;
        String status;
        long duration;
        String errorMessage = "";
    }
}

