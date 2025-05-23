package com.kamil.aiinterview.aiinterviewcoach.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenRouterService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();

    private final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private final String API_TOKEN = "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";

    public String generateQuestion(String prompt) {
        try {
            // Create the request payload
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "openai/gpt-3.5-turbo");
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", prompt));
            requestBody.put("messages", messages);

            // Convert to JSON safely
            String json = objectMapper.writeValueAsString(requestBody);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(API_TOKEN);

            HttpEntity<String> entity = new HttpEntity<>(json, headers);

            // Make the request
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);

            // Extract the response text
            return objectMapper
                .readTree(response.getBody())
                .path("choices").get(0)
                .path("message")
                .path("content")
                .asText();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating response.";
        }
    }
}