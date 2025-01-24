package com.tibiadata.tibia_crawler.model.utils;

import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class ElementsUtils {

    public String getTd() {
        return "td";
    }

    public String getTrEvenOdd() {
        return "tr.Even, tr.Odd";
    }

    public String getTrBgcolor() {
        return "tr[bgcolor='#F1E0C6'], tr[bgcolor='#D4C0A1']";
    }

    public String getTr() {
        return "tr";
    }

    public String getTrBgcolor2() {
        return "tr[style='background-color:#F1E0C6;'], tr[style='background-color:#D4C0A1;']";
    }
}
