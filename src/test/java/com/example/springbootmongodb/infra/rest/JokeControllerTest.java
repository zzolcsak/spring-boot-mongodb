package com.example.springbootmongodb.infra.rest;

import com.example.springbootmongodb.app.model.Joke;
import com.example.springbootmongodb.service.JokeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@MockitoSettings
public class JokeControllerTest {
    private static final Joke JOKE1 = new Joke(1, "general", "one", "two");
    private static final Joke JOKE2 = new Joke(2, "general", "one", "two");
    private static final Joke JOKE3 = new Joke(3, "general", "one", "two");
    private static final Joke JOKE4 = new Joke(4, "general", "one", "two");
    private static final Joke JOKE5 = new Joke(5, "general", "one", "two");
    private static final Joke JOKE6 = new Joke(6, "general", "one", "two");
    private static final Joke JOKE7 = new Joke(7, "general", "one", "two");
    private static final Joke JOKE8 = new Joke(8, "general", "one", "two");
    private static final Joke JOKE9 = new Joke(9, "general", "one", "two");
    private static final Joke JOKE10 = new Joke(10, "general", "one", "two");
    private static final Joke JOKE11 = new Joke(11, "general", "one", "two");
    @InjectMocks
    JokeController underTest;
    @Mock
    JokeService jokeService;

    @Test
    void givenCountIsThreeWhenCallingGetJokesThenReturnsThreeJokes() {
        // given
        List<Joke> expected = List.of(JOKE1, JOKE2, JOKE3);
        doReturn(expected).when(jokeService).getJokes(3);
        // when
        List<Joke> actual = underTest.getJokes(3).getBody();
        // then
        assertNotNull(actual);
        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));
    }

    @Test
    void givenCountIsElevenWhenCallingGetJokesThenReturnsElevenJokes() {
        // given
        List<Joke> expected = List.of(JOKE1, JOKE2, JOKE3, JOKE4, JOKE5, JOKE6, JOKE7, JOKE8, JOKE9, JOKE10, JOKE11);
        doReturn(expected).when(jokeService).getJokes(11);
        // when
        List<Joke> actual = underTest.getJokes(11).getBody();
        // then
        assertNotNull(actual);
        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));
    }
}
