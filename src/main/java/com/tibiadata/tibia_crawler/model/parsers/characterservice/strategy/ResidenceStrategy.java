package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;

/**
 *
 * @author Devmachine
 */
public class ResidenceStrategy implements AttributeStrategy {

    //
    private static final short ITEM = 1;

    @Override
    public boolean apply(Personage personage, String item, boolean needsPersistence) {
        String residence = StringUtils.splitAndReplace(item, ITEM);

        if (!needsPersistence) {
            needsPersistence = AttributeHandler.attributeValidator(personage, personage::getResidence, Personage::setResidence, residence);
        } else {
            AttributeHandler.attributeValidator(personage, personage::getResidence, Personage::setResidence, residence);
        }
        return needsPersistence;
    }

}
