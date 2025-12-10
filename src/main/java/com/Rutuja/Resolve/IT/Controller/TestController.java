package com.Rutuja.Resolve.IT.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String checkBackend() {
        return "Backend is working perfectly fine!";
    }
}
