package com.tibiadata.tibia_crawler.model.scripts.onlineservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OnlineCharactersPersistence {
    private final OnlineCharacterProcessor onlineCharacterProcessor;

    @Autowired
    public OnlineCharactersPersistence(OnlineCharacterProcessor onlineCharacterProcessor) {
        this.onlineCharacterProcessor = onlineCharacterProcessor;
    }

    public void serverSaveCharactersPersistence(Map<String, Map<String, Long>> worldTotalPlayers) {
        for (var worldEntry : worldTotalPlayers.entrySet()) {// mundos
            for (var playerEntry : worldEntry.getValue().entrySet()) {//personagens
                onlineCharacterProcessor.onlineCharacter(playerEntry.getKey(), playerEntry.getValue().intValue());
            }
        }
    }
}
