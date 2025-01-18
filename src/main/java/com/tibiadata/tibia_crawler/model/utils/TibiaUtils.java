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

    public static List<String> getWorlds() {
        return worlds;
    }

}
