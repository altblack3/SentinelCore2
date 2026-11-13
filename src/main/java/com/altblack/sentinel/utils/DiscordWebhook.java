package com.altblack.sentinel.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscordWebhook {
    private final String webhookUrl;
    private final String botName;
    private final HttpClient httpClient;
    private final Logger logger;

    public DiscordWebhook(String webhookUrl, String botName, Logger logger) {
        this.webhookUrl = webhookUrl;
        this.botName = botName;
        this.logger = logger;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public void sendCrashAlert(String title, String stackTrace, String serverVersion) {
        if (webhookUrl == null || webhookUrl.isEmpty() || webhookUrl.contains("YOUR_WEBHOOK_ID")) {
            return;
        }

        if (stackTrace.length() > 3000) {
            stackTrace = stackTrace.substring(0, 3000) + "\n... [Truncated]";
        }

        String jsonPayload = String.format(
                "{\"username\": \"%s\", \"embeds\": [{" +
                        "\"title\": \"🚨 CRITICAL CRASH / EXCEPTION: %s\"," +
                        "\"description\": \"```java\n%s\n```\"," +
                        "\"color\": 15158332," +
                        "\"fields\": [{\"name\": \"Server Environment\", \"value\": \"%s\", \"inline\": false}]," +
                        "\"timestamp\": \"%s\"" +
                        "}]}",
                escapeJson(botName),
                escapeJson(title),
                escapeJson(stackTrace),
                escapeJson(serverVersion),
                java.time.Instant.now().toString()
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .whenComplete((response, throwable) -> {
                    if (throwable != null) {
                        logger.log(Level.WARNING, "Failed to send Discord crash webhook alert", throwable);
                    } else if (response.statusCode() != 204 && response.statusCode() != 200) {
                        logger.warning("Discord webhook responded with status code: " + response.statusCode());
                    }
                });
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}