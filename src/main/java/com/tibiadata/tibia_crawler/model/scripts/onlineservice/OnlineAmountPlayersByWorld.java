package com.tibiadata.tibia_crawler.model.scripts.onlineservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OnlineAmountPlayersByWorld {
    private static final Logger logger = LoggerFactory.getLogger(OnlineAmountPlayersByWorld.class);
    private final OnlineWorldPlayersProcessor onlineWorldPlayersProcessor;

    @Autowired
    public OnlineAmountPlayersByWorld(OnlineWorldPlayersProcessor onlineWorldPlayersProcessor) {
        this.onlineWorldPlayersProcessor = onlineWorldPlayersProcessor;
    }

    public int worldTypes(List<String> worlds) {
        int amount = 0;
        for (String world : worlds) {
            amount = amount + onlineWorldPlayersProcessor.processWorld(world).size();
        }

        logger.debug("{} jogadores online.", amount);

        return amount;
    }

    public int uniqueWorld(String world) {
        int amount = onlineWorldPlayersProcessor.processWorld(world).size();
        logger.debug("{} jogadores online.", amount);

        return amount;
    }
}
