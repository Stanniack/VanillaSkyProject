package com.tibiadata.tibia_crawler;

import com.tibiadata.tibia_crawler.model.parsers.characterservice.CharacterService;
import com.tibiadata.tibia_crawler.model.parsers.characterservice.CharacterService2;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TibiaCrawlerApplication2 {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = SpringApplication.run(TibiaCrawlerApplication2.class, args);
        CharacterService2 characterService = context.getBean(CharacterService2.class);

        characterService.fetchCharacter("https://www.tibia.com/community/?subtopic=characters&name=Talles Lionheart");
    }

}
