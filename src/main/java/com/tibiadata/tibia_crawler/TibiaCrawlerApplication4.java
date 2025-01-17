package com.tibiadata.tibia_crawler;

import com.tibiadata.tibia_crawler.model.mocks.HighScoreMock;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TibiaCrawlerApplication4 {

    public static void main(String[] args) throws IOException, InterruptedException {
        ApplicationContext context = SpringApplication.run(TibiaCrawlerApplication2.class, args);
        HighScoreMock hsm = context.getBean(HighScoreMock.class);
        
        hsm.mockHighScorePlayers();
        //characterProcessor.processName("Kharsek");
    }

}
