package com.tibiadata.tibia_crawler.model.parsers.onlineservice;

import com.tibiadata.tibia_crawler.model.parsers.characterservice.strategies.OnlineTimeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
@Scope("prototype")
public class OnlineTimeProcessor {

    private OnlineTimeStrategy otStrategy;

    @Autowired
    public OnlineTimeProcessor(OnlineTimeStrategy otStrategy) {
        this.otStrategy = otStrategy;
    }

    public void processOnlineTime(Integer personageId, Integer onlineMs) {
        otStrategy.apply(personageId, onlineMs);
    }

}
