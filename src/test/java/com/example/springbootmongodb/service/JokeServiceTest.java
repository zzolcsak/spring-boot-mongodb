package com.example.springbootmongodb.service;

import com.example.springbootmongodb.app.model.Joke;
import com.example.springbootmongodb.db.JokeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        assertThrows(RuntimeException.class, () -> underTest.getJokes(1));
        // then
        verifyNoInteractions(jokeRepository);
    }

    @Test
    void givenJokeAlreadyInDbWhenCallingGetJokeThenRepositorySaveIsNotCalled() {
        // given
        Joke expected = new Joke(1, "general", "one", "two");
        doReturn(true).when(jokeRepository).existsById("1");
        doReturn(expected).when(restTemplate).getForObject(JokeService.URL, Joke.class);
        // when
        List<Joke> actual = underTest.getJokes(1);
        // then
        assertFalse(actual.isEmpty());
        assertEquals(List.of(expected), actual);
        verify(jokeRepository, times(0)).save(any(Joke.class));
    }

    @Test
    void givenJokeWhenCallingGetJokeThenSavesJokeAndReturnsJoke() {
        // given
        Joke expected = new Joke(1, "general", "one", "two");
        doReturn(expected).when(restTemplate).getForObject(JokeService.URL, Joke.class);
        doReturn(expected).when(jokeRepository).save(expected);
        // when
        List<Joke> actual = underTest.getJokes(1);
        // then
        assertFalse(actual.isEmpty());
        assertEquals(List.of(expected), actual);
        verify(jokeRepository).save(any(Joke.class));
    }
}
