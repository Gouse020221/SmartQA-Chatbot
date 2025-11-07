package com.driverUtility;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

/**
 * Simple HTTP Server to receive Teams bot webhook commands
 * This enables two-way communication: Teams -> Bot -> Jenkins
 * 
 * Usage:
 * 1. Deploy this server (or use a cloud service like Azure Functions)
 * 2. Configure Teams bot webhook to point to this server
 * 3. Users can send commands in Teams chat to trigger Jenkins builds
 */
public class TeamsBotWebhookServer {
    private static final Logger logger = LoggerFactory.getLogger(TeamsBotWebhookServer.class);
    private static HttpServer server;
    private static final int DEFAULT_PORT = 8080;

    /**
     * Start the webhook server
     */
    public static void startServer(int port) {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/teams/webhook", new TeamsWebhookHandler());
            server.createContext("/health", new HealthCheckHandler());
            server.setExecutor(null);
            server.start();
            logger.info("Teams Bot Webhook Server started on port {}", port);
            logger.info("Webhook endpoint: http://localhost:{}/teams/webhook", port);
        } catch (IOException e) {
            logger.error("Failed to start Teams webhook server: {}", e.getMessage(), e);
        }
    }

    /**
     * Stop the webhook server
     */
    public static void stopServer() {
        if (server != null) {
            server.stop(0);
            logger.info("Teams Bot Webhook Server stopped");
        }
    }

    /**
     * Handler for Teams webhook requests
     */
    static class TeamsWebhookHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                try {
                    // Read request body
                    String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    logger.info("Received Teams webhook: {}", requestBody);
                    
                    // Parse JSON request
                    JsonObject request = JsonParser.parseString(requestBody).getAsJsonObject();
                    
                    // Extract command from Teams message
                    String command = extractCommand(request);
                    String userId = extractUserId(request);
                    String channelId = extractChannelId(request);
                    
                    // Process command
                    String response = TeamsChatOpsIntegration.processTeamsCommand(command, userId, channelId);
                    
                    // Create Teams response card
                    String responseCard = createTeamsResponseCard(response);
                    
                    // Send response
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, responseCard.getBytes(StandardCharsets.UTF_8).length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(responseCard.getBytes(StandardCharsets.UTF_8));
                    os.close();
                    
                    logger.info("Sent response to Teams: {}", response);
                } catch (Exception e) {
                    logger.error("Error processing Teams webhook: {}", e.getMessage(), e);
                    sendErrorResponse(exchange, 500, "Internal server error");
                }
            } else {
                sendErrorResponse(exchange, 405, "Method not allowed");
            }
        }

        private String extractCommand(JsonObject request) {
            // Teams sends messages in different formats
            // Try to extract from text field or message field
            if (request.has("text")) {
                return request.get("text").getAsString();
            }
            if (request.has("message")) {
                JsonObject message = request.getAsJsonObject("message");
                if (message.has("text")) {
                    return message.get("text").getAsString();
                }
            }
            if (request.has("value")) {
                JsonObject value = request.getAsJsonObject("value");
                if (value.has("text")) {
                    return value.get("text").getAsString();
                }
            }
            return "";
        }

        private String extractUserId(JsonObject request) {
            if (request.has("from")) {
                JsonObject from = request.getAsJsonObject("from");
                if (from.has("id")) {
                    return from.get("id").getAsString();
                }
                if (from.has("name")) {
                    return from.get("name").getAsString();
                }
            }
            return "Unknown";
        }

        private String extractChannelId(JsonObject request) {
            if (request.has("channel")) {
                JsonObject channel = request.getAsJsonObject("channel");
                if (channel.has("id")) {
                    return channel.get("id").getAsString();
                }
            }
            return "Unknown";
        }

        private String createTeamsResponseCard(String message) {
            JsonObject card = new JsonObject();
            card.addProperty("type", "message");
            
            JsonObject attachment = new JsonObject();
            attachment.addProperty("contentType", "application/vnd.microsoft.card.adaptive");
            
            JsonObject content = new JsonObject();
            content.addProperty("type", "AdaptiveCard");
            content.addProperty("$schema", "http://adaptivecards.io/schemas/adaptive-card.json");
            content.addProperty("version", "1.4");
            
            com.google.gson.JsonArray body = new com.google.gson.JsonArray();
            JsonObject textBlock = new JsonObject();
            textBlock.addProperty("type", "TextBlock");
            textBlock.addProperty("text", message);
            textBlock.addProperty("wrap", true);
            body.add(textBlock);
            
            content.add("body", body);
            attachment.add("content", content);
            
            com.google.gson.JsonArray attachments = new com.google.gson.JsonArray();
            attachments.add(attachment);
            card.add("attachments", attachments);
            
            return card.toString();
        }

        private void sendErrorResponse(HttpExchange exchange, int code, String message) throws IOException {
            exchange.sendResponseHeaders(code, message.length());
            OutputStream os = exchange.getResponseBody();
            os.write(message.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }

    /**
     * Health check handler
     */
    static class HealthCheckHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "{\"status\":\"healthy\",\"service\":\"Teams Bot Webhook Server\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }
}

