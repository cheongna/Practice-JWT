package com.ehgus973.security1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {
    @GetMapping("home")
    public String home(){
        return "<h1>Home</h1>";
    }
    @PostMapping ("home")
    public String home2(){
        return "<h1>Home</h1>";
    }
}
