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
     * @param item the string to be multSplit and processed
     * @param regex the regular expression used to multSplit the string
     * @return an array containing up to two parts of the processed string
     */
    public String[] split(String item, String regex) {
        return item.split(regex, 2);
    }
    
    public String[] multSplit(String item, String regex){
        return item.split(regex);
    }

    /**
     * Splits the input string using the provided regex and returns an array
     * with a maximum size defined by the given arraySize.
     *
     * @param item the string to be multSplit and processed
     * @param regex the regular expression used to multSplit the string
     * @param arraySize the maximum number of parts in the resulting array
     * @return an array containing up to arraySize parts of the processed string
     */
    public String[] split(String item, String regex, int arraySize) {
        return item.split(regex, arraySize);
    }

    /**
     *
     * @param str string to replace first index
     * @return string treated without first index containing space
     */
    public String replaceFirstSpace(String str) {
        return str.replaceFirst("^\\s+", "");
    }
}
