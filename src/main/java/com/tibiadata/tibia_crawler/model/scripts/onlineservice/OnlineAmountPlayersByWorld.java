package com.tibiadata.tibia_crawler.model.scripts.onlineservice;

import com.tibiadata.tibia_crawler.model.utils.TibiaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OnlineAmountPlayersByWorld {
    private final OnlineWorldPlayersProcessor onlineWorldPlayersProcessor;

    @Autowired
    public OnlineAmountPlayersByWorld(OnlineWorldPlayersProcessor onlineWorldPlayersProcessor) {
        this.onlineWorldPlayersProcessor = onlineWorldPlayersProcessor;
    }

    public int worldTypes(List<String> worlds) {
        int amount = 0;
        for (String world : worlds) {
            System.out.println(world + ": " + onlineWorldPlayersProcessor.processWorld(world).size());
            amount = amount + onlineWorldPlayersProcessor.processWorld(world).size();
        }
        return amount;
    }

    public int uniqueWorld(String world) {
        return onlineWorldPlayersProcessor.processWorld(world).size();
    }
}
