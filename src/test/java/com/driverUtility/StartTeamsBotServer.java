package com.driverUtility;

import com.automation.config.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple utility class to start the Teams Bot Webhook Server
 * 
 * Usage:
 * 1. Run this class as a standalone application
 * 2. Or call TeamsBotWebhookServer.startServer() from your code
 * 
 * The server will listen for Teams bot commands and trigger Jenkins builds
 */
public class StartTeamsBotServer {
    private static final Logger logger = LoggerFactory.getLogger(StartTeamsBotServer.class);

    public static void main(String[] args) {
        logger.info("========================================");
        logger.info("Teams Bot Webhook Server");
        logger.info("========================================");
        
        if (!ConfigReader.isTeamsBotServerEnabled()) {
            logger.warn("Teams bot server is disabled in config.properties");
            logger.info("To enable, set: teams.bot.server.enabled=true");
            return;
        }
        
        int port = ConfigReader.getTeamsBotServerPort();
        logger.info("Starting Teams Bot Webhook Server on port {}...", port);
        
        try {
            TeamsBotWebhookServer.startServer(port);
            
            logger.info("========================================");
            logger.info("✅ Server started successfully!");
            logger.info("========================================");
            logger.info("Webhook endpoint: http://localhost:{}/teams/webhook", port);
            logger.info("Health check: http://localhost:{}/health", port);
            logger.info("");
            logger.info("📝 Next steps:");
            logger.info("1. Expose this server using ngrok: ngrok http {}", port);
            logger.info("2. Configure Teams bot with the ngrok URL");
            logger.info("3. Add bot to your Teams channel");
            logger.info("4. Test by typing 'build job-name' in Teams");
            logger.info("");
            logger.info("Press Ctrl+C to stop the server");
            logger.info("========================================");
            
            // Keep server running
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Shutting down Teams Bot Webhook Server...");
                TeamsBotWebhookServer.stopServer();
            }));
            
            // Keep main thread alive
            Thread.sleep(Long.MAX_VALUE);
            
        } catch (InterruptedException e) {
            logger.info("Server interrupted, shutting down...");
            TeamsBotWebhookServer.stopServer();
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Failed to start server: {}", e.getMessage(), e);
        }
    }
}

