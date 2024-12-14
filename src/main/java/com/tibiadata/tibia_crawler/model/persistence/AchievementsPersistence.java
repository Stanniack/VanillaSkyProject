package com.tibiadata.tibia_crawler.model.persistence;

import com.tibiadata.tibia_crawler.model.entities.Achievements;
import com.tibiadata.tibia_crawler.model.repositories.AchievementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class AchievementsPersistence {

    @Autowired
    private AchievementsRepository ar;

    public String findLastPoints(String name) {
        return ar.findLastPoints(name);
    }

    public Achievements save(Achievements points) {
        return ar.save(points);
    }

}
