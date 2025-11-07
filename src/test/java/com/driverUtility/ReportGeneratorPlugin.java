package com.driverUtility;

import AI.AIEmailReportGenerator;
//import com.automation.config.ConfigReader;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber Plugin to automatically generate professional test report after test execution
 * This ensures the report is generated every time, even when running from Eclipse
 * Also generates and sends AI-powered email reports automatically
 */
public class ReportGeneratorPlugin implements ConcurrentEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ReportGeneratorPlugin.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);
    }

    private void handleTestRunFinished(TestRunFinished event) {
        logger.info("🎯 Test run finished. Generating professional test report...");
        
        try {
            // Small delay to ensure JSON file is fully written
            Thread.sleep(3000);
            
            // Generate the professional report
            ProfessionalTestReport.generateReport();
            
            logger.info("✅ Professional Test Report generated successfully!");
            logger.info("📊 Report location: target/cucumber-reports/Test_Execution_Performance_Report.html");
            
            // Generate and send AI email report
            generateAndSendEmailReport();
            
        } catch (Exception e) {
            logger.error("❌ Error generating professional report: {}", e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Generate and send AI-powered email report
     */
    private void generateAndSendEmailReport() {
        try {
            // Check if email reporting is enabled
            //String emailEnabled = ConfigReader.getProperty("email.report.enabled");
//            if (emailEnabled == null || !emailEnabled.equalsIgnoreCase("true")) {
//                logger.info("📧 Email reporting is disabled. Skipping email report generation.");
//                return;
//            }
            
            // Get email recipients from config
//            String recipientsStr = ConfigReader.getProperty("email.report.recipients");
//            if (recipientsStr == null || recipientsStr.trim().isEmpty()) {
//                logger.warn("📧 No email recipients configured. Skipping email report.");
//                return;
//            }
            
            // Parse recipients (comma-separated)
//            String[] recipients = recipientsStr.split(",");
//            for (int i = 0; i < recipients.length; i++) {
//                recipients[i] = recipients[i].trim();
//            }
            
            logger.info("📧 Generating AI email report...");
            
            // Generate AI email report
            AIEmailReportGenerator emailGenerator = new AIEmailReportGenerator();
            emailGenerator.generateEmailReport();
            
            logger.info("✅ AI Email Report generated successfully!");
            
            // Send email report
//            logger.info("📧 Sending email report to: {}", String.join(", ", recipients));
//            emailGenerator.sendEmailReport(recipients);
//            
//            logger.info("✅ Email report sent successfully to all recipients!");
//            
        } catch (Exception e) {
            logger.error("❌ Error generating/sending email report: {}", e.getMessage(), e);
            // Don't throw exception - email failure shouldn't fail the test run
        }
    }
}



