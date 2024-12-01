package com.tibiadata.tibia_crawler.model.persistence;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.repositories.PersonageRepository;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class PersonagePersistence {

    @Autowired
    private PersonageRepository pp;

    public Personage save(Personage p) {
        return pp.save(p);
    }

    public Personage findByName(String name) {
        return pp.findByName(name);
    }

    public boolean existsByName(String name) {
        return pp.existsByName(name);
    }

    public Integer findIdByName(String name) {
        return pp.findIdByName(name);
    }
    
    public Calendar findRegisteredDateByName(String name){
        return pp.findRegisteredDateByName(name);
    }
    
}
