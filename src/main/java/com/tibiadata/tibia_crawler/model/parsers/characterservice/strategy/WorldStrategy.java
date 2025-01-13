package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.entities.World;
import com.tibiadata.tibia_crawler.model.persistence.WorldPersistence;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class WorldStrategy implements ObjectStrategy {

    //
    @Autowired
    private WorldPersistence wp;
    //
    private static final short ITEM = 1;
    //
    private World world;

    @Override
    public <T> void apply(Personage personage, String newValue) {
                ObjectHandler.genericValidator(
                personage,
                StringUtils.splitAndReplace(newValue, ITEM),
                param -> wp.findLastWorldById(param),
                (value, date) -> new World(value, date),
                newWorld -> this.world = newWorld
        );

        ObjectHandler.persistObject(world, _world -> _world.setPersonage(personage), _world -> wp.save(_world));
    }

}
