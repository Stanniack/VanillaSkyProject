package com.tibiadata.tibia_crawler.model.persistence;

import com.tibiadata.tibia_crawler.model.entities.House;
import com.tibiadata.tibia_crawler.model.repositories.HouseRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class HousePersistence {

    @Autowired
    private HouseRepository hp;

    public House save(House house) {
        return hp.save(house);
    }

    public List<House> findLastThreeHouses(String pName) {
        return hp.findLastThreeHouses(pName);
    }
}
