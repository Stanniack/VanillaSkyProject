package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import java.util.function.Consumer;

/**
 *
 * @author Devmachine
 */
public class ObjectHandler {

    public static <T> void persistObject(T object, Consumer<T> setPersonage, Consumer<T> dbPersister) {
        if (object != null) {// Se for diferente de nulo, então foi criado ou alterado
            setPersonage.accept(object); //object.setPersonage(personage)
            dbPersister.accept(object); // persistence.save(object)
        }
    }
}
