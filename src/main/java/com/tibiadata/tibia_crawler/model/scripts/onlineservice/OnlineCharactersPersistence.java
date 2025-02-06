package com.tibiadata.tibia_crawler.model.scripts.onlineservice;

import com.tibiadata.tibia_crawler.model.scripts.serversservice.ServersVerifierFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OnlineCharactersPersistence {
    private static final Logger logger = LoggerFactory.getLogger(OnlineCharactersPersistence.class);
    //
    private final OnlineCharacterProcessor onlineCharacterProcessor;

    @Autowired
    public OnlineCharactersPersistence(OnlineCharacterProcessor onlineCharacterProcessor) {
        this.onlineCharacterProcessor = onlineCharacterProcessor;
    }

    public void serverSaveCharactersPersistence(Map<String, Map<String, Long>> worldTotalPlayers) {

        for (var worldEntry : worldTotalPlayers.entrySet()) {// mundos
            logger.debug("SALVANDO PERSONAGENS NO SERVER SAVE DO SERVIDOR: {}", worldEntry.getKey());
            for (var playerEntry : worldEntry.getValue().entrySet()) {//personagens
                onlineCharacterProcessor.onlineCharacter(playerEntry.getKey(), playerEntry.getValue().intValue());
            }
        }
    }
}
