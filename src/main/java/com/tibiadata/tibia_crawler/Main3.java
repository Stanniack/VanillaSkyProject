package com.tibiadata.tibia_crawler;

import com.tibiadata.tibia_crawler.model.connections.GetContent;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Devmachine
 */
public class Main3 {

    public static void main(String[] args) {
        try {
            List<List<String>> list = new GetContent().getTableContent("https://www.tibia.com/community/?subtopic=worlds&world=Antica",
                    ("tr.Odd, tr.Even"),
                    "td");
            
            for (List<String> item : list) {
                System.out.println(item);
            }
        } catch (IOException ex) {
            Logger.getLogger(Main3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
