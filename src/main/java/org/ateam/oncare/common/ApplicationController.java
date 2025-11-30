package org.ateam.oncare.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @GetMapping("/health")
    public String health() {
        return "I'm OK";
    }
}
