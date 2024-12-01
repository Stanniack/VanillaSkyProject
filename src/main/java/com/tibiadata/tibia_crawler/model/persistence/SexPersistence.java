package com.tibiadata.tibia_crawler.model.persistence;

import com.tibiadata.tibia_crawler.model.entities.Sex;
import com.tibiadata.tibia_crawler.model.repositories.SexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class SexPersistence {

    @Autowired
    private SexRepository sr;

    /**
     *
     * @param personageName Personage's name to find his last genre
     * @return last Personage's genre type registered
     */
    public Sex findLastSex(String personageName) {
        return sr.findLastSex(personageName);
    }

    public Sex save(Sex sex) {
        return sr.save(sex);
    }

}
