package com.example.demo.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTest {
  @Autowired private MockMvc mvc;

  @Test
  void root_returns200() throws Exception {
    mvc.perform(get("/")).andExpect(status().isOk());
  }

  @Test
  void hello_returns200() throws Exception {
    mvc.perform(get("/api/hello")).andExpect(status().isOk());
  }
}

