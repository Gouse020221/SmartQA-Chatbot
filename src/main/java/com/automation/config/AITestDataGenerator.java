package com.automation.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * AI Test Data Generator Utility
 * Uses OpenAI API to generate realistic test data
 */
public class AITestDataGenerator {
    
    private static final Logger logger = LoggerFactory.getLogger(AITestDataGenerator.class);
    private static final String DEFAULT_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String DEFAULT_MODEL = "gpt-3.5-turbo";
    
    private final OkHttpClient client;
    private final String apiUrl;
    private final String apiKey;
    private final String model;
    
    /**
     * Constructor with default settings
     */
    public AITestDataGenerator() {
        this(DEFAULT_API_URL, System.getProperty("OPENAI_API_KEY", ""), DEFAULT_MODEL);
    }
    
    /**
     * Constructor with custom settings
     * @param apiUrl API endpoint URL
     * @param apiKey API key for authentication
     * @param model AI model to use
     */
    public AITestDataGenerator(String apiUrl, String apiKey, String model) {
        this.client = new OkHttpClient();
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.model = model;
    }
    
    /**
     * Generate AI test data based on prompt
     * @param prompt The prompt for AI to generate data
     * @return Generated test data
     */
    public String generateTestData(String prompt) {
        return generateTestData(prompt, 50, 0.7);
    }
    
    /**
     * Generate AI test data with custom parameters
     * @param prompt The prompt for AI to generate data
     * @param maxTokens Maximum tokens for response
     * @param temperature Temperature for randomness (0.0-1.0)
     * @return Generated test data
     */
    public String generateTestData(String prompt, int maxTokens, double temperature) {
        // Check if API key is configured
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-api-key-here")) {
            logger.warn("OpenAI API key not configured. Using fallback data generation.");
            return generateFallbackData(prompt);
        }
        
        try {
            logger.info("Generating AI test data with prompt: {}", prompt);
            
            // Create JSON request body
            JsonObject messageObj = new JsonObject();
            messageObj.addProperty("role", "user");
            messageObj.addProperty("content", prompt + ". Return only the requested data without any explanation or additional text.");
            
            JsonArray messagesArray = new JsonArray();
            messagesArray.add(messageObj);
            
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", model);
            requestBody.add("messages", messagesArray);
            requestBody.addProperty("max_tokens", maxTokens);
            requestBody.addProperty("temperature", temperature);
            
            // Create HTTP request
            RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json; charset=utf-8")
            );
            
            Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
            
            // Execute request
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    logger.debug("AI API Response: {}", responseBody);
                    
                    // Parse response
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                    JsonArray choices = jsonResponse.getAsJsonArray("choices");
                    
                    if (choices != null && choices.size() > 0) {
                        JsonObject firstChoice = choices.get(0).getAsJsonObject();
                        JsonObject message = firstChoice.getAsJsonObject("message");
                        String generatedText = message.get("content").getAsString().trim();
                        logger.info("Generated AI Data: {}", generatedText);
                        return generatedText;
                    }
                } else {
                    logger.warn("AI API call failed with status: {}. Using fallback.", response.code());
                }
            }
            
        } catch (IOException e) {
            logger.warn("Error calling AI API: {}. Using fallback.", e.getMessage());
        } catch (Exception e) {
            logger.warn("Unexpected error calling AI API: {}. Using fallback.", e.getMessage());
        }
        
        // Fallback if AI service fails
        return generateFallbackData(prompt);
    }
    
    /**
     * Generate fallback data when AI service is unavailable
     * @param prompt The original prompt
     * @return Fallback test data
     */
    private String generateFallbackData(String prompt) {
        logger.info("Using fallback data generation for prompt: {}", prompt);
        
        String lowerPrompt = prompt.toLowerCase();
        
        // Determine data type from prompt
        if (lowerPrompt.contains("first name") || lowerPrompt.contains("firstname")) {
            return generateFallbackFirstName();
        } else if (lowerPrompt.contains("last name") || lowerPrompt.contains("lastname")) {
            return generateFallbackLastName();
        } else if (lowerPrompt.contains("email")) {
            return generateFallbackEmail();
        } else if (lowerPrompt.contains("phone")) {
            return generateFallbackPhoneNumber();
        } else if (lowerPrompt.contains("address")) {
            return generateFallbackAddress();
        } else if (lowerPrompt.contains("city")) {
            return generateFallbackCity();
        } else if (lowerPrompt.contains("zip")) {
            return generateFallbackZip();
        } else {
            return "TestData_" + System.currentTimeMillis();
        }
    }
    
    /**
     * Generate random first name
     */
    private String generateFallbackFirstName() {
        String[] firstNames = {
            "John", "Emma", "Michael", "Sophia", "William", 
            "Olivia", "James", "Ava", "Benjamin", "Isabella", 
            "Lucas", "Mia", "Alexander", "Charlotte", "Daniel", 
            "Amelia", "Henry", "Harper", "Matthew", "Evelyn",
            "David", "Abigail", "Joseph", "Emily", "Andrew"
        };
        return firstNames[(int) (Math.random() * firstNames.length)];
    }
    
    /**
     * Generate random last name
     */
    private String generateFallbackLastName() {
        String[] lastNames = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", 
            "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", 
            "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
            "Thomas", "Taylor", "Moore", "Jackson", "Martin"
        };
        return lastNames[(int) (Math.random() * lastNames.length)];
    }
    
    /**
     * Generate random email
     */
    private String generateFallbackEmail() {
        return "testuser" + System.currentTimeMillis() + "@yopmail.com";
    }
    
    /**
     * Generate random phone number
     */
    private String generateFallbackPhoneNumber() {
        long phoneNumber = 2000000000L + (long) (Math.random() * 7999999999L);
        return String.valueOf(phoneNumber);
    }
    
    /**
     * Generate random address
     */
    private String generateFallbackAddress() {
        int streetNumber = 100 + (int) (Math.random() * 9900);
        String[] streetNames = {"Main St", "Oak Ave", "Maple Dr", "Cedar Ln", "Pine Rd", "Elm St"};
        return streetNumber + " " + streetNames[(int) (Math.random() * streetNames.length)];
    }
    
    /**
     * Generate random city
     */
    private String generateFallbackCity() {
        String[] cities = {
            "Austin", "Dallas", "Houston", "San Antonio", "Phoenix", 
            "Chicago", "New York", "Los Angeles", "Miami", "Seattle"
        };
        return cities[(int) (Math.random() * cities.length)];
    }
    
    /**
     * Generate random zip code
     */
    private String generateFallbackZip() {
        int zip = 10000 + (int) (Math.random() * 89999);
        return String.valueOf(zip);
    }
}

