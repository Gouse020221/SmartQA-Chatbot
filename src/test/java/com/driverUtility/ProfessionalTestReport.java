package com.driverUtility;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Professional Test Execution Report Generator
 * Creates detailed HTML reports with modern styling similar to enterprise test reports
 */
public class ProfessionalTestReport {
    private static final Logger logger = LoggerFactory.getLogger(ProfessionalTestReport.class);
    private static final String JSON_REPORT_PATH = "target/cucumber-reports/Cucumber.json";
    private static final String HTML_REPORT_PATH = "target/cucumber-reports/Test_Execution_Performance_Report.html";
    private static final String TEXT_REPORT_PATH = "target/cucumber-reports/Test_Execution_Performance_Report.txt";

    public static class TestAnalysis {
        int totalScenarios = 0;
        int passedScenarios = 0;
        int failedScenarios = 0;
        int totalSteps = 0;
        int passedSteps = 0;
        int failedSteps = 0;
        long totalDuration = 0;
        String featureName = "";
        String featureDescription = "";
        List<ScenarioDetails> scenarios = new ArrayList<>();
        List<StepDetails> allSteps = new ArrayList<>();
    }

    public static class ScenarioDetails {
        String name;
        String status;
        long duration;
        String formattedDuration;
        List<StepDetails> steps = new ArrayList<>();
        String errorMessage = "";
    }

    public static class StepDetails {
        String keyword;
        String name;
        String status;
        long duration;
        String formattedDuration;
        String errorMessage = "";
    }

    public static void main(String[] args) {
        try {
            logger.info("Starting Professional Test Report Generation...");
            generateReport();
            logger.info("Report generation completed successfully!");
        } catch (Exception e) {
            logger.error("Error generating report: {}", e.getMessage(), e);
        }
    }

    public static void generateReport() {
        try {
            // Read and analyze JSON report
            String jsonContent = readJsonReport();
            if (jsonContent == null || jsonContent.isEmpty()) {
                logger.warn("No JSON report found. Cannot generate reports.");
                return;
            }

            TestAnalysis analysis = analyzeTestResults(jsonContent);
            
            // Generate HTML Report
            generateHTMLReport(analysis);
            logger.info("Professional HTML Report generated at: {}", HTML_REPORT_PATH);
            
            // Generate Text Report
            generateTextReport(analysis);
            logger.info("Professional Text Report generated at: {}", TEXT_REPORT_PATH);
            
        } catch (Exception e) {
            logger.error("Error generating report: {}", e.getMessage(), e);
        }
    }

