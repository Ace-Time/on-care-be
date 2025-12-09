package org.ateam.oncare.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @GetMapping("/health")
    public String health() {
        return "I'm OK";
    }

    @GetMapping("/error1")
    public String error() throws IOException, InterruptedException {
            logger.debug("error 호출");
            logger.warn("곧 에러발생 예정");
            Thread.sleep(1000);
            throw new IOException("I'm Error");
    }
}
