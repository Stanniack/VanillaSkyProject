package com.tibiadata.tibia_crawler.model.scripts.onlineservice;

import com.tibiadata.tibia_crawler.model.utils.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tibiadata.tibia_crawler.model.utils.TibiaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Devmachine
 */
@Service
public class OnlineService {
    private final OnlineWorldPlayersProcessor onlineWorldPlayersProcessor;
    private final OnlineCharacterProcessor onlineCharacterProcessor;

    private final Map<String, Map<String, Long>> worldOnlinePlayers = new HashMap<>();
    private final Map<String, Map<String, Long>> worldTotalPlayers = new HashMap<>();
    private final Map<String, Map<String, Long>> worldOfflinePlayers = new HashMap<>();

    @Autowired
    public OnlineService(OnlineWorldPlayersProcessor onlineWorldPlayersProcessor, OnlineCharacterProcessor onlineCharacterProcessor) {
        this.onlineWorldPlayersProcessor = onlineWorldPlayersProcessor;
        this.onlineCharacterProcessor = onlineCharacterProcessor;
    }

    //!!! retorna lista contendo total players e off players e só persiste tempo online de total. A persistência dos personagens ocorre em ambas as listas?
    public Map<String, Map<String, Long>> serversVerifier(int minute) {
        long timer = System.currentTimeMillis();

        while ((System.currentTimeMillis() - timer) / 1000 < (minute * 60L)) { // Aguarda o timer chegar no for maior para finalizar
            onlineTimeManager(TibiaUtils.getRetroOpenPvpServers());
        }

        //printPlayers();

        return worldTotalPlayers;
    }

    private void onlineTimeManager(List<String> worlds) {

        for (String world : worlds) { // Itera sobre a lista de mundos
            long startTime = System.currentTimeMillis();
            List<List<String>> playersList = onlineWorldPlayersProcessor.processWorld(world); // Obtém a lista de jogadores online

            initializeWorldMaps(world);

            for (List<String> players : playersList) { // Itera sobre cada lista de jogadores
                String[] infoPlayer = StringUtils.split(String.join(", ", players), "\\d+", 2); // Divide a string do jogador, ignorando level e profissão
                String player = infoPlayer[0].trim(); // Trata o nome do jogador (retira espaços)

                if (worldTotalPlayers.get(world).get(player) != null) { //Se diferente de nulo ent player já tem entrada
                    long currentOnlineTime = worldTotalPlayers.get(world).get(player) + (System.currentTimeMillis() - startTime);
                    worldOnlinePlayers.get(world).put(player, currentOnlineTime);

                } else {// Senão nova entrada do player
                    worldOnlinePlayers.get(world).put(player, 0L);
                }

            } // fim for mundos

            Iterator<String> iterator = worldTotalPlayers.get(world).keySet().iterator();
            long persistirOffTime = System.currentTimeMillis();
            int amount = 0;

            while (iterator.hasNext()) {
                String name = iterator.next();
                if (!worldOnlinePlayers.get(world).containsKey(name)) { // Se o jogador não estiver mais online
                    onlineCharacterProcessor.onlineCharacter(name, worldTotalPlayers.get(world).get(name).intValue()); // !!persistência simultânea
                    amount++;
                    worldOfflinePlayers.get(world).put(name, worldTotalPlayers.get(world).get(name)); // Move o jogador para a lista de offline
                    iterator.remove();
                }
            }

            persistenceLogs(world, amount, persistirOffTime);

            worldTotalPlayers.get(world).putAll(worldOnlinePlayers.get(world)); // Atualiza a lista total com os jogadores online do mundo
            worldOnlinePlayers.get(world).clear(); // Limpa a lista de jogadores online do mundo
        }
    }

    private void initializeWorldMaps(String world) {
        worldOnlinePlayers.putIfAbsent(world, new HashMap<>());
        worldTotalPlayers.putIfAbsent(world, new HashMap<>());
        worldOfflinePlayers.putIfAbsent(world, new HashMap<>());
    }

    private void persistenceLogs(String world, int amount, long persistirOffTime) {
        if (amount > 0) {
            System.out.println("TEMPO PARA PERSISTÊNCIA DOS PERSONAGENS QUE DESLOGARAM: " + (System.currentTimeMillis() - persistirOffTime) / 1000 + " SECS");
            System.out.println("TENTATIVAS DE PERSISTÊNCIA: " + amount);
            System.out.println("SERVIDOR: " + world);
            System.out.println("___________________________________________________________________________________");
        }
    }

    private void printPlayers() {

        System.out.println("---------");
        System.out.println("Online:");
        System.out.println("---------");

        for (var entry : worldTotalPlayers.entrySet()) {
            entry.getValue().entrySet().forEach(System.out::println);
        }

        System.out.println("---------");
        System.out.println("Offline:");
        System.out.println("---------");

        for (var entry : worldOfflinePlayers.entrySet()) {
            entry.getValue().entrySet().forEach(System.out::println);
        }
    }

}

