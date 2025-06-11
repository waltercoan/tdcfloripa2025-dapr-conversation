package br.com.waltercoan.app_b.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

    @PostMapping()
    public ResponseEntity index(@RequestBody String message) {
        System.out.println("App B received message: " + message);
        return ResponseEntity.ok().body("Hello from App B");
    }
}