    private static String readJsonReport() {
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

    private static TestAnalysis analyzeTestResults(String jsonContent) {
        TestAnalysis analysis = new TestAnalysis();
        
        try {
            JsonArray features = JsonParser.parseString(jsonContent).getAsJsonArray();
            
            for (JsonElement featureElement : features) {
                JsonObject feature = featureElement.getAsJsonObject();
                analysis.featureName = feature.get("name").getAsString();
                
                if (feature.has("description") && !feature.get("description").isJsonNull()) {
                    analysis.featureDescription = feature.get("description").getAsString();
                }
                
                JsonArray elements = feature.getAsJsonArray("elements");
                
                for (JsonElement element : elements) {
                    JsonObject scenario = element.getAsJsonObject();
                    
                    // Skip Background elements - only count actual scenarios
                    String elementType = scenario.has("type") ? scenario.get("type").getAsString() : "scenario";
                    if ("background".equals(elementType)) {
                        continue; // Skip background, don't count as scenario
                    }
                    
                    ScenarioDetails scenarioDetails = new ScenarioDetails();
                    scenarioDetails.name = scenario.get("name").getAsString();
                    
                    analysis.totalScenarios++;
                    
                    boolean scenarioPassed = true;
                    long scenarioDuration = 0;
                    
                    JsonArray steps = scenario.getAsJsonArray("steps");
                    for (JsonElement stepElement : steps) {
                        JsonObject step = stepElement.getAsJsonObject();
                        
                        StepDetails stepDetails = new StepDetails();
                        stepDetails.keyword = step.get("keyword").getAsString();
                        stepDetails.name = step.get("name").getAsString();
                        
                        JsonObject result = step.getAsJsonObject("result");
                        String stepStatus = result.get("status").getAsString();
                        stepDetails.status = stepStatus;
                        
                        analysis.totalSteps++;
                        
                        long stepDuration = result.has("duration") ? result.get("duration").getAsLong() : 0;
                        stepDetails.duration = stepDuration;
                        stepDetails.formattedDuration = formatDuration(stepDuration);
                        scenarioDuration += stepDuration;
                        
                        if ("passed".equals(stepStatus)) {
                            analysis.passedSteps++;
                        } else {
                            analysis.failedSteps++;
                            scenarioPassed = false;
                            if (result.has("error_message")) {
                                stepDetails.errorMessage = result.get("error_message").getAsString();
                                scenarioDetails.errorMessage = stepDetails.errorMessage;
                            }
                        }
                        
                        scenarioDetails.steps.add(stepDetails);
                        analysis.allSteps.add(stepDetails);
                    }
                    
                    scenarioDetails.status = scenarioPassed ? "passed" : "failed";
                    scenarioDetails.duration = scenarioDuration;
                    scenarioDetails.formattedDuration = formatDuration(scenarioDuration);
                    analysis.totalDuration += scenarioDuration;
                    
                    if (scenarioPassed) {
                        analysis.passedScenarios++;
                    } else {
                        analysis.failedScenarios++;
                    }
                    
                    analysis.scenarios.add(scenarioDetails);
                }
            }
            
        } catch (Exception e) {
            logger.error("Error analyzing test results: {}", e.getMessage(), e);
        }
        
        return analysis;
    }

    private static String formatDuration(long nanos) {
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

    private static void generateHTMLReport(TestAnalysis analysis) {
        try {
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss"));
            double passRate = analysis.totalScenarios > 0 
                ? (analysis.passedScenarios * 100.0 / analysis.totalScenarios) 
                : 0;
            
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html lang=\"en\">\n");
            html.append("<head>\n");
            html.append("    <meta charset=\"UTF-8\">\n");
            html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            html.append("    <title>Test Execution Performance Report - " + currentDate + "</title>\n");
            html.append("    <style>\n");
            html.append(getCSS());
            html.append("    </style>\n");
            html.append("</head>\n");
            html.append("<body>\n");
            html.append("    <div class=\"container\">\n");
            
            // Header
            html.append("        <!-- Header -->\n");
            html.append("        <div class=\"header\">\n");
            html.append("            <h1>Test Execution Performance Report</h1>\n");
            html.append("            <div class=\"meta\">\n");
            html.append("                <strong>Date:</strong> " + currentDate + " | \n");
            html.append("                <strong>Test Suite:</strong> " + analysis.featureName + " | \n");
            html.append("                <strong>Browser:</strong> Chrome\n");
            html.append("            </div>\n");
            html.append("        </div>\n\n");
            
            // Executive Summary
            html.append("        <!-- Executive Summary -->\n");
            html.append("        <div class=\"summary-box\">\n");
            html.append("            <h2>Executive Summary</h2>\n");
            html.append("            <p>" + analysis.featureDescription + "</p>\n");
            html.append("            \n");
            html.append("            <div class=\"summary-stats\">\n");
            html.append("                <div class=\"stat-card\">\n");
            html.append("                    <span class=\"number\">" + analysis.totalScenarios + "</span>\n");
            html.append("                    <span class=\"label\">Total Scenarios</span>\n");
            html.append("                </div>\n");
            html.append("                <div class=\"stat-card\">\n");
            html.append("                    <span class=\"number\">" + analysis.passedScenarios + "</span>\n");
            html.append("                    <span class=\"label\">Passed</span>\n");
            html.append("                </div>\n");
            html.append("                <div class=\"stat-card\">\n");
            html.append("                    <span class=\"number\">" + analysis.failedScenarios + "</span>\n");
            html.append("                    <span class=\"label\">Failed</span>\n");
            html.append("                </div>\n");
            html.append("                <div class=\"stat-card\">\n");
            html.append("                    <span class=\"number\">" + String.format("%.2f%%", passRate) + "</span>\n");
            html.append("                    <span class=\"label\">Pass Rate</span>\n");
            html.append("                </div>\n");
            html.append("            </div>\n");
            html.append("        </div>\n\n");
            
            // Detailed Test Results
            html.append("        <!-- Detailed Test Results -->\n");
            html.append("        <div class=\"section\">\n");
            html.append("            <h2>📊 Detailed Test Results</h2>\n");
            html.append("            <table>\n");
            html.append("                <thead>\n");
            html.append("                    <tr>\n");
            html.append("                        <th>#</th>\n");
            html.append("                        <th>Scenario</th>\n");
            html.append("                        <th>Status</th>\n");
            html.append("                        <th>Duration</th>\n");
            html.append("                        <th>Steps</th>\n");
            html.append("                    </tr>\n");
            html.append("                </thead>\n");
            html.append("                <tbody>\n");
            
            int scenarioNum = 1;
            for (ScenarioDetails scenario : analysis.scenarios) {
                String rowStyle = scenario.status.equals("failed") ? " style=\"background: #fee;\"" : "";
                html.append("                    <tr" + rowStyle + ">\n");
                html.append("                        <td>" + scenarioNum++ + "</td>\n");
                html.append("                        <td>" + scenario.name + "</td>\n");
                
                if (scenario.status.equals("passed")) {
                    html.append("                        <td><span class=\"status-passed\">✅ PASSED</span></td>\n");
                } else {
                    html.append("                        <td><span class=\"status-failed\">❌ FAILED</span></td>\n");
                }
                
                html.append("                        <td>" + scenario.formattedDuration + "</td>\n");
                html.append("                        <td>" + scenario.steps.size() + " steps</td>\n");
                html.append("                    </tr>\n");
            }
            
            html.append("                </tbody>\n");
            html.append("            </table>\n");
            html.append("        </div>\n\n");
            
            // Step-by-Step Execution Details
            html.append("        <!-- Step-by-Step Execution Details -->\n");
            html.append("        <div class=\"section\">\n");
            html.append("            <h2>📋 Step-by-Step Execution Details</h2>\n");
            
            scenarioNum = 1;
            for (ScenarioDetails scenario : analysis.scenarios) {
                html.append("            <h3>Scenario " + scenarioNum + ": " + scenario.name + "</h3>\n");
                html.append("            <table>\n");
                html.append("                <thead>\n");
                html.append("                    <tr>\n");
                html.append("                        <th>Step</th>\n");
                html.append("                        <th>Description</th>\n");
                html.append("                        <th>Status</th>\n");
                html.append("                        <th>Duration</th>\n");
                html.append("                    </tr>\n");
                html.append("                </thead>\n");
                html.append("                <tbody>\n");
                
                int stepNum = 1;
                for (StepDetails step : scenario.steps) {
                    html.append("                    <tr>\n");
                    html.append("                        <td>" + stepNum++ + "</td>\n");
                    html.append("                        <td>" + step.keyword + step.name + "</td>\n");
                    
                    if (step.status.equals("passed")) {
                        html.append("                        <td><span class=\"badge badge-success\">PASSED</span></td>\n");
                    } else {
                        html.append("                        <td><span class=\"badge badge-danger\">FAILED</span></td>\n");
                    }
                    
                    html.append("                        <td>" + step.formattedDuration + "</td>\n");
                    html.append("                    </tr>\n");
                    
                    // Show error message if step failed
                    if (!step.status.equals("passed") && !step.errorMessage.isEmpty()) {
                        html.append("                    <tr>\n");
                        html.append("                        <td colspan=\"4\">\n");
                        html.append("                            <div class=\"alert alert-danger\">\n");
                        html.append("                                <strong>Error:</strong><br>\n");
                        html.append("                                <pre style=\"white-space: pre-wrap;\">" + escapeHtml(step.errorMessage) + "</pre>\n");
                        html.append("                            </div>\n");
                        html.append("                        </td>\n");
                        html.append("                    </tr>\n");
                    }
                }
                
                html.append("                </tbody>\n");
                html.append("            </table>\n\n");
                scenarioNum++;
            }
            
            html.append("        </div>\n\n");
            
            // Performance Metrics
            html.append("        <!-- Performance Metrics -->\n");
            html.append("        <div class=\"section\">\n");
            html.append("            <h2>⚡ Performance Metrics</h2>\n");
            html.append("            <div class=\"metrics-grid\">\n");
            html.append("                <div class=\"metric-card\">\n");
            html.append("                    <h4>Total Execution Time</h4>\n");
            html.append("                    <div class=\"value\">" + formatDuration(analysis.totalDuration) + "</div>\n");
            html.append("                </div>\n");
            html.append("                <div class=\"metric-card\">\n");
            html.append("                    <h4>Total Steps</h4>\n");
            html.append("                    <div class=\"value\">" + analysis.totalSteps + "</div>\n");
            html.append("                </div>\n");
            html.append("                <div class=\"metric-card\">\n");
            html.append("                    <h4>Step Success Rate</h4>\n");
            html.append("                    <div class=\"value\">" + String.format("%.2f%%", (analysis.passedSteps * 100.0 / analysis.totalSteps)) + "</div>\n");
            html.append("                </div>\n");
            html.append("            </div>\n");
            html.append("        </div>\n\n");
            
            // Failure Analysis (if any)
            if (analysis.failedScenarios > 0) {
                html.append("        <!-- Failure Analysis -->\n");
                html.append("        <div class=\"section\">\n");
                html.append("            <h2>🔍 Failure Analysis</h2>\n");
                
                for (ScenarioDetails scenario : analysis.scenarios) {
                    if (scenario.status.equals("failed")) {
                        html.append("            <div class=\"alert alert-danger\">\n");
                        html.append("                <strong>Failed Scenario:</strong> " + scenario.name + "<br>\n");
                        html.append("                <strong>Duration:</strong> " + scenario.formattedDuration + "\n");
                        html.append("            </div>\n");
                        
                        if (!scenario.errorMessage.isEmpty()) {
                            html.append("            <h3>Error Details</h3>\n");
                            html.append("            <div class=\"code-block\">" + escapeHtml(scenario.errorMessage) + "</div>\n");
                        }
                    }
                }
                
                html.append("        </div>\n\n");
            }
            
            // Key Findings
            html.append("        <!-- Key Findings -->\n");
            html.append("        <div class=\"section\">\n");
            html.append("            <h2>📌 Key Findings</h2>\n");
            
            if (analysis.failedScenarios == 0) {
                html.append("            <div class=\"alert alert-success\">\n");
                html.append("                <h3 style=\"margin-bottom: 10px;\">✅ Positive Findings</h3>\n");
                html.append("                <ul style=\"margin-bottom: 0;\">\n");
                html.append("                    <li><strong>Perfect Success Rate:</strong> 100% pass rate indicates stable automation</li>\n");
                html.append("                    <li><strong>All Scenarios Passed:</strong> No failures detected in current execution</li>\n");
                html.append("                    <li><strong>Consistent Performance:</strong> All operations completed successfully</li>\n");
                html.append("                </ul>\n");
                html.append("            </div>\n");
            } else {
                html.append("            <div class=\"alert alert-warning\">\n");
                html.append("                <h3 style=\"margin-bottom: 10px;\">⚠️ Areas of Concern</h3>\n");
                html.append("                <ul style=\"margin-bottom: 0;\">\n");
                html.append("                    <li>" + analysis.failedScenarios + " scenario(s) failed out of " + analysis.totalScenarios + "</li>\n");
                html.append("                    <li>" + analysis.failedSteps + " step(s) failed out of " + analysis.totalSteps + "</li>\n");
                html.append("                    <li>Review error logs for detailed failure analysis</li>\n");
                html.append("                </ul>\n");
                html.append("            </div>\n");
            }
            
            html.append("        </div>\n\n");
            
            // Conclusion
            html.append("        <!-- Conclusion -->\n");
            html.append("        <div class=\"section\">\n");
            html.append("            <h2>🎯 Conclusion</h2>\n");
            
            if (passRate == 100) {
                html.append("            <p>The test execution demonstrates a <strong>robust and reliable automation framework</strong> with a perfect success rate of 100%.</p>\n");
                html.append("            <div class=\"alert alert-success\">\n");
                html.append("                <strong>Overall Assessment:</strong> All scenarios passed successfully. The automation suite is production-ready.\n");
                html.append("            </div>\n");
            } else if (passRate >= 80) {
                html.append("            <p>The test execution shows <strong>good stability</strong> with a " + String.format("%.2f%%", passRate) + " pass rate, but some issues need attention.</p>\n");
                html.append("            <div class=\"alert alert-warning\">\n");
                html.append("                <strong>Overall Assessment:</strong> The automation suite is functional but requires minor fixes for improved reliability.\n");
                html.append("            </div>\n");
            } else {
                html.append("            <p>The test execution indicates <strong>critical issues</strong> with a " + String.format("%.2f%%", passRate) + " pass rate. Immediate attention required.</p>\n");
                html.append("            <div class=\"alert alert-danger\">\n");
                html.append("                <strong>Overall Assessment:</strong> Multiple failures detected. Please review and fix failing scenarios before production deployment.\n");
                html.append("            </div>\n");
            }
            
            html.append("        </div>\n\n");
            
            // Footer
            html.append("        <!-- Footer -->\n");
            html.append("        <div class=\"footer\">\n");
            html.append("            <p><strong>Report Generated:</strong> " + currentDate + "</p>\n");
            html.append("            <p><strong>Prepared By:</strong> Automation Testing Team</p>\n");
            html.append("            <p><strong>Next Review:</strong> Weekly</p>\n");
            html.append("            <p style=\"margin-top: 10px;\">© 2025 SmartQA Automation Testing. All rights reserved.</p>\n");
            html.append("        </div>\n");
            
            html.append("    </div>\n\n");
            
            // JavaScript
            html.append("    <script>\n");
            html.append("        // Add print functionality\n");
            html.append("        window.onload = function() {\n");
            html.append("            console.log('Report loaded successfully. Use Ctrl+P or File > Print to generate PDF.');\n");
            html.append("        };\n");
            html.append("    </script>\n");
            
            html.append("</body>\n");
            html.append("</html>\n");
            
            // Write to file
            Files.write(Paths.get(HTML_REPORT_PATH), html.toString().getBytes());
            
        } catch (Exception e) {
            logger.error("Error generating HTML report: {}", e.getMessage(), e);
        }
    }

    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;");
    }

