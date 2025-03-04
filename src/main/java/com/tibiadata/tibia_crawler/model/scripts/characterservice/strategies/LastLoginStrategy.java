package com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class LastLoginStrategy implements AttributeStrategy {

    private static final String LASTLOGIN = "Last Login:";

    //
    private static final short ITEM = 1;

    @Override
    public boolean apply(Personage personage, String item, boolean needsPersistence) {
        String lastLogin = StringUtils.splitAndReplace(item, ITEM);

        if (!needsPersistence) {
            needsPersistence = AttributeHandler.attributeValidator(personage, personage::getLastLogin, Personage::setLastLogin, lastLogin);
        } else {
            AttributeHandler.attributeValidator(personage, personage::getLastLogin, Personage::setLastLogin, lastLogin);
        }
        return needsPersistence;
    }

    @Override
    public String getKey() {
        return LASTLOGIN;
    }

}
