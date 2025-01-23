package com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies;

import com.tibiadata.tibia_crawler.model.entities.OnlineTime;
import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.persistence.OnlineTimePersistence;
import com.tibiadata.tibia_crawler.model.utils.CalendarUtils;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
@Scope("prototype")
public class OnlineTimeStrategy {

    @Autowired
    private OnlineTimePersistence otp;
    //
    private OnlineTime onlineTime;
    //
    private boolean needsPersistence = false;

    public void apply(Integer personageId, Integer onlineSecs) {
        onlineTimeValidator(personageId, onlineSecs);
        save();
    }

    private void onlineTimeValidator(Integer personageId, Integer onlineMs) {
        //Compara se data da tabela OnlineTime são do mesmo dia
        // Compara campo secs se tem o mesmo valor
        onlineTime = otp.findLastTimeOnlineById(personageId);

        if (onlineTime == null || !CalendarUtils.isSameDate(onlineTime.getRegisteredDate())) { // Se não existir ou se a data buscada for diferente a de hoje
            Personage p = new Personage();
            p.setId(personageId);
            onlineTime = new OnlineTime(onlineMs / 1000, Calendar.getInstance(), p);
            needsPersistence = true;

        } else { // Senão, soma tempo diário
            Integer previousTimeSecs = onlineTime.getSeconds() * 1000; // * 1000 para ms
            onlineTime.setSeconds((onlineMs + previousTimeSecs) / 1000);// Soma tempo já obtido com o novo tempo e transforma ms em secs
            needsPersistence = true;
        }
    }

    private void save() {
        if (needsPersistence) {
            otp.save(onlineTime);
        }
    }

}
