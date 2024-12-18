package com.tibiadata.tibia_crawler.model.GetContent;

import com.tibiadata.tibia_crawler.model.connections.GetContent;
import com.tibiadata.tibia_crawler.model.utils.ElementsUtils;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Devmachine
 */
public class GetContentTest {

    private ElementsUtils selectors;

    @BeforeEach
    private void loadSelectors() {
        selectors = new ElementsUtils();
    }

    @Test
    void testGetOnlinePlayers() {
        try {
            List<List<String>> list = new GetContent().getOnlinePlayers(
                    "https://www.tibia.com/community/?subtopic=worlds&world=Antica",
                    selectors.getTrEvenOdd(),
                    selectors.getTd()
            );

            // Ensure the list is not empty
            assertFalse(list.isEmpty(), "The returned list is empty.");

        } catch (IOException ex) {
            fail("Test failed due to an IOException, check the internet connection server: " + ex.getMessage());
        }
    }

    @Test
    void testGetTableContent() {
        try {
            List<String> list = new GetContent().getTableContent(
                    "https://www.tibia.com/community/?subtopic=characters&name=Drakcule+Val+Denoya",
                    selectors.getTrBgcolor(),
                    selectors.getTr()
            );

            // Ensure the list is not empty
            assertFalse(list.isEmpty(), "The returned list is empty.");

        } catch (IOException ex) {
            fail("Test failed due to an IOException, check the internet connection server: " + ex.getMessage());
        }
    }

}
