package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;

/**
 *
 * @author Devmachine
 */
public class VocationStrategy implements AttributeStrategy {

    //
    private static final short ITEM = 1;

    @Override
    public boolean apply(Personage personage, String item, boolean needsPersistence) {
        String vocation = StringUtils.splitAndReplace(item, ITEM);

        if (!needsPersistence) {
            needsPersistence = AttributeHandler.attributeValidator(personage, personage::getVocation, Personage::setVocation, vocation);
        } else {
            AttributeHandler.attributeValidator(personage, personage::getVocation, Personage::setVocation, vocation);
        }
        return needsPersistence;
    }

}
