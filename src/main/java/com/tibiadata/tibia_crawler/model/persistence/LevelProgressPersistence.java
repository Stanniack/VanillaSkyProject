package com.tibiadata.tibia_crawler.model.persistence;

import com.tibiadata.tibia_crawler.model.entities.LevelProgress;
import com.tibiadata.tibia_crawler.model.repositories.LevelProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class LevelProgressPersistence {

    @Autowired
    LevelProgressRepository lpr;

    public LevelProgress save(LevelProgress levelProgress) {
        return lpr.save(levelProgress);
    }

    public String findLastLevelProgress(String personageName) {
        return lpr.findLastLevelProgress(personageName);
    }
    
    public String findLastLevelProgressById(Integer personageId) {
        return lpr.findLastLevelProgressById(personageId);
    }

}
