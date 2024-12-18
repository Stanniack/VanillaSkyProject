package com.tibiadata.tibia_crawler.model.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Devmachine
 */
public class PriorityHandler {

    /**
     * Reorders a list of strings based on predefined priorities. The method
     * groups items by their corresponding titles (e.g., "Name:", "Former
     * Names:", etc.) and ensures that each group is ordered according to the
     * priority defined. If there are multiple occurrences of the same title,
     * they are preserved in the result.
     *
     * @param originalList The list of strings to be reordered.
     * @return A reordered list of strings based on priority.
     */
    public List<String> reorderList(List<String> originalList) {
        // Mapa com a ordem de prioridade (números como chave)
        Map<Integer, String> priorityMap = new HashMap<>();
        priorityMap.put(1, "Name:");
        priorityMap.put(2, "Former Names:");
        priorityMap.put(3, "Title:");
        priorityMap.put(4, "Vocation:");
        priorityMap.put(5, "Residence:");
        priorityMap.put(6, "Last Login:");
        priorityMap.put(7, "Account Status:");
        priorityMap.put(8, "Loyalty Title:");
        priorityMap.put(9, "Created:");
        priorityMap.put(10, "Sex:");
        priorityMap.put(11, "Level:");
        priorityMap.put(12, "Achievement Points:");
        priorityMap.put(13, "World:");
        priorityMap.put(14, "Guild Membership:");
        priorityMap.put(15, "House:");

        // Mapa para armazenar listas de strings, para lidar com múltiplas ocorrências
        Map<String, List<String>> groupedItems = new HashMap<>();

        // Agrupar as ocorrências de "House:" e outras similares
        for (String item : originalList) {
            for (String key : priorityMap.values()) {
                if (item.startsWith(key)) {
                    groupedItems.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
                    break;
                }
            }
        }

        // Criar a lista reordenada
        List<String> reorderedList = new ArrayList<>();

        // Iterar sobre o priorityMap para garantir que a ordem de prioridade seja mantida
        for (Map.Entry<Integer, String> entry : priorityMap.entrySet()) {
            List<String> items = groupedItems.get(entry.getValue());
            if (items != null) {
                reorderedList.addAll(items);  // Adiciona todas as ocorrências de uma chave
            }
        }

        return reorderedList;
    }

}
