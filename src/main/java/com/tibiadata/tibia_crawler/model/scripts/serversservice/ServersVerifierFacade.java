package com.tibiadata.tibia_crawler.model.scripts.serversservice;

import com.tibiadata.tibia_crawler.model.scripts.onlineservice.OnlineCharactersPersistence;
import com.tibiadata.tibia_crawler.model.scripts.onlineservice.OnlineService;
import com.tibiadata.tibia_crawler.model.utils.CalendarUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ServersVerifierFacade {
    private static final Logger logger = LoggerFactory.getLogger(ServersVerifierFacade.class);
    //
    private final OnlineService onlineService;
    private final OnlineCharactersPersistence onlineCharactersPersistence;

    //minutes
    private static final int LOOPMINUTETIMER = (int) CalendarUtils.minutesToServerSave();
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

        try {
            while (true) {
                Map<String, Map<String, Long>> worldTotalPlayers = getWorldTotalPlayers();
                Map<String, Map<String, Long>> worldTotalPlayersCopy = deepCopy(worldTotalPlayers);
                executor.execute(() -> onlineCharacterPersistence(worldTotalPlayersCopy));

                Thread.sleep(SERVERSAVEMINUTE * SECONDS * MS);

                logger.debug("--------- !RECOMEÇA NOVAMENTE! ---------");
            }
        } catch (Exception e) {
            logger.error("Exceção na thread principal: {}", e.getMessage());
        } finally {
            logger.warn("Finalizando thread secundária.");
            executor.shutdown();  // Garantir que a segunda thread seja encerrada para evitar sobrrecarregamento de thread ao inicializar serviço NSSP
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
