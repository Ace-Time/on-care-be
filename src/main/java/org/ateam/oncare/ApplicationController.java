package org.ateam.oncare;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.common.command.entity.CareLevel;
import org.ateam.oncare.common.command.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApplicationController {

    private final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    private final CommonService commonService;

    @GetMapping("/health")
    public String health() {
        return "I'm OK";
    }

    @GetMapping("/error1")
    public String error() throws IOException, InterruptedException {
            logger.debug("error 호출");
            logger.warn("에러발생 예정");
            Thread.sleep(1000);
            throw new IOException("I'm Error");
    }


    @GetMapping("/connection")
    public ResponseEntity<CareLevel> testForConnection() throws IOException {
        CareLevel careLevel = commonService.getCareLevel(1);
        return ResponseEntity.ok(careLevel);
    }
}
