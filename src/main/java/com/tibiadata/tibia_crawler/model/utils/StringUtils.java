package com.tibiadata.tibia_crawler.model.utils;

/**
 *
 * @author Devmachine
 */
public class StringUtils {

    /**
     *
     * @param item var type string to be processed by split and replace
     * @param regex regex to use for process string
     * @return an array of processed strings
     */
    public static String[] splitAndReplace(String item, String regex) {
        return item.split(regex);
    }

    /**
     *
     * @param str string to replace first index
     * @return string treated without first index containing space
     */
    public static String replaceFirstSpace(String str) {
        return str.replaceFirst("^\\s+", "");
    }
}
