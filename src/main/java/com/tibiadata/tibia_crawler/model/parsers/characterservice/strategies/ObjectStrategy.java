package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategies;

import com.tibiadata.tibia_crawler.model.entities.Personage;

/**
 *
 * @author Devmachine
 */
public interface ObjectStrategy {

    <T> void apply(Personage personage, String newValue);

    String getKey();
}
