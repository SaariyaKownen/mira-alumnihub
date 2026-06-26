package com.alumni.portal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${anthropic.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
        .baseUrl("https://generativelanguage.googleapis.com")
        .codecs(configurer -> configurer
            .defaultCodecs()
            .maxInMemorySize(2 * 1024 * 1024))
        .build();
    public String callAI(String prompt) {
        int maxRetries = 2;
        Exception lastError = null;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                Map<String, Object> textPart = new HashMap<>();
                textPart.put("text", prompt);

                List<Map<String, Object>> parts = new ArrayList<>();
                parts.add(textPart);

                Map<String, Object> content = new HashMap<>();
                content.put("parts", parts);

                List<Map<String, Object>> contents = new ArrayList<>();
                contents.add(content);

                Map<String, Object> body = new HashMap<>();
                body.put("contents", contents);

                String response = webClient.post()
                    .uri("/v1/models/gemini-2.5-flash:generateContent" +
                         "?key=" + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

                JsonNode node = new ObjectMapper().readTree(response);
                return node.get("candidates").get(0)
                           .get("content").get("parts").get(0)
                           .get("text").asText();

            } catch (Exception e) {
                lastError = e;
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ignored) {}
            }
        }

        String error = lastError != null ? lastError.getMessage() : "Unknown";
        if (error != null && error.contains("429")) {
            return "⏳ Too many requests right now. Please wait 30 seconds and try again.";
        }
        if (error != null && (error.contains("resolve") ||
                              error.contains("UnknownHost") ||
                              error.contains("Connection"))) {
            return "📡 Connection issue reaching the AI service. " +
                   "Please check your internet connection and try again in a moment.";
        }
        return "🤖 AI is taking a short break. Please try again shortly.";
    }
    // Feature 1: Career Advisor
    public String getCareerAdvice(String fullName, String currentJob,
                                   String company, String graduationYear) {
        String prompt = "You are a friendly career advisor for alumni. " +
            "Give personalized career advice for this person: " +
            "Name: " + fullName + ", " +
            "Current Job: " + currentJob + ", " +
            "Company: " + company + ", " +
            "Graduation Year: " + graduationYear + ". " +
            "Please provide: " +
            "1) 3 specific career growth suggestions " +
            "2) 2 skill recommendations " +
            "3) 2 potential next job roles. " +
            "Use emojis, be friendly and motivating. " +
            "Keep response under 300 words.";
        return callAI(prompt);
    }

    // Feature 2: Bio Generator
    public String generateBio(String fullName, String currentJob,
                               String company, String graduationYear) {
        String prompt = "Write a professional LinkedIn-style bio for: " +
            "Name: " + fullName + ", " +
            "Job Title: " + currentJob + ", " +
            "Company: " + company + ", " +
            "Graduation Year: " + graduationYear + ". " +
            "Write exactly 3-4 sentences. " +
            "Make it professional, confident and impressive. " +
            "Write in first person. " +
            "Do not use any placeholder text.";
        return callAI(prompt);
    }

    // Feature 3: Message Assistant
    public String generateMessage(String senderName, String receiverName,
                                   String receiverJob, String purpose) {
        String prompt = "Write a short professional networking message. " +
            "From: " + senderName + ", " +
            "To: " + receiverName + " who is a " + receiverJob + ". " +
            "Purpose: " + purpose + ". " +
            "Keep it under 100 words. " +
            "Friendly but professional tone. " +
            "No subject line. Just the message body. " +
            "End with the sender's name.";
        return callAI(prompt);
    }

    // Feature 4: Smart Search
    public String smartSearch(String query, String alumniData) {
        String prompt = "You are searching through an alumni database. " +
            "Here is the alumni data: " + alumniData + ". " +
            "User is searching for: '" + query + "'. " +
            "Find and list all matching alumni. " +
            "For each match show: Name, Email, Job Title. " +
            "Use emojis to format nicely. " +
            "If no matches found, say 'No alumni found for this search.'";
        return callAI(prompt);
    }
}