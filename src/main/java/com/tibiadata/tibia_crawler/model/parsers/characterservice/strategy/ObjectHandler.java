package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import java.util.Calendar;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class ObjectHandler {

    public static <T> void persistObject(T object, Consumer<T> setPersonage, Consumer<T> dbPersister) {
        if (object != null) {// Se for diferente de nulo, então foi criado ou alterado
            setPersonage.accept(object); //object.setPersonage(personage)
            dbPersister.accept(object); // persistence.save(object)
        }
    }

    public static <T> void genericValidator(Personage personage, String newValue,
            Function<Integer, String> dbPersister, BiFunction<String, Calendar, T> object, Consumer<T> setter) {

        String dbValue = dbPersister.apply(personage.getId());

        if (dbValue == null || !dbValue.equals(newValue)) {// Se valor buscado no db é null ou não é igual o valor atual, setar valor atual para persistência
            T newObject = object.apply(newValue, Calendar.getInstance()); // instancia novo objeto
            setter.accept(newObject); // altera valores
        }
    }
}
