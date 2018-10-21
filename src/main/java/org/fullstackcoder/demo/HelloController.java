package org.fullstackcoder.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public Hello hello() {
        return new Hello(1, "Hello world!");
    }
}
