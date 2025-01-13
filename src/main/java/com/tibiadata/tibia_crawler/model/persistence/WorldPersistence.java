package com.tibiadata.tibia_crawler.model.persistence;

import com.tibiadata.tibia_crawler.model.entities.World;
import com.tibiadata.tibia_crawler.model.repositories.WorldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class WorldPersistence {

    @Autowired
    private WorldRepository wr;

    public String findLastWorld(String server) {
        return wr.findLastWorld(server);
    }

    public String findLastWorldById(Integer personageId) {
        return wr.findLastWorldById(personageId);
    }

    public World save(World world) {
        return wr.save(world);
    }
}
