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
    //
    private static final int LOOPMINUTETIMER = 3;

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
