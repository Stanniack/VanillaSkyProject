package com.tibiadata.tibia_crawler.mocks;

import com.tibiadata.tibia_crawler.TibiaCrawlerApplication;
import com.tibiadata.tibia_crawler.model.mocks.HighScoreMock;
import com.tibiadata.tibia_crawler.model.utils.ElementsUtils;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Devmachine
 */
@SpringBootTest(classes = TibiaCrawlerApplication.class)
public class HighScoreMockTest {

    @Autowired
    private HighScoreMock hsm;
    private ElementsUtils selectors;

    @BeforeEach
    public void loadContexts() {
        this.selectors = new ElementsUtils();
    }

    @Test
    public void testContainsAllPlayersPage1() throws IOException {

        List<String> nicks = hsm
                .getHighscoreNicks(
                        "https://www.tibia.com/community/?subtopic=highscores&world=&beprotection=-1&category=6&profession=&currentpage=1",
                        selectors.getTrBgcolor2(),
                        selectors.getTd());

        assertTrue(nicks.size() == 50, "The highscore page is NOT working.");
    }

}
