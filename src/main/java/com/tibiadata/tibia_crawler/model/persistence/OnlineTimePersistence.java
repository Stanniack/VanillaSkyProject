package com.tibiadata.tibia_crawler.model.persistence;

import com.tibiadata.tibia_crawler.model.entities.OnlineTime;
import com.tibiadata.tibia_crawler.model.repositories.OnlineTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class OnlineTimePersistence {

    @Autowired
    private OnlineTimeRepository otr;

    public Integer findLastOnlineTime(Integer persongeId) {
        return otr.findLastOnlineTime(persongeId);
    }

    public OnlineTime findLastTimeOnlineById(Integer persongeId) {
        return otr.findLastTimeOnlineById(persongeId);
    }

    public OnlineTime save(OnlineTime onlineTime) {
        return otr.save(onlineTime);
    }

}
