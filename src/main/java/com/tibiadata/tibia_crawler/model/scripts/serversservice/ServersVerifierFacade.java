package com.tibiadata.tibia_crawler.model.scripts.serversservice;

import com.tibiadata.tibia_crawler.model.scripts.onlineservice.OnlineCharactersPersistence;
import com.tibiadata.tibia_crawler.model.scripts.onlineservice.OnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ServersVerifierFacade {
    private final OnlineService onlineService;
    private final OnlineCharactersPersistence onlineCharactersPersistence;

    //minutes
    private static final int LOOPMINUTETIMER = 540;
    private static final int SERVERSAVEMINUTE = 15;
    //seconds
    private static final int SECONDS = 60;
    //milliseconds
    private static final int MS = 1000;

    @Autowired
    public ServersVerifierFacade(OnlineService onlineService, OnlineCharactersPersistence onlineCharactersPersistence) {
        this.onlineService = onlineService;
        this.onlineCharactersPersistence = onlineCharactersPersistence;
    }

    public void browseServers() {
        ExecutorService executor = Executors.newCachedThreadPool();

        while (true) {
            Map<String, Map<String, Long>> worldTotalPlayers = getWorldTotalPlayers();
            Map<String, Map<String, Long>> worldTotalPlayersCopy = deepCopy(worldTotalPlayers); // Cópia profunda
            executor.execute(() -> onlineCharacterPersistence(worldTotalPlayersCopy));

            try {
                Thread.sleep(SERVERSAVEMINUTE * SECONDS * MS);
            } catch (InterruptedException ignored) {
            }

            System.out.println("---------\nRECOMEÇA DE NOVO!\n---------");
        }
    }

    private Map<String, Map<String, Long>> deepCopy(Map<String, Map<String, Long>> original) {
        Map<String, Map<String, Long>> mapCopy = new HashMap<>();

        for (Map.Entry<String, Map<String, Long>> entry : original.entrySet()) {
            mapCopy.put(entry.getKey(), new HashMap<>(entry.getValue()));// Cria uma nova cópia do mapa interno
        }
        return mapCopy;
    }


    private Map<String, Map<String, Long>> getWorldTotalPlayers() {
        return onlineService.serversVerifier(LOOPMINUTETIMER);
    }

    private void onlineCharacterPersistence(Map<String, Map<String, Long>> worldTotalPlayers) {
        onlineCharactersPersistence.serverSaveCharactersPersistence(worldTotalPlayers);
    }
}
