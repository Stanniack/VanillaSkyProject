package com.tibiadata.tibia_crawler.model.utils;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Devmachine
 */
public class TibiaUtils {

    private static List<String> worlds = Arrays.asList(
            "Aethera", "Ambra", "Antica", "Astera", "Belobra", "Bona", "Bravoria", "Calmera",
            "Cantabra", "Celebra", "Celesta", "Collabra", "Descubra", "Dia", "Divina", "Epoca",
            "Esmera", "Etebra", "Ferobra", "Fibera", "Firmera", "Flamera", "Gentebra", "Gladera",
            "Gladibra", "Gravitera", "Harmonia", "Havera", "Honbra", "Inabra", "Issobra", "Jacabra",
            "Jadebra", "Jaguna", "Kalibra", "Karmeya", "Lobera", "Luminera", "Lutabra", "Malivora",
            "Menera", "Monza", "Nefera", "Nevia", "Obscubra", "Oceanis", "Ombra", "Ourobra", "Pacera",
            "Peloria", "Premia", "Quebra", "Quelibra", "Quidera", "Quintera", "Rasteibra", "Refugia",
            "Retalia", "Runera", "Secura", "Serdebra", "Solidera", "Stralis", "Talera", "Temera",
            "Thyria", "Tornabra", "Ulera", "Unebra", "Ustebra", "Vandera", "Venebra", "Victoris",
            "Vitera", "Vunira", "Wadira", "Wildera", "Wintera", "Xyla", "Yara", "Yonabra", "Yovera",
            "Yubra", "Zephyra", "Zuna", "Zunera");

    private static List<String> experimentalWorlds = Arrays.asList(
            "Zuna", "Zunera");

    private static List<String> pvpServers = Arrays.asList(
            "Aethera", "Antica", "Cantabra", "Ferobra", "Honbra", "Lobera", "Havera", "Ombra", "Ourobra",
            "Peloria", "Quebra", "Quelibra", "Rasteibra", "Solidera", "Tornabra", "Secura", "Serdebra", "Wintera"
    );

    private static List<String> optPvpServers = Arrays.asList(
            "Astera", "Belobra", "Bona", "Bravoria", "Calmera", "Celebra", "Celesta", "Collabra", "Descubra",
            "Esmera", "Etebra", "Fibera", "Firmera", "Gentebra", "Gladera", "Harmonia", "Issobra", "Jacabra",
            "Jadebra", "Kalibra", "Karmeya", "Luminera", "Menera", "Nefera", "Nevia", "Monza", "Neiva", "Obscubra",
            "Oceanis", "Ombra", "Pacera", "Refugia", "Secura", "Ustebra", "Venebra", "Yovera", "Yubra", "Zephyra"
    );

    private static List<String> retroOpenPvpServers = Arrays.asList(
            "Ambra", "Divina", "Epoca", "Fibera", "Flamera", "Gladibra", "Lutabra", "Malivora", "Lutabra",
            "Quebra", "Temera"
    );

    private static List<String> retroHardcorePvpservers = Arrays.asList(
            "Gravitera", "Jacabra", "Obscubra", "Retalia", "Zunera"
    );

    private static List<String> southAmericaServers = Arrays.asList(
            "Ambra", "Belobra", "Cantabra", "Celebra", "Collabra", "Descubra", "Ferobra", "Gentebra", "Havera",
            "Honbra", "Inabra", "Issobra", "Jacabra", "Jadebra", "Kalibra", "Lutabra", "Malivora", "Menera",
            "Monza", "Neiva", "Ombra", "Pacera", "Quebra", "Quelibra", "Rasteibra", "Serdebra", "Tornabra", "Unebra",
            "Ustebra", "Venebra", "Yara", "Yonabra", "Yubra"
    );

    private static List<String> northAmericaServers = Arrays.asList(
            "Aethera", "Astera", "Calmera", "Celebra", "Fibera", "Firmera", "Gladera", "Gravitera", "Havera",
            "Lobera", "Menera", "Nefera", "Quidera", "Quintera", "Runera", "Solidera", "Talera", "Temera", "Ulera",
            "Vandera", "Vitera", "Wintera"
    );

    private static List<String> europeServers = Arrays.asList(
            "Antica", "Bona", "Bravoria", "Celesta", "Dia", "Divina", "Epoca", "Harmonia", "Jaguna", "Karmeya",
            "Luminera", "Monza", "Neiva", "Nevia", "Peloria", "Premia", "Thyria", "Vunira", "Wadira", "Xyla", "Yara",
            "Zephyra", "Zuna"
    );

    public static List<String> getWorlds() {
        return worlds;
    }

    public static List<String> getExperimentalWorlds() {
        return experimentalWorlds;
    }

    public static List<String> getPvpServers() {
        return pvpServers;
    }

    public static List<String> getOptPvpServers() {
        return optPvpServers;
    }

    public static List<String> getRetroOpenPvpServers() {
        return retroOpenPvpServers;
    }

    public static List<String> getRetroHardcorePvpservers() {
        return retroHardcorePvpservers;
    }

    public static List<String> getSouthAmericaServers() {
        return southAmericaServers;
    }

    public static List<String> getNorthAmericaServers() {
        return northAmericaServers;
    }

    public static List<String> getEuropeServers() {
        return europeServers;
    }

}
