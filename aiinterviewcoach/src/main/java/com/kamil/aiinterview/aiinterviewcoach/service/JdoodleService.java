package com.kamil.aiinterview.aiinterviewcoach.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JdoodleService {
	private final RestTemplate restTemplate = new RestTemplate();
	private final String clientId = "37554e68892281e73551cc86ceebfab";
	private final String clientSecret = "fe44b78ecd509ac0d404ef484643f0c13c4608199bfa10d25afff234e570fd9c";
	private final String API_URL = "https://api.jdoodle.com/v1/execute";
	
	public String runCode(String code, String language) {
        Map<String, Object> request = new HashMap<>();
        request.put("clientId", clientId);
        request.put("clientSecret", clientSecret);
        request.put("script", code);
        request.put("language", language);
        request.put("versionIndex", "0");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            return root.path("output").asText();
        } catch (Exception e) {
            return "Error running code: " + e.getMessage();
        }
    }
}
