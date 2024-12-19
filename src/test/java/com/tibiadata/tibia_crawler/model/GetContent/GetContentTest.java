package com.tibiadata.tibia_crawler.model.GetContent;

import com.tibiadata.tibia_crawler.model.connections.GetContent;
import com.tibiadata.tibia_crawler.model.handler.PriorityHandler;
import com.tibiadata.tibia_crawler.model.utils.ElementsUtils;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Devmachine
 */
public class GetContentTest {

    private GetContent getContent;
    private ElementsUtils selectors;
    private PriorityHandler priorityOrder;
    private List<String> personageItens;

    @BeforeEach
    private void loadContexts() {
        getContent = new GetContent();
        selectors = new ElementsUtils();
        priorityOrder = new PriorityHandler();
    }

    @BeforeEach
    private void loadPersonageItens() {
        try {
            String url = "https://www.tibia.com/community/?subtopic=characters&name=";
            String charName = "Vytsznyak";

            personageItens = getContent.getTableContent(
                    url + charName,
                    selectors.getTrBgcolor(),
                    selectors.getTr()
            );

        } catch (IOException ex) {
            fail("Test failed due to an IOException, check the internet connection server: " + ex.getMessage());
        }
    }

    @Test
    void testGetOnlinePlayers() {
        try {
            String url = "https://www.tibia.com/community/?subtopic=worlds&world=";
            String world = "Antica";

            List<List<String>> onlinePersonages = getContent.getOnlinePlayers(
                    url + world,
                    selectors.getTrEvenOdd(),
                    selectors.getTd()
            );

            assertFalse(onlinePersonages.isEmpty(), "The returned list is empty. Check the url and world");

        } catch (IOException ex) {
            fail("Test failed due to an IOException, check the internet connection server: " + ex.getMessage());
        }
    }

    @Test
    void testGetTableContent() {
        assertFalse(personageItens.isEmpty(), "The returned list is empty. Check the url or character name");
    }

    @Test
    void testContainsPersonageItens() {
        List<String> reorderedItens = priorityOrder.reorderList(personageItens); // se o método reorderList for alterado, inviabiliza o test

        // Se a lista está vazia, não precisa fazer a verficações de prefixo
        assertFalse(reorderedItens.isEmpty(), "The returned list is empty. Check the url or character name");
        
        // Prefixos esperados armazenados em um Set para otimização
        Set<String> expectedItems = Set.of(
                "Name:", "Title:", "Vocation:", "Residence:",
                "Last Login:", "Account Status:", "Sex:",
                "Level:", "Achievement Points:", "World:"
        );

        // Verificando se a lista contém os prefixos esperados
        for (String prefix : expectedItems) {
            assertTrue(reorderedItens.stream().anyMatch(reorderedItem -> reorderedItem.startsWith(prefix)),
                    "The list doesn't contain an item with '" + prefix + "'");
        }
    }

    @Test
    void testContainsHiddenPersonageItens() {

    }

    @Test
    void testContainsArbitraryPersonageItens() {

    }

}
