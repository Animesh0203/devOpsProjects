package com.example.studentapi.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class OllamaService {

    private RestTemplate restTemplate = new RestTemplate();

    public String generateSummary(String prompt) {
        String url = "http://localhost:11434/api/generate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request body
        String requestBody = String.format("{\"model\":\"llama3\",\"prompt\":\"%s\"}", prompt);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<OllamaResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, OllamaResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().getResponse();
        } else {
            throw new RestClientException("Failed to get response from Ollama");
        }
    }

    public static class OllamaResponse {
        private String response;

        public OllamaResponse() {
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }
}
