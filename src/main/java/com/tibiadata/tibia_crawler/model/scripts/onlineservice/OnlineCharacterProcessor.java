package com.tibiadata.tibia_crawler.model.scripts.onlineservice;

import com.tibiadata.tibia_crawler.model.scripts.characterservice.CharacterProcessor;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.OnlineTimeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
@Scope("prototype")
public class OnlineCharacterProcessor {

    private final CharacterProcessor characterProcessor;
    private final OnlineTimeStrategy onlineTimeStrategy;

    @Autowired
    public OnlineCharacterProcessor(CharacterProcessor characterProcessor, OnlineTimeStrategy onlineTimeStrategy) {
        this.characterProcessor = characterProcessor;
        this.onlineTimeStrategy = onlineTimeStrategy;
    }

    public void onlineCharacter(String name, Integer onlineMs) {
        Integer persongeId = characterProcessor.processName(name);//Processar personagem (CharacterProcessor)
        onlineTimeStrategy.apply(persongeId, onlineMs);//Processar tempo online
    }

}
