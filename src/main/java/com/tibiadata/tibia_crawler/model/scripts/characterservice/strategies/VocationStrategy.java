package com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component

public class VocationStrategy implements AttributeStrategy {

    private static final String VOCATION = "Vocation:";
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

    @Override
    public String getKey() {
        return VOCATION;
    }

}
