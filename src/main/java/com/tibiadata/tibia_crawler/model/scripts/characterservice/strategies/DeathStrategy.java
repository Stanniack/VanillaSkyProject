package com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies;

import com.tibiadata.tibia_crawler.model.entities.Death;
import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.exceptions.StringLengthException;
import com.tibiadata.tibia_crawler.model.persistence.DeathPersistence;
import com.tibiadata.tibia_crawler.model.utils.CalendarUtils;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
@Scope("prototype")
public class DeathStrategy implements ObjectStrategy {

    @Autowired
    private DeathPersistence dp;

    private static final String DEATH = "^\\w{3} \\d{2} \\d{4}, \\d{2}:\\d{2}:\\d{2} \\w+.*";
    private static final short STRLENTOLERANCE = 2000;

    private final List<Death> deaths = new ArrayList<>();

    @Override
    public <T> void apply(Personage personage, String newValue) {
        try {
            String[] occurrence = StringUtils.split(newValue, "\\s?(CET|CEST)\\s?");

            deathValidator(personage, occurrence[0], occurrence[1]);
            persistDeaths(personage);
        } catch (StringLengthException ex) {
            Logger.getLogger(DeathStrategy.class.getName()).log(Level.SEVERE, "The Death's string length is too long. Cannot persist.");
        }
    }

    private void deathValidator(Personage personage, String deathDate, String occurrence) throws StringLengthException {

        if (occurrence.length() <= STRLENTOLERANCE) {
            Calendar convertedDeathDate = CalendarUtils.parseToCalendar(deathDate);
            Date dbDate = dp.findDeathByDateAndPersonageId(convertedDeathDate, personage.getId());

            if (dbDate == null) {//Se a data da morte (horário incluso) buscada for nula (não existe!) a morte é nova, persistir
                deaths.add(new Death(occurrence, convertedDeathDate));
            }

        } else {
            throw new StringLengthException("The string death occurrence is too long.");
        }
    }

    protected void persistDeaths(Personage personage) {
        deaths.forEach(death -> {
            ObjectHandler.persistObject(death, _death -> _death.setPersonage(personage), _death -> dp.save(_death));
        });
    }

    @Override
    public String getKey() {
        return DEATH;
    }

}
