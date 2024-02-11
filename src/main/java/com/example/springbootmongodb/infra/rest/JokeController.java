package com.example.springbootmongodb.infra.rest;

import com.example.springbootmongodb.app.model.Joke;
import com.example.springbootmongodb.service.JokeService;
import jakarta.validation.constraints.Max;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/jokes")
@Validated
public class JokeController {
    public static final int DEFAULT_NUMBER_OF_JOKES = 5;
    private final JokeService jokeService;
    private final ExecutorService executorService;

    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
        executorService = Executors.newFixedThreadPool(10);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Joke[]> getJokes(@RequestParam(required = false) @Max(value = 100, message = "You can get no\n" +
            "more than 100 jokes at a time") Integer count) {
        int c = count == null || count < 1 ? DEFAULT_NUMBER_OF_JOKES : count;
        Callable<Optional<Joke>> getJoke = jokeService::getJoke;
        List<Joke> jokes = new ArrayList<>();
        for (int remaining = c; c > 0; c -= 10) {
            jokes.addAll(Stream.generate(() -> getJoke)
                    .limit(Math.min(10, remaining))
                    .map(executorService::submit)
                    .toList()
                    .stream()
                    .map(this::getResult)
                    .map(o -> o.orElseThrow(RuntimeException::new))
                    .toList());

        }
        return ResponseEntity.ok(jokes.toArray(Joke[]::new));
    }

    private Optional<Joke> getResult(Future<Optional<Joke>> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
