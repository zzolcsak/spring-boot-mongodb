package com.example.springbootmongodb.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("joke")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Joke {

    @Id
    private int id;
    private String type;
    private String setup;
    private String punchline;
}
