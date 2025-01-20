package com.tibiadata.tibia_crawler.model.connections;

import com.tibiadata.tibia_crawler.TibiaCrawlerApplication4;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Devmachine
 */
@SpringBootTest(classes = TibiaCrawlerApplication4.class)
public class GetContentTest {

    @Autowired
    private GetContent getContent;
    @Autowired
    private ElementsUtils elementsUtils;
    @Autowired
    private PriorityHandler priorityOrder;
    private List<String> personageItens;

    @BeforeEach
    public void loadPersonageItens() {
        try {
            String url = "https://www.tibia.com/community/?subtopic=characters&name=";
            String charName = "Vepeeh";

            personageItens = getContent.getTableContent(
                    url + charName,
                    elementsUtils.getTrBgcolor(),
                    elementsUtils.getTr()
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

            List<List<String>> onlinePersonages = getContent.getPlayersInfo(
                    url + world,
                    elementsUtils.getTrEvenOdd(),
                    elementsUtils.getTd()
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
        List<String> reorderedItens = priorityOrder.reorderList(personageItens);

        // Se a lista está vazia, não precisa fazer a verficações de prefixo
        assertFalse(reorderedItens.isEmpty(), "The returned list is empty. Check the url or character name");

        // Prefixos esperados armazenados em um Set - se o método reorderList for alterado, inviabiliza o test
        Set<String> expectedItems = Set.of(
                "Name:", "Title:", "Vocation:", "Residence:",
                "Last Login:", "Account Status:", "Sex:",
                "Level:", "Achievement Points:", "World:"
        );

        // Verificando se a lista contém os prefixos esperados, caso não houver, o personagem não existe ou há problemas ao minerar os dados
        for (String prefix : expectedItems) {
            assertTrue(reorderedItens.stream().anyMatch(reorderedItem -> reorderedItem.startsWith(prefix)),
                    "The list doesn't contain an item with '" + prefix + "'");
        }
    }

    @Test
    void testContainsHiddenPersonageItens() {
        List<String> reorderedItens = priorityOrder.reorderList(personageItens);

        // Se a lista está vazia, não precisa fazer a verficações de prefixo
        assertFalse(reorderedItens.isEmpty(), "The returned list is empty. Check the url or character name");

        Set<String> expectedItems = Set.of("Loyalty Title:", "Created:");

        // Verificando se a lista contém os prefixos esperados, caso não houver, o personagem está marcado como 'hidden' pelo proprietário
        for (String prefix : expectedItems) {
            assertTrue(reorderedItens.stream().anyMatch(reorderedItem -> reorderedItem.startsWith(prefix)),
                    "The list doesn't contain an item with '" + prefix + "'");
        }
    }

    @Test
    void testContainsArbitraryPersonageItens() {
        List<String> reorderedItens = priorityOrder.reorderList(personageItens);

        // Se a lista está vazia, não precisa fazer a verficações de prefixo
        assertFalse(reorderedItens.isEmpty(), "The returned list is empty. Check the url or character name");

        Set<String> expectedItems = Set.of("Former Names:", "Guild Membership:", "House:");

        /*Verificando se a lista contém os prefixos esperados, caso não houver, 
        o personagem não possui algumas informações arbitrárias, podendo ser checadas unitariamente */
        for (String prefix : expectedItems) {
            assertTrue(reorderedItens.stream().anyMatch(reorderedItem -> reorderedItem.startsWith(prefix)),
                    "The list doesn't contain an item with '" + prefix + "'");
        }
    }

}
