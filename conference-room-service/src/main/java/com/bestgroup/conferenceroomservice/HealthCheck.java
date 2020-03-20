package com.bestgroup.conferenceroomservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@Component
@NoArgsConstructor
public class HealthCheck {

    private RestTemplate restTemplate = new RestTemplate();
    private JsonNode root;
    private ResponseEntity<String> response;

    public boolean isStatusUp()  {
        if(!tryGettingResponseEntity()) {return false;}
        if(!tryReadingTreeFromResponseEntity()) {return false;}
        return root.path("status").asText().equals("UP") ? true : false;
    }

    private boolean tryGettingResponseEntity() {
        try {
            response = restTemplate.getForEntity("http://localhost:8090/health", String.class);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean tryReadingTreeFromResponseEntity() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            root = mapper.readTree(response.getBody());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

}
