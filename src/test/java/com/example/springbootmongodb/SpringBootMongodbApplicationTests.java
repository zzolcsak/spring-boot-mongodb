package com.example.springbootmongodb;

import com.example.springbootmongodb.infra.rest.JokeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpringBootMongodbApplicationTests {
	@Autowired
	MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void givenCountTooHighWhenCallingGetJokesThenShouldReturnBadRequest() throws Exception {
		mockMvc.perform(get("/jokes?count=101"))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().string(is(JokeController.COUNT_TOO_HIGH_MESSAGE)));
	}

	@Test
	void givenCountTooLowWhenCallingGetJokesThenShouldReturnBadRequest() throws Exception {
		mockMvc.perform(get("/jokes?count=0"))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().string(is(JokeController.COUNT_TOO_LOW_MESSAGE)));
	}

	@Test
	void shouldReturnInternalServerError() throws Exception {
		mockMvc.perform(get("/jokes?count=parseThis"))
				.andDo(print())
				.andExpect(status().isInternalServerError())
				.andExpect(content().string(is("Internal server error")));
	}

}
