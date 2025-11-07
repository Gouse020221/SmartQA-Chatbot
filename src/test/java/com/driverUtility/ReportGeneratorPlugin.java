package com.driverUtility;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber Plugin to automatically generate professional test report after test execution
 * This ensures the report is generated every time, even when running from Eclipse
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
            Thread.sleep(2000);
            
            // Generate the professional report
            ProfessionalTestReport.generateReport();
            
            logger.info("✅ Professional Test Report generated successfully!");
            logger.info("📊 Report location: target/cucumber-reports/Test_Execution_Performance_Report.html");
        } catch (Exception e) {
            logger.error("❌ Error generating professional report: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}


