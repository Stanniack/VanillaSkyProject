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

    /**
     * Splits the given string by the specified regex pattern.
     *
     * @param item the string to split
     * @param regex the regex pattern to use for splitting
     * @return an array of substrings
     */
    public String[] multSplit(String item, String regex) {
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
     * Removes the first occurrence of leading whitespace from the given string.
     *
     * @param str the string to process
     * @return the string without leading whitespace
     */
    public String replaceFirstSpace(String str) {
        return str.replaceFirst("^\\s+", "");
    }

    /**
     * Splits the given string by ":" and removes leading whitespace from the
     * specified index.
     *
     * @param item the string to split
     * @param index the index of the substring to process
     * @return the processed substring with leading whitespace removed
     */
    public String splitAndReplace(String item, int index) {
        return replaceFirstSpace(split(item, ":")[index]);
    }

    /**
     * Splits the given string by the specified regex, limits the array size,
     * and removes leading whitespace from the specified index.
     *
     * @param item the string to split
     * @param regex the regex pattern to use for splitting
     * @param arraySize the maximum size of the resulting array
     * @param indexItem the index of the substring to process
     * @return the processed substring with leading whitespace removed
     */
    public String splitAndReplace(String item, String regex, int arraySize, int indexItem) {
        return replaceFirstSpace(split(item, regex, arraySize)[indexItem]);
    }

}
