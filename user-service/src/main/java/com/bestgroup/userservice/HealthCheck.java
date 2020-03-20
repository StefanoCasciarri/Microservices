package com.bestgroup.userservice;

import com.bestgroup.userservice.Exceptions.OtherServiceNotRespondingException;
import com.bestgroup.userservice.responseentitystructure.RoomBooking;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
            HttpEntity entity = this.createTokenHeader();
            response = restTemplate.exchange("http://localhost:8070/health",  HttpMethod.GET, entity, String.class);
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

    private String getTokenFromRequest(){
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder
                        .currentRequestAttributes())
                .getRequest();
        String value = request.getHeader("Authorization").split(" ")[1];
        return value;
    }

    private HttpEntity createTokenHeader(){
        String token = this.getTokenFromRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return new HttpEntity(headers);
    }

}
