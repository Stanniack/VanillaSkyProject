package com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies;

import com.tibiadata.tibia_crawler.model.entities.Personage;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 * @author Devmachine
 */
@Component
public class AttributeHandler {

    /**
     * Validates and updates a string attribute if it is null or different from
     * a new value.
     *
     * @param personage
     * @param getter a Supplier that retrieves the current value of the
     * attribute.
     * @param setter a BiConsumer that sets the new value for the attribute.
     * @param newValue the new value to be validated and potentially set.
     * @return {@code true} if the attribute was updated; {@code false}
     * otherwise.
     */
    public static boolean attributeValidator(Personage personage, Supplier<String> getter, BiConsumer<Personage, String> setter, String newValue) {
        String currentValue = getter.get(); // Obtém o valor atual do atributo usando o getter

        // Verifica se o valor atual é nulo ou diferente do novo valor
        if (currentValue == null || !currentValue.equals(newValue)) {
            setter.accept(personage, newValue); // Atualiza o atributo usando o setter
            return true;
        }
        return false;
    }
}
