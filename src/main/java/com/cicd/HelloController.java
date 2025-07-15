package com.cicd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "JENKINS~ END~~~~~!!!~₩~!!!, Please2~~~!!";
    }

    @GetMapping("/bye")
    public String Bye() {
        return "ARGO END~~~@@@~~~~!!, Please2~~~!!";
    }
}
