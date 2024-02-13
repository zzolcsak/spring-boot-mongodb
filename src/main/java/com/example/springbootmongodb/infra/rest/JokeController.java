package com.example.springbootmongodb.infra.rest;

import com.example.springbootmongodb.app.model.Joke;
import com.example.springbootmongodb.service.JokeService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/jokes")
@Validated
public class JokeController {
    public static final String COUNT_TOO_HIGH_MESSAGE = "You can get no more than 100 jokes at a time";
    public static final String COUNT_TOO_LOW_MESSAGE = "You can request no less than one joke";
    private final JokeService jokeService;

    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Joke>> getJokes(@RequestParam(required = false, defaultValue = "${jokes.count.defaultValue:5}")
                                               @Min(value = 1, message = COUNT_TOO_LOW_MESSAGE)
                                               @Max(value = 100, message = COUNT_TOO_HIGH_MESSAGE) Integer count) {
        return ResponseEntity.ok(jokeService.getJokes(count));
    }
}
