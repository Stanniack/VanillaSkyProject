package com.tibiadata.tibia_crawler.model.scripts.characterservice;

import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import org.springframework.stereotype.Component;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Scope;

@Component
@Scope("prototype")
public class CharacterProcessor {

    private final ObjectProvider<CharacterService> characterServiceProvider;

    // Construtor para injeção de dependência
    public CharacterProcessor(ObjectProvider<CharacterService> characterServiceProvider) {
        this.characterServiceProvider = characterServiceProvider;
    }

    // Método que recebe uma lista de nomes e faz o processamento
    public void processNames(List<String> names) {
        for (String name : names) {
            processName(name);
        }
    }

    public Integer processName(String name) {
        CharacterService characterService = getCharacterServiceProvider().getObject();
        return characterService.fetchCharacter("https://www.tibia.com/community/?name=" + StringUtils.nameFormater(name));
    }

    private ObjectProvider<CharacterService> getCharacterServiceProvider() {
        return characterServiceProvider;
    }

}
