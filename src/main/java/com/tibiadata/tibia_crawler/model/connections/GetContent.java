package com.tibiadata.tibia_crawler.model.connections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class GetContent {

    private BrotliDescompacter brotliDescompacter;

    @Autowired
    public GetContent(BrotliDescompacter brotliDescompacter) {
        this.brotliDescompacter = brotliDescompacter;
    }

    /**
     * Fetches and extracts table content from a given URL using CSS selectors.
     *
     * @param url the URL to fetch the HTML content from.
     * @param select1 the first CSS selector to filter parent elements.
     * @param select2 the second CSS selector to filter child elements within
     * the parent.
     * @return a list of strings, where each string represents the text content
     * of a child element filtered by the second selector for all matching
     * parent elements.
     * @throws IOException if an error occurs while connecting to the URL or
     * fetching the HTML content.
     */
    public List<String> getTableContent(String url, String select1, String select2) throws IOException {
        String html = brotliDescompacter.gzipBrDescompacter(url); // HTML a ser limpo

        // Fazer parsing do HTML
        Document parserDocument = Jsoup.parse(html);

        List<String> allContent = new ArrayList<>();

        for (Element filter1 : parserDocument.select(select1)) {
            for (Element filter2 : filter1.select(select2)) {
                allContent.add(filter2.text()); // Adiciona o texto de cada linha à lista
            }
        }
        return allContent;
    }

    /**
     * Fetches and parses online players' data from a given URL based on CSS
     * selectors.
     *
     * @param url the URL to fetch the HTML content from.
     * @param select1 the first CSS selector to filter parent elements.
     * @param select2 the second CSS selector to filter child elements within
     * the parent.
     * @return a list of lists, where each inner list contains the text content
     * of child elements filtered by the second selector for each parent
     * element.
     * @throws IOException if an error occurs while connecting to the URL or
     * fetching the HTML content.
     */
    public List<List<String>> getPlayersInfo(String url, String select1, String select2) throws IOException {
        String html = brotliDescompacter.gzipBrDescompacter(url); // HTML a ser limpo

        // Fazer parsing do HTML
        Document parserDocument = Jsoup.parse(html);

        List<List<String>> allContent = new ArrayList<>();

        for (Element filter1 : parserDocument.select(select1)) {
            List<String> eachPlayer = new ArrayList<>();
            for (Element filter2 : filter1.select(select2)) {
                eachPlayer.add(filter2.text()); // Adiciona a lista contendo a linha a uma segunda linha
            }
            allContent.add(eachPlayer);
        }
        return allContent;
    }
}
