package com.example.springbootmongodb.db;

import com.example.springbootmongodb.app.model.Joke;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JokeRepository extends MongoRepository<Joke, String> {
}
