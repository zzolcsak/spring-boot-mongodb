package com.example.springbootmongodb.service;

import com.example.springbootmongodb.app.model.Joke;
import com.example.springbootmongodb.db.JokeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@MockitoSettings
public class JokeServiceTest {
    private JokeService underTest;
    @Mock
    private JokeRepository jokeRepository;
    @Mock
    RestTemplateBuilder restTemplateBuilder;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setupClass() {
        doReturn(restTemplate).when(restTemplateBuilder).build();
        underTest = new JokeService(restTemplateBuilder, jokeRepository);
    }

    @Test
    void givenRestTemplateReturnsNullWhenCallingGetJokeThenReturnsEmptyOptional() {
        // when
        Optional<Joke> actual = underTest.getJoke();
        // then
        assertTrue(actual.isEmpty());
        verifyNoInteractions(jokeRepository);
    }

    @Test
    void givenJokeAlreadyInDbWhenCallingGetJokeThenRepositorySaveIsNotCalled() {
        // given
        Joke expected = new Joke(1, "general", "one", "two");
        doReturn(true).when(jokeRepository).existsById("1");
        doReturn(expected).when(restTemplate).getForObject(JokeService.URL, Joke.class);
        // when
        Optional<Joke> actual = underTest.getJoke();
        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
        verify(jokeRepository, times(0)).save(any(Joke.class));
    }

    @Test
    void givenJokeWhenCallingGetJokeThenSavesJokeAndReturnsJoke() {
        // given
        Joke expected = new Joke(1, "general", "one", "two");
        doReturn(expected).when(restTemplate).getForObject(JokeService.URL, Joke.class);
        // when
        Optional<Joke> actual = underTest.getJoke();
        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
        verify(jokeRepository).save(any(Joke.class));
    }
}
