package com.example.springbootmongodb.service;

import com.example.springbootmongodb.app.model.Joke;
import com.example.springbootmongodb.db.JokeRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class JokeService {
    public static final String URL = "https://official-joke-api.appspot.com/random_joke";
    private final RestTemplate restTemplate;
    private final JokeRepository jokeRepository;

    public JokeService(RestTemplateBuilder restTemplateBuilder, JokeRepository jokeRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.jokeRepository = jokeRepository;
    }

    public Optional<Joke> getJoke() {
        Joke joke = this.restTemplate.getForObject(URL, Joke.class);
        if (joke != null && !jokeRepository.existsById(joke.getId() + "")) {
            jokeRepository.save(joke);
        }
        return Optional.ofNullable(joke);
    }
}
