package com.tibiadata.tibia_crawler.model.utils;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author Devmachine
 */
public class TibiaUtils {

    @Getter
    private static final List<String> allWorlds = Arrays.asList(
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

    @Getter
    private static final List<String> experimentalWorlds = Arrays.asList(
            "Zuna", "Zunera");

    @Getter
    private static final List<String> openPvpWorlds = Arrays.asList(
            "Aethera", "Antica", "Cantabra", "Ferobra", "Havera", "Honbra", "Inabra", "Jadebra", "Jaguna",
            "Lobera", "Ombra", "Ourobra", "Peloria", "Premia", "Quelibra", "Quidera", "Quintera", "Rasteibra",
            "Runera", "Serdebra", "Solidera", "Talera", "Thyria", "Tornabra", "Unebra", "Vandera", "Victoris",
            "Vunira", "Wintera", "Xyla", "Yara"
    );

    @Getter
    private static final List<String> optionalPvpWorlds = Arrays.asList(
            "Astera", "Belobra", "Bona", "Bravoria", "Calmera", "Celebra", "Celesta", "Collabra",
            "Descubra", "Dia", "Esmera", "Etebra", "Gentebra", "Gladera", "Harmonia", "Issobra",
            "Kalibra", "Karmeya", "Luminera", "Menera", "Monza", "Nefera", "Nevia", "Oceanis", "Pacera",
            "Refugia", "Secura", "Stralis", "Ulera", "Ustebra", "Venebra", "Vitera", "Wadira", "Yonabra",
            "Yovera", "Yubra", "Zephyra"
    );

    @Getter
    private static final List<String> retroOpenPvpServers = Arrays.asList(
            "Ambra", "Divina", "Epoca", "Fibera", "Firmera", "Flamera", "Gladibra", "Lutabra",
            "Malivora", "Quebra", "Temera"
    );

    @Getter
    private static final List<String> retroHardcorePvpservers = Arrays.asList(
            "Gravitera", "Jacabra", "Obscubra", "Retalia", "Wildera"
    );

}
