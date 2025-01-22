package com.tibiadata.tibia_crawler.model.parsers.onlineservice;

import com.tibiadata.tibia_crawler.model.connections.GetContent;
import com.tibiadata.tibia_crawler.model.utils.ElementsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class OnlineWorldPlayersProcessor {
    private final GetContent getContent;
    private final ElementsUtils elementsUtils;

    @Autowired
    public OnlineWorldPlayersProcessor(GetContent getContent, ElementsUtils elementsUtils) {
        this.getContent = getContent;
        this.elementsUtils = elementsUtils;
    }

    public List<List<String>> processWorld(String world) {
        try {

            return getContent.
                    getPlayersInfo(
                            "https://www.tibia.com/community/?subtopic=worlds&world=" + world,
                            elementsUtils.getTrEvenOdd(),
                            elementsUtils.getTr());

        } catch (IOException ex) {
            Logger.getLogger(OnlineService.class.getName()).log(Level.SEVERE, "Erro ao tentar buscar a pagina do servidor.");
        }

        return Arrays.asList();
    }
}
