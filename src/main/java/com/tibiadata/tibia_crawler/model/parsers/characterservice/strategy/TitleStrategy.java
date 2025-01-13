package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;

/**
 *
 * @author Devmachine
 */
public class TitleStrategy implements AttributeStrategy {

    //
    private static final short ITEM = 1;

    @Override
    public boolean apply(Personage personage, String item, boolean needsPersistence) {
        String title = StringUtils.splitAndReplace(item, ITEM);

        if (!needsPersistence) {
            needsPersistence = AttributeHandler.attributeValidator(personage, personage::getTitle, Personage::setTitle, title);
        } else {
            AttributeHandler.attributeValidator(personage, personage::getTitle, Personage::setTitle, title);
        }
        return needsPersistence;
    }
}
