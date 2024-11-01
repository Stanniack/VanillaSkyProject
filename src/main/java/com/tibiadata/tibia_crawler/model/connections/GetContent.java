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
