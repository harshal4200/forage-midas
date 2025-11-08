package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveService {

    private final RestTemplate restTemplate;
    private final String incentiveApiUrl = "http://localhost:8080/incentive";

    public IncentiveService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public float getIncentiveAmount(Transaction transaction) {
        try {
            Incentive incentive = restTemplate.postForObject(incentiveApiUrl, transaction, Incentive.class);
            return incentive != null ? incentive.getAmount() : 0.0f;
        } catch (Exception e) {
            System.out.println("Error calling incentive API: " + e.getMessage());
            return 0.0f;
        }
    }
}