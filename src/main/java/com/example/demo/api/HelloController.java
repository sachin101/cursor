package com.example.demo.api;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/")
  public Map<String, Object> root() {
    return Map.of(
        "service", "java-program",
        "status", "ok",
        "ts", Instant.now().toString()
    );
  }

  @GetMapping("/api/hello")
  public Map<String, Object> hello() {
    return Map.of("message", "Hello from Spring Boot");
  }
}

