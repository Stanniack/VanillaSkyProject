package com.tibiadata.tibia_crawler;

import com.tibiadata.tibia_crawler.model.scripts.GetCharacter;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TibiaCrawlerApplication {

    public static void main(String[] args) throws IOException {
        ApplicationContext context = SpringApplication.run(TibiaCrawlerApplication.class, args);
        GetCharacter getCharacter = context.getBean(GetCharacter.class);

        getCharacter.getCharacter("https://www.tibia.com/community/?subtopic=characters&name=Vepeeh");
    }

}
