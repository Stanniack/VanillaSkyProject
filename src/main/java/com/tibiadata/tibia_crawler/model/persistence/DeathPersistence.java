package com.tibiadata.tibia_crawler.model.persistence;

import com.tibiadata.tibia_crawler.model.entities.Death;
import com.tibiadata.tibia_crawler.model.repositories.DeathRepository;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class DeathPersistence {

    @Autowired
    private DeathRepository dr;

    public Date findDeathByDate(Calendar deathDate, String pName) {
        return dr.findDeathByDate(deathDate, pName);
    }

    public Date findDeathByDateAndPersonageId(Calendar deathDate, Integer personageId) {
        return dr.findDeathByDateAndPersonageId(deathDate, personageId);
    }

    public Death save(Death death) {
        return dr.save(death);
    }
}
