package com.example.springbootmongodb.service;

import com.example.springbootmongodb.app.model.Joke;
import com.example.springbootmongodb.db.JokeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@Service
@Slf4j
public class JokeService {
    public static final String URL = "https://official-joke-api.appspot.com/random_joke";
    public static final int N_THREADS = 10;
    private final RestTemplate restTemplate;
    private final JokeRepository jokeRepository;

    public JokeService(RestTemplateBuilder restTemplateBuilder, JokeRepository jokeRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.jokeRepository = jokeRepository;
    }

    public List<Joke> getJokes(int count) {
        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        try {
            return IntStream.rangeClosed(1, count)
                    .parallel()
                    .mapToObj(i -> executorService.submit(this::getJoke))
                    .map(this::getResult)
                    .map(o -> o.orElseThrow(RuntimeException::new))
                    .toList();
        } finally {
            executorService.shutdown();
        }
    }

    private Optional<Joke> getResult(Future<Optional<Joke>> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Exception while waiting for joke: ", e);
            throw new RuntimeException(e);
        }
    }

    private Optional<Joke> getJoke() {
        return Optional.ofNullable(this.restTemplate.getForObject(URL, Joke.class))
                .map(j -> jokeRepository.existsById(j.getId() + "") ? j : jokeRepository.save(j));
    }
}
