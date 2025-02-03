package com.tibiadata.tibia_crawler.model.scripts.onlineservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class OnlineWorldPlayersProcessorTest {
    @Autowired
    private OnlineWorldPlayersProcessor onlineWorldPlayersProcessor;

    @Test
    public void testOnlineWorldPlayersProcessor(){
        assertTrue("A lista está retornando vazia", !onlineWorldPlayersProcessor.processWorld("Antica").isEmpty());
    }
}
