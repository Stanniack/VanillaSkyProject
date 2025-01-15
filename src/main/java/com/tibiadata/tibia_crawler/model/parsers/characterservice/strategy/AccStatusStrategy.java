package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class AccStatusStrategy implements AttributeStrategy {

    private static final String ACCSTATUS = "Account Status:";

    //
    private static final short ITEM = 1;

    @Override
    public boolean apply(Personage personage, String item, boolean needsPersistence) {
        String accStatus = StringUtils.splitAndReplace(item, ITEM);

        if (!needsPersistence) {
            needsPersistence = AttributeHandler.attributeValidator(personage, personage::getAccStatus, Personage::setAccStatus, accStatus);
        } else {
            AttributeHandler.attributeValidator(personage, personage::getAccStatus, Personage::setAccStatus, accStatus);
        }
        return needsPersistence;
    }

    @Override
    public String getKey() {
        return ACCSTATUS;
    }

}