    private static String getCSS() {
        StringBuilder css = new StringBuilder();
        css.append("@page { size: A4; margin: 1.5cm; }\n");
        css.append("* { margin: 0; padding: 0; box-sizing: border-box; }\n");
        css.append("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background: #f5f5f5; }\n");
        css.append(".container { max-width: 1200px; margin: 0 auto; background: white; padding: 40px; box-shadow: 0 0 20px rgba(0,0,0,0.1); }\n");
        css.append(".header { text-align: center; border-bottom: 4px solid #2c3e50; padding-bottom: 20px; margin-bottom: 30px; }\n");
        css.append(".header h1 { color: #2c3e50; font-size: 28px; margin-bottom: 10px; }\n");
        css.append(".header .meta { color: #7f8c8d; font-size: 14px; }\n");
        css.append(".summary-box { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 25px; border-radius: 10px; margin-bottom: 30px; }\n");
        css.append(".summary-box h2 { margin-bottom: 15px; font-size: 22px; }\n");
        css.append(".summary-stats { display: grid; grid-template-columns: repeat(4, 1fr); gap: 15px; margin-top: 20px; }\n");
        css.append(".stat-card { background: rgba(255,255,255,0.2); padding: 15px; border-radius: 8px; text-align: center; }\n");
        css.append(".stat-card .number { font-size: 32px; font-weight: bold; display: block; }\n");
        css.append(".stat-card .label { font-size: 12px; opacity: 0.9; text-transform: uppercase; }\n");
        css.append(".section { margin-bottom: 35px; page-break-inside: avoid; }\n");
        css.append(".section h2 { color: #2c3e50; font-size: 20px; margin-bottom: 15px; padding-bottom: 8px; border-bottom: 2px solid #3498db; }\n");
        css.append(".section h3 { color: #34495e; font-size: 16px; margin-top: 20px; margin-bottom: 10px; }\n");
        css.append("table { width: 100%; border-collapse: collapse; margin: 15px 0; font-size: 13px; }\n");
        css.append("table thead { background: #34495e; color: white; }\n");
        css.append("table th { padding: 12px; text-align: left; font-weight: 600; }\n");
        css.append("table td { padding: 10px 12px; border-bottom: 1px solid #ecf0f1; }\n");
        css.append("table tbody tr:hover { background: #f8f9fa; }\n");
        css.append(".status-passed { color: #27ae60; font-weight: bold; }\n");
        css.append(".status-failed { color: #e74c3c; font-weight: bold; }\n");
        css.append(".badge { display: inline-block; padding: 4px 10px; border-radius: 12px; font-size: 11px; font-weight: bold; text-transform: uppercase; }\n");
        css.append(".badge-success { background: #d4edda; color: #155724; }\n");
        css.append(".badge-danger { background: #f8d7da; color: #721c24; }\n");
        css.append(".badge-warning { background: #fff3cd; color: #856404; }\n");
        css.append(".badge-info { background: #d1ecf1; color: #0c5460; }\n");
        css.append(".metrics-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin: 20px 0; }\n");
        css.append(".metric-card { background: #f8f9fa; padding: 20px; border-radius: 8px; border-left: 4px solid #3498db; }\n");
        css.append(".metric-card h4 { color: #2c3e50; margin-bottom: 10px; font-size: 14px; }\n");
        css.append(".metric-card .value { font-size: 24px; font-weight: bold; color: #3498db; }\n");
        css.append(".alert { padding: 15px 20px; margin: 15px 0; border-radius: 8px; border-left: 4px solid; }\n");
        css.append(".alert-warning { background: #fff3cd; border-color: #ffc107; color: #856404; }\n");
        css.append(".alert-danger { background: #f8d7da; border-color: #dc3545; color: #721c24; }\n");
        css.append(".alert-success { background: #d4edda; border-color: #28a745; color: #155724; }\n");
        css.append(".alert-info { background: #d1ecf1; border-color: #17a2b8; color: #0c5460; }\n");
        css.append(".code-block { background: #282c34; color: #abb2bf; padding: 15px; border-radius: 6px; font-family: 'Courier New', monospace; font-size: 12px; overflow-x: auto; margin: 10px 0; }\n");
        css.append("ul, ol { margin-left: 25px; margin-bottom: 15px; }\n");
        css.append("li { margin-bottom: 8px; }\n");
        css.append(".footer { margin-top: 40px; padding-top: 20px; border-top: 2px solid #ecf0f1; text-align: center; color: #7f8c8d; font-size: 12px; }\n");
        css.append("@media print { body { background: white; } .container { box-shadow: none; padding: 20px; } .section { page-break-inside: avoid; } }\n");
        return css.toString();
    }
    
