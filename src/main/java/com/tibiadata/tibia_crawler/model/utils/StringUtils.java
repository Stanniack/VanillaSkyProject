package com.tibiadata.tibia_crawler.model.utils;

/**
 *
 * @author Devmachine
 */
public class StringUtils {

    /**
     * Splits the input string using the provided regex and returns an array
     * with at most two parts.
     *
     * @param item the string to be split and processed
     * @param regex the regular expression used to split the string
     * @return an array containing up to two parts of the processed string
     */
    public static String[] splitAndReplace(String item, String regex) {
        return item.split(regex, 2);
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
