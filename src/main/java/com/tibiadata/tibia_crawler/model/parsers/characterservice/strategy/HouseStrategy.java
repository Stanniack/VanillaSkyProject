package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import com.tibiadata.tibia_crawler.model.entities.House;
import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.persistence.HousePersistence;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class HouseStrategy implements ObjectStrategy {

    //
    @Autowired
    private HousePersistence hp;
    //
    private static final short ITEM = 1;
    //
    private List<House> houses = new ArrayList<>();
    private List<House> cacheHouses;

    @Override
    public <T> void apply(Personage personage, String newValue) {
        String houseName = StringUtils.splitAndReplace(newValue, ":|\\s?is paid until\\s?", 3, 1);
        String paidUntil = StringUtils.splitAndReplace(newValue, ":|\\s?is paid until\\s?", 3, 2);

        houseValidator(personage, houseName, paidUntil);
        persistHouses(personage);

    }

    private void houseValidator(Personage personage, String curHouseName, String curPaidUntil) {

        // Armezena a verificação do banco de dados para evitar chamadas repetidas inúteis, uma vez que o valor no db não se altera dinamicamente enquanto a classe estiver sendo instanciada
        if (cacheHouses == null) {
            cacheHouses = hp.findLastThreeHousesById(personage.getId());
        }

        if (cacheHouses == null) {// Se dbHouse for nulo, persiste as houses que encontrar
            houses.add(new House(curHouseName, curPaidUntil, Calendar.getInstance()));

        } else {
            boolean found = false;
            House curHouse = null;

            for (House cacheHouse : cacheHouses) {
                if (cacheHouse.getHouseName().equals(curHouseName)) { // Se encontrar a house na lista do bd, flag para persistência
                    found = true;
                    curHouse = cacheHouse;
                    break;
                }
            }

            if (found && !curHouse.getPaidUntil().equals(curPaidUntil)) {// Se achou na lista do bd e paidUntil é diferente, alterar e persistir
                curHouse.setPaidUntil(curPaidUntil);
                houses.add(curHouse);

            } else if (!found) { //Senão achou a house é nova
                houses.add(new House(curHouseName, curPaidUntil, Calendar.getInstance()));
            }
        }

    }

    private void persistHouses(Personage p) {
        houses.forEach(houseToPersist -> {
            houseToPersist.setPersonage(p);
            hp.save(houseToPersist);
        });
    }

}
