package org.ateam.oncare.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @GetMapping("/health")
    public String health() {
        return "I'm OK";
    }

    @GetMapping("/error1")
    public String error() {
        try{
            logger.debug("error 호출");
            logger.warn("곧 에러발생 예정");
            Thread.sleep(1000);
            throw new Exception("I'm Error");
        } catch(Exception e){
            logger.error(e.getMessage());
        }
        return "I'm OK";
    }
}
