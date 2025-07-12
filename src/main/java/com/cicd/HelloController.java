package com.cicd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World! JENKINS1231234214241";
    }

    @GetMapping("/bye")
    public String Bye() {
        return "Bye, World! JENKINS";
    }
}
