package com.tibiadata.tibia_crawler.model.parsers.onlineservice;

import com.tibiadata.tibia_crawler.model.parsers.characterservice.CharacterProcessor;
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

    private CharacterProcessor characterProcessor;
    private OnlineTimeProcessor onlineTimeProcessor;

    @Autowired
    public OnlineCharacterProcessor(CharacterProcessor characterProcessor, OnlineTimeProcessor onlineTimeProcessor) {
        this.characterProcessor = characterProcessor;
        this.onlineTimeProcessor = onlineTimeProcessor;
    }

    public void onlineCharacter(String name, Integer onlineSecs) {
        Integer persongeId = characterProcessor.processName(name);//Processar personagem (CharacterProcessor)
        onlineTimeProcessor.processOnlineTime(persongeId, onlineSecs);//Processar tempo online (OnlineTimeProcessor)
    }

}
