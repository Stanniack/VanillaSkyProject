package com.tibiadata.tibia_crawler.model.utils;

import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class ElementsUtils {

    private final String td = "td";
    private final String tr = "tr";
    private final String trEvenOdd = "tr.Even, tr.Odd";
    private final String trBgcolor = "tr[bgcolor='#F1E0C6'], tr[bgcolor='#D4C0A1']";
    private final String trBgcolor2 = "tr[style='background-color:#F1E0C6;'], tr[style='background-color:#D4C0A1;']";

    public String getTd() {
        return td;
    }

    public String getTrEvenOdd() {
        return trEvenOdd;
    }

    public String getTrBgcolor() {
        return trBgcolor;
    }

    public String getTr() {
        return tr;
    }

    public String getTrBgcolor2() {
        return trBgcolor2;
    }
}
