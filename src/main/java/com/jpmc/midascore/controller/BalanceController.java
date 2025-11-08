package com.jpmc.midascore.controller;

import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRecordRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final UserRecordRepository userRecordRepository;

    public BalanceController(UserRecordRepository userRecordRepository) {
        this.userRecordRepository = userRecordRepository;
    }

    @GetMapping
    public Balance getBalance(@RequestParam("userId") Long userId) {
        UserRecord user = userRecordRepository.findById(userId).orElse(null);

        if (user != null) {
            return new Balance(user.getId(), user.getBalance());
        } else {
            // User nahin mila toh balance 0 return karo
            return new Balance(userId, 0.0f);
        }
    }
}