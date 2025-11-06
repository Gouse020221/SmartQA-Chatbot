package com.automation.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    static {
        try {
            FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH);
            properties = new Properties();
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless"));
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout"));
    }

    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getAiApiUrl() {
        return getProperty("ai.api.url");
    }

    public static String getAiApiKey() {
        return getProperty("ai.api.key");
    }

    public static String getAiModel() {
        return getProperty("ai.model");
    }

    public static double getAiTemperature() {
        return Double.parseDouble(getProperty("ai.temperature"));
    }

    public static int getAiMaxTokens() {
        return Integer.parseInt(getProperty("ai.max.tokens"));
    }

    // Teams ChatOps Configuration
    public static String getTeamsWebhookUrl() {
        return getProperty("teams.webhook.url");
    }

    public static String getReportBaseUrl() {
        return getProperty("report.base.url");
    }

    // Jenkins Configuration
    public static String getJenkinsBaseUrl() {
        return getProperty("jenkins.base.url");
    }

    public static String getJenkinsJobName() {
        return getProperty("jenkins.job.name");
    }

    public static String getJenkinsUsername() {
        return getProperty("jenkins.username");
    }

    public static String getJenkinsApiToken() {
        return getProperty("jenkins.api.token");
    }

    // Teams Bot Server Configuration
    public static int getTeamsBotServerPort() {
        String port = getProperty("teams.bot.server.port");
        return port != null && !port.isEmpty() ? Integer.parseInt(port) : 8080;
    }

    public static boolean isTeamsBotServerEnabled() {
        return Boolean.parseBoolean(getProperty("teams.bot.server.enabled"));
    }
}

