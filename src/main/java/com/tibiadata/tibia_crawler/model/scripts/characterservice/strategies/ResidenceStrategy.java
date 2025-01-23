package com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class ResidenceStrategy implements AttributeStrategy {

    private static final String RESIDENCE = "Residence:";

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

    @Override
    public String getKey() {
        return RESIDENCE;
    }

}
