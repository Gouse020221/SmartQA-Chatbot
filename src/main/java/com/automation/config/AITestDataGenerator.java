package com.automation.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
     * Constructor with default settings - loads from ConfigReader
     */
    public AITestDataGenerator() {
        this(
            ConfigReader.getAiApiUrl() != null ? ConfigReader.getAiApiUrl() : DEFAULT_API_URL,
            ConfigReader.getAiApiKey() != null ? ConfigReader.getAiApiKey() : "",
            ConfigReader.getAiModel() != null ? ConfigReader.getAiModel() : DEFAULT_MODEL
        );
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
        return generateTestData(prompt, 400, 0.7);
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
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-api-key-here") || apiKey.equals("PASTE_YOUR_NEW_API_KEY_HERE")) {
            logger.warn("AI API key not configured. Using fallback data generation.");
            return generateFallbackData(prompt);
        }
        
        try {
            logger.info("Generating AI test data with prompt: {}", prompt);
            
            // Detect if using Gemini or OpenAI based on URL
            boolean isGemini = apiUrl.contains("generativelanguage.googleapis.com");
            
            JsonObject requestBody;
            String finalUrl = apiUrl;
            
            if (isGemini) {
                // Gemini API format
                logger.info("Using Google Gemini API");
                
                // Build Gemini request body
                JsonObject textPart = new JsonObject();
                // Add random number to prompt for uniqueness (shorter than timestamp)
                int random = (int)(Math.random() * 10000);
                String uniquePrompt = prompt + ". Just the value, #" + random;
                textPart.addProperty("text", uniquePrompt);
                
                JsonObject content = new JsonObject();
                JsonArray parts = new JsonArray();
                parts.add(textPart);
                content.add("parts", parts);
                
                JsonArray contents = new JsonArray();
                contents.add(content);
                
                requestBody = new JsonObject();
                requestBody.add("contents", contents);
                
                // Add generation config with increased randomness for variety
                JsonObject generationConfig = new JsonObject();
                generationConfig.addProperty("temperature", 1.0);  // Maximum variety
                generationConfig.addProperty("maxOutputTokens", maxTokens);
                generationConfig.addProperty("topK", 40);  // More options for variety
                generationConfig.addProperty("topP", 0.95);  // Increased for more randomness
                requestBody.add("generationConfig", generationConfig);
                
                // Add API key to URL for Gemini
                finalUrl = apiUrl + "?key=" + apiKey;
                
            } else {
                // OpenAI API format
                logger.info("Using OpenAI API");
                
                JsonObject messageObj = new JsonObject();
                messageObj.addProperty("role", "user");
                messageObj.addProperty("content", prompt + ". Return only the requested data without any explanation or additional text.");
                
                JsonArray messagesArray = new JsonArray();
                messagesArray.add(messageObj);
                
                requestBody = new JsonObject();
                requestBody.addProperty("model", model);
                requestBody.add("messages", messagesArray);
                requestBody.addProperty("max_tokens", maxTokens);
                requestBody.addProperty("temperature", temperature);
            }
            
            // Create HTTP request
            RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json; charset=utf-8")
            );
            
            Request.Builder requestBuilder = new Request.Builder()
                .url(finalUrl)
                .addHeader("Content-Type", "application/json")
                .post(body);
            
            // OpenAI uses Bearer token, Gemini uses API key in URL
            if (!isGemini) {
                requestBuilder.addHeader("Authorization", "Bearer " + apiKey);
            }
            
            Request request = requestBuilder.build();
            
            // Execute request
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    logger.debug("AI API Response: {}", responseBody);
                    
                    // Parse response
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                    
                    String generatedText = null;
                    
                    if (isGemini) {
                        // Parse Gemini response format
                        JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
                        if (candidates != null && candidates.size() > 0) {
                            JsonObject candidate = candidates.get(0).getAsJsonObject();
                            JsonObject content = candidate.getAsJsonObject("content");
                            JsonArray parts = content.getAsJsonArray("parts");
                            if (parts != null && parts.size() > 0) {
                                JsonObject part = parts.get(0).getAsJsonObject();
                                generatedText = part.get("text").getAsString().trim();
                            }
                        }
                    } else {
                        // Parse OpenAI response format
                        JsonArray choices = jsonResponse.getAsJsonArray("choices");
                        if (choices != null && choices.size() > 0) {
                            JsonObject firstChoice = choices.get(0).getAsJsonObject();
                            JsonObject message = firstChoice.getAsJsonObject("message");
                            generatedText = message.get("content").getAsString().trim();
                        }
                    }
                    
                    if (generatedText != null) {
                        logger.info("Generated AI Data: {}", generatedText);
                        return generatedText;
                    }
                } else {
                    logger.warn("AI API call failed with status: {}. Using fallback.", response.code());
                    if (response.body() != null) {
                        logger.warn("Error response: {}", response.body().string());
                    }
                }
            }
            
        } catch (IOException e) {
            logger.warn("Error calling AI API: {}. Using fallback.", e.getMessage());
        } catch (Exception e) {
            logger.warn("Unexpected error calling AI API: {}. Using fallback.", e.getMessage());
            e.printStackTrace();
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
    
    /**
     * Summarize issues using Gemini API
     * Converts Ruby code to Java equivalent
     * @param issuesData Map containing issue information
     * @return Summarized text from Gemini API
     */
    public String summarizeIssues(Map<String, Object> issuesData) {
        try {
            logger.info("Summarizing issues using Gemini API");
            
            // Format the issues for summary
            String formattedText = formatIssuesForSummary(issuesData);
            
            // Build the prompt
            String prompt = buildPromptForIssueSummary(formattedText);
            
            // Build Gemini request body
            JsonObject textPart = new JsonObject();
            textPart.addProperty("text", prompt);
            
            JsonObject content = new JsonObject();
            JsonArray parts = new JsonArray();
            parts.add(textPart);
            content.add("parts", parts);
            
            JsonArray contents = new JsonArray();
            contents.add(content);
            
            JsonObject requestBody = new JsonObject();
            requestBody.add("contents", contents);
            
            // Use configured API URL instead of hardcoding
            // For Gemini, the API key goes in the URL parameter
            String geminiUrl = apiUrl + "?key=" + apiKey;
            
            // Create HTTP request
            RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json; charset=utf-8")
            );
            
            Request request = new Request.Builder()
                .url(geminiUrl)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
            
            // Execute request
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    logger.debug("Gemini API Response: {}", responseBody);
                    
                    // Parse Gemini response
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                    
                    // Extract text from Gemini response format
                    JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
                    if (candidates != null && candidates.size() > 0) {
                        JsonObject candidate = candidates.get(0).getAsJsonObject();
                        JsonObject contentObj = candidate.getAsJsonObject("content");
                        JsonArray partsArray = contentObj.getAsJsonArray("parts");
                        if (partsArray != null && partsArray.size() > 0) {
                            JsonObject part = partsArray.get(0).getAsJsonObject();
                            String summarizedText = part.get("text").getAsString().trim();
                            logger.info("Issue summary generated successfully");
                            return summarizedText;
                        }
                    }
                } else {
                    logger.error("Gemini API call failed with status: {}", response.code());
                    if (response.body() != null) {
                        logger.error("Error response: {}", response.body().string());
                    }
                }
            }
            
        } catch (IOException e) {
            logger.error("Error calling Gemini API for issue summary: {}", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Unexpected error during issue summarization: {}", e.getMessage());
            e.printStackTrace();
        }
        
        return "Failed to generate issue summary";
    }
    
    /**
     * Format issues data for summary
     * @param issuesData Map containing issue information
     * @return Formatted string representation of issues
     */
    private String formatIssuesForSummary(Map<String, Object> issuesData) {
        StringBuilder formatted = new StringBuilder();
        formatted.append("Test Execution Issues Summary:\n\n");
        
        // Extract and format issue details
        if (issuesData.containsKey("totalIssues")) {
            formatted.append("Total Issues: ").append(issuesData.get("totalIssues")).append("\n");
        }
        
        if (issuesData.containsKey("failedTests")) {
            formatted.append("Failed Tests: ").append(issuesData.get("failedTests")).append("\n");
        }
        
        if (issuesData.containsKey("errors")) {
            formatted.append("Errors: ").append(issuesData.get("errors")).append("\n");
        }
        
        if (issuesData.containsKey("issuesList") && issuesData.get("issuesList") instanceof List) {
            formatted.append("\nDetailed Issues:\n");
            @SuppressWarnings("unchecked")
            List<Map<String, String>> issuesList = (List<Map<String, String>>) issuesData.get("issuesList");
            
            int count = 1;
            for (Map<String, String> issue : issuesList) {
                formatted.append(count++).append(". ");
                formatted.append("Test: ").append(issue.getOrDefault("testName", "Unknown")).append("\n");
                formatted.append("   Error: ").append(issue.getOrDefault("error", "No details")).append("\n");
                formatted.append("   Location: ").append(issue.getOrDefault("location", "Unknown")).append("\n\n");
            }
        }
        
        return formatted.toString();
    }
    
    /**
     * Build prompt for Gemini to summarize issues
     * @param formattedText Formatted issues text
     * @return Prompt string for Gemini
     */
    private String buildPromptForIssueSummary(String formattedText) {
        return "Please analyze the following test execution issues and provide a concise summary with:\n" +
               "1. Overview of the problems\n" +
               "2. Common patterns or root causes\n" +
               "3. Suggested actions to resolve\n\n" +
               "Issues:\n" + formattedText;
    }
}

