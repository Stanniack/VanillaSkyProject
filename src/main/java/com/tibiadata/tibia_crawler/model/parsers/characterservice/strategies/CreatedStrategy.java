package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategies;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class CreatedStrategy implements AttributeStrategy {

    private static final String CREATED = "Created:";
    //
    private static final short ITEM = 1;

    @Override
    public boolean apply(Personage personage, String item, boolean needsPersistence) {
        String created = StringUtils.splitAndReplace(item, ITEM);

        if (!needsPersistence) {
            needsPersistence = AttributeHandler.attributeValidator(personage, personage::getCreated, Personage::setCreated, created);
        } else {
            AttributeHandler.attributeValidator(personage, personage::getCreated, Personage::setCreated, created);
        }
        return needsPersistence;
    }

    @Override
    public String getKey() {
        return CREATED;
    }

}
