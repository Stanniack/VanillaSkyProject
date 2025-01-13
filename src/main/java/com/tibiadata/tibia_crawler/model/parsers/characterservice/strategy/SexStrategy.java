package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.entities.Sex;
import com.tibiadata.tibia_crawler.model.persistence.SexPersistence;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Devmachine
 */
@Component
public class SexStrategy implements ObjectStrategy {

    //
    @Autowired
    private SexPersistence sp;
    //
    private static final short ITEM = 1;
    //
    private Sex sex;

    @Override
    public <T> void apply(Personage personage, String newValue) {
        ObjectHandler.genericValidator(
                personage,
                StringUtils.splitAndReplace(newValue, ITEM),
                param -> sp.findLastSexById(param),
                (value, date) -> new Sex(value, date),
                newSex -> this.sex = newSex
        );

        ObjectHandler.persistObject(sex, _sex -> _sex.setPersonage(personage), _sex -> sp.save(_sex));
    }
}
