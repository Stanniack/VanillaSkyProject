package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.entities.Sex;
import com.tibiadata.tibia_crawler.model.persistence.SexPersistence;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import jakarta.transaction.Transactional;
import java.util.Calendar;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Devmachine
 */
@Service
public class SexStrategy implements ObjectStrategy {

    //
    @Autowired
    private SexPersistence sp;
    //
    private static final short ITEM = 1;
    //
    private Sex sex;

    @Override
    @Transactional
    public <T> void apply(Personage personage, String newValue) {
        genericValidator(
                personage,
                StringUtils.splitAndReplace(newValue, ITEM),
                param -> sp.findLastSexById(param),
                (value, date) -> new Sex(value, date),
                newSex -> {
                    this.sex = newSex;
                });

        ObjectHandler.persistObject(sex, _sex -> _sex.setPersonage(personage), _sex -> sp.save(_sex));
    }

    private <T> void genericValidator(Personage personage, String newValue,
            Function<Integer, String> dbPersister, BiFunction<String, Calendar, T> object, Consumer<T> setter) {

        //String dbValue = (oldName != null) ? dbPersister.apply(oldName) : dbPersister.apply(personage.getName());//Se oldName existir, então verifica o último valor do antigo name
        String dbValue = dbPersister.apply(personage.getId());

        if (dbValue == null || !dbValue.equals(newValue)) {// Se valor buscado no db é null ou não é igual o valor atual, setar valor atual para persistência
            T newObject = object.apply(newValue, Calendar.getInstance()); // instancia novo objeto
            setter.accept(newObject); // altera valores
        }
    }
}
