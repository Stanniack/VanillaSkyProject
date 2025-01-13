package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;

/**
 *
 * @author Devmachine
 */
public class LoyaltyTitleStrategy implements AttributeStrategy {

    //
    private static final short ITEM = 1;

    @Override
    public boolean apply(Personage personage, String item, boolean needsPersistence) {
        String loyaltyTitle = StringUtils.splitAndReplace(item, ITEM);

        if (!needsPersistence) {
            needsPersistence = AttributeHandler.attributeValidator(personage, personage::getLoyaltyTitle, Personage::setLoyaltyTitle, loyaltyTitle);
        } else {
            AttributeHandler.attributeValidator(personage, personage::getLoyaltyTitle, Personage::setLoyaltyTitle, loyaltyTitle);
        }
        return needsPersistence;
    }

}
