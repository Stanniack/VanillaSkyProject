package com.tibiadata.tibia_crawler.model.parsers.onlineservice;

import com.tibiadata.tibia_crawler.model.connections.GetContent;
import com.tibiadata.tibia_crawler.model.utils.CalendarUtils;
import com.tibiadata.tibia_crawler.model.utils.ElementsUtils;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import com.tibiadata.tibia_crawler.model.utils.TibiaUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Devmachine
 */
@Service
public class OnlineService {

    private GetContent getContent;
    private OnlineCharacterProcessor onlineCharacterProcessor;
    private ElementsUtils elementsUtils;

    private Map<String, Map<String, Long>> worldOnlinePlayers = new HashMap<>();
    private Map<String, Map<String, Long>> worldTotalPlayers = new HashMap<>();
    private Map<String, Map<String, Long>> worldOfflinePlayers = new HashMap<>();

    @Autowired
    public OnlineService(GetContent getContent, ElementsUtils elementsUtils, OnlineCharacterProcessor onlineCharacterProcessor) {
        this.getContent = getContent;
        this.elementsUtils = elementsUtils;
        this.onlineCharacterProcessor = onlineCharacterProcessor;
    }

    private List<List<String>> onlinePlayersProcessor(String world) {
        try {
            List<List<String>> content = getContent.
                    getPlayersInfo(
                            "https://www.tibia.com/community/?subtopic=worlds&world=" + world,
                            elementsUtils.getTrEvenOdd(),
                            elementsUtils.getTr());

            return content;

        } catch (IOException ex) {
            Logger.getLogger(OnlineService.class.getName()).log(Level.SEVERE, "Erro ao tentar buscar a pagina do servidor.");
        }

        return Arrays.asList();
    }

    public Map<String, Map<String, Long>> serversVerifier() {

        while (!CalendarUtils.isCurrentMinute(10)) { // Aguarda até que o minuto atual seja 03

            for (String world : Arrays.asList("Zunera")) { // Itera sobre a lista de mundos 
                Long startTime = System.currentTimeMillis();
                List<List<String>> playersList = onlinePlayersProcessor(world); // Obtém a lista de jogadores online

                worldOnlinePlayers.putIfAbsent(world, new HashMap<>());
                worldTotalPlayers.putIfAbsent(world, new HashMap<>());
                worldOfflinePlayers.putIfAbsent(world, new HashMap<>());

                for (List<String> players : playersList) { // Itera sobre cada lista de jogadores
                    String[] infoPlayer = StringUtils.split(String.join(", ", players), "\\d+", 2); // Divide a string do jogador, ignorando level e profissão
                    String player = infoPlayer[0].trim(); // Trata o nome do jogador (retira espaços)

                    /* Mapa auxiliar para somar tempo online do jogador? Teste de mesa para Samarcarna!!!*/
                    if (worldTotalPlayers.get(world).get(player) != null) { //Se diferente de nulo ent player já tem entrada
                        long currentOnlineTime = worldTotalPlayers.get(world).get(player) + (System.currentTimeMillis() - startTime);
                        worldOnlinePlayers.get(world).put(player, currentOnlineTime);

                    } else {// Senão nova entrada do player
                        worldOnlinePlayers.get(world).put(player, 0L);
                    }

                } // fim for mundos

                Iterator<String> iterator = worldTotalPlayers.get(world).keySet().iterator();

                while (iterator.hasNext()) {
                    String name = iterator.next();
                    if (!worldOnlinePlayers.get(world).containsKey(name)) { // Se o jogador não estiver mais online
                        onlineCharacterProcessor.onlineCharacter(name, worldTotalPlayers.get(world).get(name).intValue()); // persistência simultânea
                        worldOfflinePlayers.get(world).put(name, worldTotalPlayers.get(world).get(name)); // Move o jogador para a lista de offline
                        iterator.remove();
                    }
                }

                worldTotalPlayers.get(world).putAll(worldOnlinePlayers.get(world)); // Atualiza a lista total com os jogadores online do mundo
                worldOnlinePlayers.get(world).clear(); // Limpa a lista de jogadores online do mundo
            }

        } // Fim while

        printPlayers();

        return worldTotalPlayers;
    }

    private void printPlayers() {
        System.out.println("---------");
        for (var entry : worldTotalPlayers.entrySet()) {
            entry.getValue().entrySet().forEach(nicks -> System.out.println(nicks));
        }

        System.out.println("---------");
        for (var entry : worldOfflinePlayers.entrySet()) {
            entry.getValue().entrySet().forEach(nicks -> System.out.println(nicks));
        }
    }

}
