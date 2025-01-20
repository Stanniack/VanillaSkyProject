package com.tibiadata.tibia_crawler.model.utils;

public class StringUtils {

    /**
     * Divide a string de entrada usando a expressão regular fornecida e retorna
     * um array com no máximo duas partes.
     *
     * @param item a string a ser dividida e processada
     * @param regex a expressão regular usada para dividir a string
     * @return um array contendo até duas partes da string processada
     */
    public static String[] split(String item, String regex) {
        return item.split(regex, 2);
    }

    /**
     * Divide a string fornecida pela expressão regular especificada.
     *
     * @param item a string a ser dividida
     * @param regex a expressão regular a ser usada para dividir
     * @return um array de substrings
     */
    public static String[] multSplit(String item, String regex) {
        return item.split(regex);
    }

    /**
     * Divide a string de entrada usando a expressão regular fornecida e retorna
     * um array com um tamanho máximo definido pelo parâmetro arraySize.
     *
     * @param item a string a ser dividida e processada
     * @param regex a expressão regular usada para dividir a string
     * @param arraySize o número máximo de partes no array resultante
     * @return um array contendo até arraySize partes da string processada
     */
    public static String[] split(String item, String regex, int arraySize) {
        return item.split(regex, arraySize);
    }

    /**
     * Remove a primeira ocorrência de espaço em branco no início da string
     * fornecida.
     *
     * @param str a string a ser processada
     * @return a string sem o espaço em branco inicial
     */
    public static String replaceFirstSpace(String str) {
        return str.replaceFirst("^\\s+", "");
    }

    /**
     * Divide a string fornecida por ":" e remove o espaço em branco do início
     * da substring no índice especificado.
     *
     * @param item a string a ser dividida
     * @param index o índice da substring a ser processada
     * @return a substring processada, sem o espaço em branco inicial
     */
    public static String splitAndReplace(String item, int index) {
        return replaceFirstSpace(split(item, ":")[index]);
    }

    /**
     * Divide a string fornecida pela expressão regular especificada, limita o
     * tamanho do array e remove o espaço em branco do início da substring no
     * índice especificado.
     *
     * @param item a string a ser dividida
     * @param regex a expressão regular a ser usada para dividir
     * @param arraySize o tamanho máximo do array resultante
     * @param indexItem o índice da substring a ser processada
     * @return a substring processada, sem o espaço em branco inicial
     */
    public static String splitAndReplace(String item, String regex, int arraySize, int indexItem) {
        return replaceFirstSpace(split(item, regex, arraySize)[indexItem]);
    }

    /**
     * Formata um nome, substituindo os espaços por "+".
     *
     * @param name O nome a ser formatado.
     * @return O nome formatado, com espaços substituídos por "+".
     */
    public static String nameFormater(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.replace(" ", "+");
    }

}
