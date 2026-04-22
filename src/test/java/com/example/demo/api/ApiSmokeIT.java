package com.example.demo.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiSmokeIT {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate rest;

  @Test
  void root_works() {
    var res = rest.getForEntity("http://localhost:" + port + "/", String.class);
    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void hello_works() {
    var res = rest.getForEntity("http://localhost:" + port + "/api/hello", String.class);
    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void health_works() {
    var res = rest.getForEntity("http://localhost:" + port + "/actuator/health", String.class);
    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}

