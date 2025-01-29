package com.tibiadata.tibia_crawler.model.scripts.serversservice;

import com.tibiadata.tibia_crawler.model.scripts.onlineservice.OnlineCharactersPersistence;
import com.tibiadata.tibia_crawler.model.scripts.onlineservice.OnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ServersVerifierFacade {
    private final OnlineService onlineService;
    private final OnlineCharactersPersistence onlineCharactersPersistence;
    //
    private static int MINUTE = 1;

    @Autowired
    public ServersVerifierFacade(OnlineService onlineService, OnlineCharactersPersistence onlineCharactersPersistence) {
        this.onlineService = onlineService;
        this.onlineCharactersPersistence = onlineCharactersPersistence;
    }

    public void browseServers() {
        ExecutorService executor = Executors.newCachedThreadPool();

        while (true) {
            Map<String, Map<String, Long>> worldTotalPlayers = getWorldTotalPlayers();
            executor.execute(() -> onlineCharacterPersistence(worldTotalPlayers));// Executa onlineCharacterPersistence em uma thread separada

            System.out.println("---------\nRECOMEÇA DE NOVO!\n---------");
        }

    }

    private Map<String, Map<String, Long>> getWorldTotalPlayers() {
        return onlineService.serversVerifier(MINUTE);
    }

    private void onlineCharacterPersistence(Map<String, Map<String, Long>> worldTotalPlayers) {
        onlineCharactersPersistence.serverSaveCharactersPersistence(worldTotalPlayers);
    }
}