    /**
     * Generate a professional text report suitable for email attachments
     */
    private static void generateTextReport(TestAnalysis analysis) {
        try {
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            double passRate = analysis.totalScenarios > 0 
                ? (analysis.passedScenarios * 100.0 / analysis.totalScenarios) 
                : 0;
            
            StringBuilder txt = new StringBuilder();
            
            // Header
            txt.append("═══════════════════════════════════════════════════════════════════════════\n");
            txt.append("                   TEST EXECUTION PERFORMANCE REPORT                       \n");
            txt.append("═══════════════════════════════════════════════════════════════════════════\n\n");
            
            txt.append("Date:        ").append(currentDate).append("\n");
            txt.append("Timestamp:   ").append(currentDateTime).append("\n");
            txt.append("Test Suite:  ").append(analysis.featureName).append("\n");
            txt.append("Browser:     Chrome\n");
            txt.append("Generated:   Automation Testing Team\n");
            txt.append("\n");
            
            // Executive Summary
            txt.append("───────────────────────────────────────────────────────────────────────────\n");
            txt.append("  EXECUTIVE SUMMARY\n");
            txt.append("───────────────────────────────────────────────────────────────────────────\n\n");
            
            if (!analysis.featureDescription.isEmpty()) {
                txt.append(analysis.featureDescription).append("\n\n");
            }
            
            txt.append(String.format("  Total Scenarios:    %d\n", analysis.totalScenarios));
            txt.append(String.format("  Passed:             %d ✓\n", analysis.passedScenarios));
            txt.append(String.format("  Failed:             %d %s\n", analysis.failedScenarios, 
                analysis.failedScenarios > 0 ? "✗" : ""));
            txt.append(String.format("  Pass Rate:          %.2f%%\n", passRate));
            txt.append(String.format("  Total Duration:     %s\n", formatDuration(analysis.totalDuration)));
            txt.append(String.format("  Total Steps:        %d\n", analysis.totalSteps));
            txt.append(String.format("  Step Success Rate:  %.2f%%\n", 
                analysis.totalSteps > 0 ? (analysis.passedSteps * 100.0 / analysis.totalSteps) : 0));
            txt.append("\n");
            
            // Test Results Summary
            txt.append("───────────────────────────────────────────────────────────────────────────\n");
            txt.append("  DETAILED TEST RESULTS\n");
            txt.append("───────────────────────────────────────────────────────────────────────────\n\n");
            
            txt.append(String.format("%-4s %-60s %-12s %-10s\n", "#", "Scenario", "Status", "Duration"));
            txt.append("─────────────────────────────────────────────────────────────────────────────\n");
            
            int scenarioNum = 1;
            for (ScenarioDetails scenario : analysis.scenarios) {
                String status = scenario.status.equals("passed") ? "✓ PASSED" : "✗ FAILED";
                String name = scenario.name;
                if (name.length() > 55) {
                    name = name.substring(0, 52) + "...";
                }
                txt.append(String.format("%-4d %-60s %-12s %-10s\n", 
                    scenarioNum++, 
                    name, 
                    status, 
                    scenario.formattedDuration));
            }
            txt.append("\n");
            
            // Step-by-Step Execution Details
            txt.append("───────────────────────────────────────────────────────────────────────────\n");
            txt.append("  STEP-BY-STEP EXECUTION DETAILS\n");
            txt.append("───────────────────────────────────────────────────────────────────────────\n\n");
            
            scenarioNum = 1;
            for (ScenarioDetails scenario : analysis.scenarios) {
                txt.append("┌─────────────────────────────────────────────────────────────────────────┐\n");
                txt.append(String.format("│ Scenario %d: %-62s│\n", scenarioNum, 
                    truncate(scenario.name, 62)));
                txt.append(String.format("│ Status: %-65s│\n", 
                    scenario.status.equals("passed") ? "✓ PASSED" : "✗ FAILED"));
                txt.append(String.format("│ Duration: %-63s│\n", scenario.formattedDuration));
                txt.append("└─────────────────────────────────────────────────────────────────────────┘\n\n");
                
                int stepNum = 1;
                for (StepDetails step : scenario.steps) {
                    String stepStatus = step.status.equals("passed") ? "[✓]" : "[✗]";
                    String stepDescription = step.keyword + step.name;
                    
                    txt.append(String.format("  %d. %s %s\n", stepNum, stepStatus, stepDescription));
                    txt.append(String.format("      Duration: %s | Status: %s\n", 
                        step.formattedDuration, 
                        step.status.toUpperCase()));
                    
                    if (!step.status.equals("passed") && !step.errorMessage.isEmpty()) {
                        txt.append("      ERROR: ").append(step.errorMessage.replace("\n", "\n             ")).append("\n");
                    }
                    txt.append("\n");
                    stepNum++;
                }
                
                scenarioNum++;
            }
            
            // Performance Metrics
            txt.append("───────────────────────────────────────────────────────────────────────────\n");
            txt.append("  PERFORMANCE METRICS\n");
            txt.append("───────────────────────────────────────────────────────────────────────────\n\n");
            
            txt.append(String.format("  ⚡ Total Execution Time:    %s\n", formatDuration(analysis.totalDuration)));
            txt.append(String.format("  📊 Total Steps Executed:    %d\n", analysis.totalSteps));
            txt.append(String.format("  ✓ Steps Passed:             %d\n", analysis.passedSteps));
            txt.append(String.format("  ✗ Steps Failed:             %d\n", analysis.failedSteps));
            txt.append(String.format("  📈 Step Success Rate:       %.2f%%\n", 
                analysis.totalSteps > 0 ? (analysis.passedSteps * 100.0 / analysis.totalSteps) : 0));
            txt.append("\n");
            
            // Failure Analysis (if any)
            if (analysis.failedScenarios > 0) {
                txt.append("───────────────────────────────────────────────────────────────────────────\n");
                txt.append("  FAILURE ANALYSIS\n");
                txt.append("───────────────────────────────────────────────────────────────────────────\n\n");
                
                for (ScenarioDetails scenario : analysis.scenarios) {
                    if (scenario.status.equals("failed")) {
                        txt.append("  ❌ Failed Scenario: ").append(scenario.name).append("\n");
                        txt.append("     Duration: ").append(scenario.formattedDuration).append("\n");
                        
                        if (!scenario.errorMessage.isEmpty()) {
                            txt.append("     Error Details:\n");
                            txt.append("     ").append(scenario.errorMessage.replace("\n", "\n     ")).append("\n");
                        }
                        txt.append("\n");
                    }
                }
            }
            
            // Key Findings
            txt.append("───────────────────────────────────────────────────────────────────────────\n");
            txt.append("  KEY FINDINGS\n");
            txt.append("───────────────────────────────────────────────────────────────────────────\n\n");
            
            if (analysis.failedScenarios == 0) {
                txt.append("  ✅ POSITIVE FINDINGS:\n\n");
                txt.append("     • Perfect Success Rate: 100% pass rate indicates stable automation\n");
                txt.append("     • All Scenarios Passed: No failures detected in current execution\n");
                txt.append("     • Consistent Performance: All operations completed successfully\n");
                txt.append("     • Production Ready: The automation suite is stable and reliable\n");
            } else {
                txt.append("  ⚠ AREAS OF CONCERN:\n\n");
                txt.append(String.format("     • %d scenario(s) failed out of %d total scenarios\n", 
                    analysis.failedScenarios, analysis.totalScenarios));
                txt.append(String.format("     • %d step(s) failed out of %d total steps\n", 
                    analysis.failedSteps, analysis.totalSteps));
                txt.append("     • Review error logs for detailed failure analysis\n");
                txt.append("     • Investigate and fix failing scenarios before production deployment\n");
            }
            txt.append("\n");
            
            // Conclusion
            txt.append("───────────────────────────────────────────────────────────────────────────\n");
            txt.append("  CONCLUSION\n");
            txt.append("───────────────────────────────────────────────────────────────────────────\n\n");
            
            if (passRate == 100) {
                txt.append("  The test execution demonstrates a ROBUST AND RELIABLE automation\n");
                txt.append("  framework with a perfect success rate of 100%.\n\n");
                txt.append("  ✅ Overall Assessment: All scenarios passed successfully. The automation\n");
                txt.append("  suite is production-ready and demonstrates excellent stability.\n");
            } else if (passRate >= 80) {
                txt.append(String.format("  The test execution shows GOOD STABILITY with a %.2f%% pass rate,\n", passRate));
                txt.append("  but some issues need attention.\n\n");
                txt.append("  ⚠ Overall Assessment: The automation suite is functional but requires\n");
                txt.append("  minor fixes for improved reliability.\n");
            } else {
                txt.append(String.format("  The test execution indicates CRITICAL ISSUES with a %.2f%% pass rate.\n", passRate));
                txt.append("  Immediate attention required.\n\n");
                txt.append("  ❌ Overall Assessment: Multiple failures detected. Please review and fix\n");
                txt.append("  failing scenarios before production deployment.\n");
            }
            txt.append("\n");
            
            // Recommendations
            txt.append("───────────────────────────────────────────────────────────────────────────\n");
            txt.append("  RECOMMENDATIONS\n");
            txt.append("───────────────────────────────────────────────────────────────────────────\n\n");
            
            if (analysis.failedScenarios == 0) {
                txt.append("  • Continue monitoring test execution trends\n");
                txt.append("  • Maintain regular test suite reviews\n");
                txt.append("  • Keep automation framework updated\n");
                txt.append("  • Consider expanding test coverage\n");
            } else {
                txt.append("  • Immediate: Fix all failing scenarios\n");
                txt.append("  • Review: Analyze root cause of failures\n");
                txt.append("  • Update: Modify tests if requirements changed\n");
                txt.append("  • Verify: Re-run tests after fixes\n");
            }
            txt.append("\n");
            
            // Footer
            txt.append("═══════════════════════════════════════════════════════════════════════════\n");
            txt.append("  Report Generated: ").append(currentDate).append("\n");
            txt.append("  Prepared By: Automation Testing Team\n");
            txt.append("  Next Review: Weekly\n");
            txt.append("  © 2025 SmartQA Automation Testing. All rights reserved.\n");
            txt.append("═══════════════════════════════════════════════════════════════════════════\n");
            
            // Write to file
            Files.write(Paths.get(TEXT_REPORT_PATH), txt.toString().getBytes());
            
        } catch (Exception e) {
            logger.error("Error generating text report: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Truncate text to specified length with ellipsis
     */
    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) {
            return String.format("%-" + maxLength + "s", text);
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}

