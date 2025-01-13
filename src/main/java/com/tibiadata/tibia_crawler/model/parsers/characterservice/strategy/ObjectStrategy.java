package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import com.tibiadata.tibia_crawler.model.entities.Personage;

/**
 *
 * @author Devmachine
 */
public interface ObjectStrategy {

    <T> void apply(Personage personage, String newValue);
}
