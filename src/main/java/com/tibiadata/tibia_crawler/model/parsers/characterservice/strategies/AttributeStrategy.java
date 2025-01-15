package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategies;

import com.tibiadata.tibia_crawler.model.entities.Personage;

/**
 *
 * @author Devmachine
 */
public interface AttributeStrategy {

    boolean apply(Personage personage, String item, boolean needsPersistence);
    String getKey();
}
