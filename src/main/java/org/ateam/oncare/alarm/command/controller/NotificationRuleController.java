package org.ateam.oncare.alarm.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.alarm.command.entity.NotificationRule;
import org.ateam.oncare.alarm.command.repository.NotificationRuleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alarms/rules")
@RequiredArgsConstructor
public class NotificationRuleController {

    private final NotificationRuleRepository ruleRepository;

    // 규칙 등록/수정 (POST)
    @PostMapping
    public ResponseEntity<NotificationRule> saveRule(@RequestBody NotificationRule rule) {
        NotificationRule savedRule = ruleRepository.save(rule);
        return ResponseEntity.ok(savedRule);
    }

    // 규칙 목록 조회 (GET)
    @GetMapping
    public ResponseEntity<List<NotificationRule>> getRules() {
        return ResponseEntity.ok(ruleRepository.findAll());
    }

    // 규칙 삭제 (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        ruleRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
