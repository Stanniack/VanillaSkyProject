package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import com.tibiadata.tibia_crawler.model.entities.LevelProgress;
import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.persistence.LevelProgressPersistence;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class LevelProgressStrategy implements ObjectStrategy {

    @Autowired
    private LevelProgressPersistence lpp;
    //
    private static final short ITEM = 1;
    //
    private LevelProgress levelProgress;

    @Override
    public <T> void apply(Personage personage, String newValue) {
        ObjectHandler.genericValidator(
                personage,
                StringUtils.splitAndReplace(newValue, ITEM), 
                param -> lpp.findLastLevelProgressById(param), 
                (value, date) -> new LevelProgress(value, date), 
                newLevelProgress -> this.levelProgress = newLevelProgress);
        
        ObjectHandler.persistObject(levelProgress, _levelProgress -> _levelProgress.setPersonage(personage), _levelProgress -> lpp.save(_levelProgress));
    }

}
