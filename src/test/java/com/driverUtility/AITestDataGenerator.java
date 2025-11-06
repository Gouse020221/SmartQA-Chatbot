package com.driverUtility;

import com.automation.config.ConfigReader;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AITestDataGenerator {
    private static final Logger logger = LoggerFactory.getLogger(AITestDataGenerator.class);
    private static final Gson gson = new Gson();
    private static final OkHttpClient httpClient = new OkHttpClient();

    /**
     * Generate test data using AI based on the requested data type and format
     * 
     * @param dataType The type of data to generate (e.g., "user", "product", "address")
     * @param fields Fields to include in the generated data
     * @param count Number of records to generate
     * @return List of generated test data maps
     */
    public static List<Map<String, String>> generateTestData(String dataType, List<String> fields, int count) {
        List<Map<String, String>> testData = new ArrayList<>();
        
        try {
            String prompt = buildPrompt(dataType, fields, count);
            String response = callAIAPI(prompt);
            testData = parseAIResponse(response, count);
            
            logger.info("Successfully generated {} test data records for type: {}", count, dataType);
        } catch (Exception e) {
            logger.error("Error generating AI test data: {}", e.getMessage());
            // Fallback to mock data if AI fails
            testData = generateMockData(dataType, fields, count);
        }
        
        return testData;
    }

    /**
     * Generate a single test data record
     */
    public static Map<String, String> generateSingleTestData(String dataType, List<String> fields) {
        List<Map<String, String>> dataList = generateTestData(dataType, fields, 1);
        return dataList.isEmpty() ? new HashMap<>() : dataList.get(0);
    }

    /**
     * Generate test data with a custom prompt
     */
    public static String generateCustomTestData(String customPrompt) {
        try {
            return callAIAPI(customPrompt);
        } catch (Exception e) {
            logger.error("Error generating custom test data: {}", e.getMessage());
            return "Error: Unable to generate test data";
        }
    }

    /**
     * Build the prompt for AI API
     */
    private static String buildPrompt(String dataType, List<String> fields, int count) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate ").append(count).append(" realistic test data records for a ").append(dataType);
        prompt.append(" with the following fields: ").append(String.join(", ", fields));
        prompt.append(". Return the data as a JSON array where each object contains these fields.");
        prompt.append(" Ensure the data is realistic and suitable for automated testing.");
        prompt.append(" Return only valid JSON, no additional text.");
        
        return prompt.toString();
    }

    /**
     * Call the AI API (OpenAI compatible)
     */
    private static String callAIAPI(String prompt) throws IOException {
        String apiKey = ConfigReader.getAiApiKey();
        
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("AIzaSyC0zdFP3Aj5V4jHrxcmVbJWMyveK5N-yns")) {
            logger.warn("AI API key not configured. Using mock data generator.");
            throw new IOException("AI API key not configured");
        }

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", ConfigReader.getAiModel());
        
        JsonArray messages = new JsonArray();
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);
        messages.add(message);
        
        requestBody.add("messages", messages);
        requestBody.addProperty("temperature", ConfigReader.getAiTemperature());
        requestBody.addProperty("max_tokens", ConfigReader.getAiMaxTokens());

        RequestBody body = RequestBody.create(
            requestBody.toString(),
            MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
            .url(ConfigReader.getAiApiUrl())
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .post(body)
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            
            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
            
            if (jsonResponse.has("choices") && jsonResponse.getAsJsonArray("choices").size() > 0) {
                JsonObject choice = jsonResponse.getAsJsonArray("choices").get(0).getAsJsonObject();
                JsonObject messageObj = choice.getAsJsonObject("message");
                return messageObj.get("content").getAsString();
            }
            
            throw new IOException("Invalid response from AI API");
        }
    }

    /**
     * Parse AI response into test data maps
     */
    private static List<Map<String, String>> parseAIResponse(String response, int count) {
        List<Map<String, String>> testData = new ArrayList<>();
        
        try {
            // Try to extract JSON array from response
            String jsonContent = extractJsonFromResponse(response);
            JsonArray jsonArray = gson.fromJson(jsonContent, JsonArray.class);
            
            for (int i = 0; i < jsonArray.size() && i < count; i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                Map<String, String> dataMap = new HashMap<>();
                
                jsonObject.entrySet().forEach(entry -> {
                    dataMap.put(entry.getKey(), entry.getValue().getAsString());
                });
                
                testData.add(dataMap);
            }
        } catch (Exception e) {
            logger.error("Error parsing AI response: {}", e.getMessage());
            throw new RuntimeException("Failed to parse AI response", e);
        }
        
        return testData;
    }

    /**
     * Extract JSON from AI response (handles markdown code blocks)
     */
    private static String extractJsonFromResponse(String response) {
        // Remove markdown code blocks if present
        response = response.trim();
        if (response.startsWith("```json")) {
            response = response.substring(7);
        }
        if (response.startsWith("```")) {
            response = response.substring(3);
        }
        if (response.endsWith("```")) {
            response = response.substring(0, response.length() - 3);
        }
        return response.trim();
    }

    /**
     * Fallback mock data generator when AI is unavailable
     */
    private static List<Map<String, String>> generateMockData(String dataType, List<String> fields, int count) {
        List<Map<String, String>> mockData = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            Map<String, String> dataMap = new HashMap<>();
            
            for (String field : fields) {
                String value = generateMockValue(field, i);
                dataMap.put(field, value);
            }
            
            mockData.add(dataMap);
        }
        
        logger.info("Generated {} mock test data records for type: {}", count, dataType);
        return mockData;
    }

    /**
     * Generate mock value based on field name
     */
    private static String generateMockValue(String fieldName, int index) {
        String lowerField = fieldName.toLowerCase();
        
        if (lowerField.contains("email")) {
            return "testuser" + index + "@example.com";
        } else if (lowerField.contains("name") || lowerField.contains("firstname")) {
            return "TestUser" + index;
        } else if (lowerField.contains("lastname") || lowerField.contains("surname")) {
            return "LastName" + index;
        } else if (lowerField.contains("phone")) {
            return "+123456789" + String.format("%02d", index);
        } else if (lowerField.contains("address")) {
            return index + " Test Street";
        } else if (lowerField.contains("city")) {
            return "TestCity" + index;
        } else if (lowerField.contains("zip") || lowerField.contains("postal")) {
            return "1234" + String.format("%02d", index);
        } else if (lowerField.contains("country")) {
            return "USA";
        } else if (lowerField.contains("password")) {
            return "Test@123" + index;
        } else if (lowerField.contains("username")) {
            return "testuser" + index;
        } else {
            return "TestValue" + index;
        }
    }
}

