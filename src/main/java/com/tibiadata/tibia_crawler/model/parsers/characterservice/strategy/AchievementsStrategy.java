package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import com.tibiadata.tibia_crawler.model.entities.Achievements;
import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.persistence.AchievementsPersistence;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class AchievementsStrategy implements ObjectStrategy {

    //
    @Autowired
    private AchievementsPersistence ap;
    //
    private static final short ITEM = 1;
    //
    private Achievements achievements;

    @Override
    public <T> void apply(Personage personage, String newValue) {
        ObjectHandler.genericValidator(
                personage,
                StringUtils.splitAndReplace(newValue, ITEM),
                param -> ap.findLastPointsById(param),
                (value, date) -> new Achievements(value, date),
                newAchievementPoint -> this.achievements = newAchievementPoint
        );

        ObjectHandler.persistObject(achievements, _achievements -> _achievements.setPersonage(personage), _achievements -> ap.save(achievements));
    }

}
