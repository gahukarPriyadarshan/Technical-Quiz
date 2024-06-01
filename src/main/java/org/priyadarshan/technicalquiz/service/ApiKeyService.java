package org.priyadarshan.technicalquiz.service;

import lombok.RequiredArgsConstructor;
import org.priyadarshan.technicalquiz.dao.ApiKeyDao;
import org.priyadarshan.technicalquiz.dao.UsersDao;
import org.priyadarshan.technicalquiz.pojo.ApiKey;
import org.priyadarshan.technicalquiz.utility.ApiKeyGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    public final UsersDao usersDao;
    public final ApiKeyDao apiKeyDao;
    public final ApiKeyGenerator apiKeyGenerator;

    public ResponseEntity<String> generateApiKey(String email) {

        if (usersDao.findByEmail(email).isPresent()) {
            long apiKeyCount = apiKeyDao.countByEmail(email);
            if (apiKeyCount < 2) {
                try {
                    String apiKey = apiKeyGenerator.generateApiKey(email);
                    ApiKey apiKeyObj = new ApiKey();
                    apiKeyObj.setEmail(email);
                    apiKeyObj.setApiKey(apiKey);
                    apiKeyDao.save(apiKeyObj);
                    return ResponseEntity.ok("API key generated successfully");
                } catch (Exception e) {
                    String errorMessage = "{\"error\": \"An error occurred while processing your request\"}";
                    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
                }
            }
            else{
                String errorMessage = "{\"error\": \"Maximum number of API keys reached.\"}";
                return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
            }
        } else {
            String errorMessage = "{\"error\": \"User not found Please Register\"}";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<String>> getApiKey(String email) {
        Optional<List<ApiKey>> apiKey = apiKeyDao.findByEmail(email);

        List<String> apiKeyList = new ArrayList<>();

        if (!apiKey.orElse(Collections.emptyList()).isEmpty()) {
            for (ApiKey key : apiKey.get()) {
                apiKeyList.add(key.getApiKey());
            }
            return ResponseEntity.ok(apiKeyList);}
        else{
            List<String> errorMessage = new ArrayList<>();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }
}