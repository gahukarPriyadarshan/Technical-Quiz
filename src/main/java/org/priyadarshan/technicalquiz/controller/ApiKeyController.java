package org.priyadarshan.technicalquiz.controller;


import lombok.RequiredArgsConstructor;
import org.priyadarshan.technicalquiz.service.ApiKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apikey")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    //http://localhost:8080/apikey/generateapikey/{email}
    @PostMapping("/generateapikey/{email}")
    public ResponseEntity<String> generateApiKey(@PathVariable String email) {
        return apiKeyService.generateApiKey(email);
    }

    //http://localhost:8080/apikey/getapikey/{email}
    @GetMapping("/getapikey/{email}")
    public ResponseEntity<List<String>> getApiKey(@PathVariable String email) {
        return apiKeyService.getApiKey(email);
    }

}