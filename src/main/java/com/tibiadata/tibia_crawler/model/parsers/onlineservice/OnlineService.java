package com.tibiadata.tibia_crawler.model.parsers.onlineservice;

import com.tibiadata.tibia_crawler.model.connections.GetContent;
import com.tibiadata.tibia_crawler.model.utils.CalendarUtils;
import com.tibiadata.tibia_crawler.model.utils.ElementsUtils;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import com.tibiadata.tibia_crawler.model.utils.TibiaUtils;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Devmachine
 */
public class OnlineService {

    private Map<String, Map<String, Long>> worldOnlinePlayers = new HashMap<>();
    private Map<String, Long> onlinePlayers = new HashMap<>();

    private Map<String, Map<String, Long>> worldTotalPlayers = new HashMap<>();
    private Map<String, Long> totalPlayers = new HashMap<>();

    private Map<String, Map<String, Long>> worldOfflinePlayers = new HashMap<>();
    private Map<String, Long> offlinePlayers = new HashMap<>();

    private List<List<String>> playersOnlineProcessor(String world) {
        try {
            List<List<String>> content = new GetContent().
                    getPlayersInfo(
                            "https://www.tibia.com/community/?subtopic=worlds&world=" + world,
                            new ElementsUtils().getTrEvenOdd(),
                            new ElementsUtils().getTr());

            return content;

        } catch (IOException ex) {
            Logger.getLogger(OnlineService.class.getName()).log(Level.SEVERE, "Erro ao tentar buscar a pagina do servidor.");
        }

        return Arrays.asList();
    }

    public void serversVerifier() {
        Long startTime = System.currentTimeMillis();

        while (!CalendarUtils.isCurrentMinute(45)) {

            for (String world : TibiaUtils.getWorlds()) {
                System.out.println(world);
                List<List<String>> content = playersOnlineProcessor(world);
                worldOnlinePlayers.putIfAbsent(world, new HashMap<>());
                worldTotalPlayers.putIfAbsent(world, new HashMap<>());
                worldOfflinePlayers.putIfAbsent(world, new HashMap<>());

                for (List<String> players : content) {
                    String[] infoPlayer = StringUtils.split(String.join(", ", players), "\\d+", 2);
                    String player = infoPlayer[0].trim();

                    if (!worldOnlinePlayers.get(world).containsKey(player)) {

                        if (worldOfflinePlayers.get(world).containsKey(player)) {
                            worldOnlinePlayers.get(world).put(player, worldOfflinePlayers.get(world).get(player));
                        } else {
                            worldOnlinePlayers.get(world).put(player, System.currentTimeMillis() - startTime);
                        }

                    } else {
                        worldOnlinePlayers.get(world).replace(player, (worldOnlinePlayers.get(world).get(player) + System.currentTimeMillis() - startTime));
                    }
                }

                Iterator<String> iterator = worldTotalPlayers.get(world).keySet().iterator();

                while (iterator.hasNext()) {
                    String player = iterator.next();
                    if (!worldOnlinePlayers.get(world).containsKey(player)) {
                        //System.out.println(LocalTime.now() + " Deslogou: " + player);
                        worldOfflinePlayers.get(world).put(player, worldTotalPlayers.get(world).get(player));
                        iterator.remove(); // Remove o jogador enquanto itera
                    }
                }

                worldTotalPlayers.get(world).putAll(worldOnlinePlayers.get(world));
                worldOnlinePlayers.get(world).clear();
            }

        }

        System.out.println("---------");
        for (var entry : worldTotalPlayers.entrySet()) {
            entry.getValue().entrySet().forEach(nicks -> System.out.println(nicks));
        }

        System.out.println("---------");
        for (var entry : worldOfflinePlayers.entrySet()) {
            entry.getValue().entrySet().forEach(nicks -> System.out.println(nicks));
        }
    }

    public void serverVerifier() {
        Long startTime = System.currentTimeMillis();

        while (!CalendarUtils.isCurrentMinute(0)) {

            List<List<String>> content = playersOnlineProcessor("Zunera");

            for (List<String> players : content) {
                String[] infoPlayer = StringUtils.split(String.join(", ", players), "\\d+", 2);
                String player = infoPlayer[0].trim();

                if (!onlinePlayers.containsKey(player)) {

                    if (offlinePlayers.containsKey(player)) {
                        onlinePlayers.put(player, offlinePlayers.get(player));
                    } else {
                        onlinePlayers.put(player, System.currentTimeMillis() - startTime);
                    }

                } else {
                    onlinePlayers.replace(player, System.currentTimeMillis() - startTime);
                }
            }

            String offline = null;

            for (var player : totalPlayers.keySet()) {
                if (!onlinePlayers.containsKey(player)) {
                    System.out.println(LocalTime.now() + " Deslogou: " + player);
                    offlinePlayers.put(player, totalPlayers.get(player));
                    offline = player;
                }
            }

            totalPlayers.remove(offline);
            totalPlayers.putAll(onlinePlayers);
            worldOnlinePlayers.clear();

        }

        System.out.println("---------");
        for (var entry : totalPlayers.entrySet()) {
            System.out.println(entry);
        }

        System.out.println("---------");
        for (var entry : offlinePlayers.entrySet()) {
            System.out.println(entry);
        }
    }

}
