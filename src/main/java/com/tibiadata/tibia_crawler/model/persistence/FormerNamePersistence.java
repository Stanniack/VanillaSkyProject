package com.tibiadata.tibia_crawler.model.persistence;

import com.tibiadata.tibia_crawler.model.entities.FormerName;
import com.tibiadata.tibia_crawler.model.repositories.FormerNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class FormerNamePersistence {

    @Autowired
    private FormerNameRepository fnr;

    public FormerName save(FormerName formerName) {
        return fnr.save(formerName);
    }

    public boolean isFormerNameFromPersonage(String formerName, Integer personageID) {
        return fnr.isFormerNameFromPersonage(formerName, personageID);
    }

}
