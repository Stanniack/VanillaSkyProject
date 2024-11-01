package com.tibiadata.tibia_crawler.model.connections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Devmachine
 */
public class GetContent {

    /**
     * @param url link to get HTTP connection
     * @param select1 HTML tag1 ex: table, tbody, tr, td and its selectors
     * @param select2 HTML tag2 ex: table, tbody, tr, td and its selectors
     * @return a list of string lists containing all
     * &lt;tag1&gt;&lt;tag2&gt;&lt;/tag2&gt;&lt;/tag1&gt; elements.
     * @throws IOException
     */
    public List<List<String>> getTableContent(String url, String select1, String select2) throws IOException {
        Document document = Jsoup.connect(url).get(); // Pega o html

        String html = document.toString(); // HTML a ser limpo

        // Fazer parsing do HTML
        Document parserDocument = Jsoup.parse(html);

        List<List<String>> allContent = new ArrayList<>();

        for (Element tr : parserDocument.select(select1)) {
            List<String> eachTag = new ArrayList<>();

            for (Element td : tr.select(select2)) {
                eachTag.add(td.text()); // Adiciona o texto de cada à lista
            }
            allContent.add(eachTag);
        }
        return allContent;
    }
}
