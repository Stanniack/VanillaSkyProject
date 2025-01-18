package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategies;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class TitleStrategy implements AttributeStrategy {

    private static final String TITLE = ".*[0-9]+ (title|titles) unlocked.*";

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

    @Override
    public String getKey() {
        return TITLE;
    }
}
