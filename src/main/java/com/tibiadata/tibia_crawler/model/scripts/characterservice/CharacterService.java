package com.tibiadata.tibia_crawler.model.scripts.characterservice;

import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.HouseStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.CreatedStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.LastLoginStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.LevelProgressStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.WorldStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.TitleStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.ObjectStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.AchievementsStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.SexStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.AttributeStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.AccStatusStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.VocationStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.DeathStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.ResidenceStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.LoyaltyTitleStrategy;
import com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies.GuildStrategy;
import com.tibiadata.tibia_crawler.model.connections.GetContent;
import com.tibiadata.tibia_crawler.model.entities.FormerName;
import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.handler.PriorityHandler;
import com.tibiadata.tibia_crawler.model.persistence.FormerNamePersistence;
import com.tibiadata.tibia_crawler.model.persistence.PersonagePersistence;
import com.tibiadata.tibia_crawler.model.scripts.onlineservice.OnlineService;
import com.tibiadata.tibia_crawler.model.utils.CalendarUtils;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tibiadata.tibia_crawler.model.utils.ElementsUtils;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Devmachine
 */
@Scope("prototype")
@Service
public class CharacterService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CharacterService.class);
    //
    private static final String NAME = "Name:";
    private static final String FORMERNAMES = "Former Names:";
    //
    private Map<String, AttributeStrategy> attributesStrategyMap;
    //
    private Map<String, ObjectStrategy> objectsStrategyMap;
    //
    private static final String TRADED = "(traded)";
    private static final String DELETED = ", will be deleted at (\\w{3} \\d{1,2} \\d{4}, \\d{2}:\\d{2}:\\d{2}) (CET|CEST)";
    //
    private static final int ITEM = 1;
    private boolean needsPersistence = false;
    //
    private final PersonagePersistence personagePersistence;
    private final FormerNamePersistence formerNamePersistence;
    //
    private Personage personage;
    private final List<FormerName> formerNames = new ArrayList<>();
    //
    private final GetContent getContent;
    private final ElementsUtils elementUtils;
    private final PriorityHandler pHandler;

    @Autowired
    public CharacterService(PersonagePersistence personagePersistence, FormerNamePersistence formerNamePersistence, GetContent getContent, ElementsUtils elementUtils, PriorityHandler pHandler) {
        this.personagePersistence = personagePersistence;
        this.formerNamePersistence = formerNamePersistence;
        //
        this.getContent = getContent;
        this.elementUtils = elementUtils;
        this.pHandler = pHandler;
    }

    @Autowired
    public void setAttributesStrategies(TitleStrategy tStrategy, VocationStrategy vStrategy, ResidenceStrategy rStrategy,
            LastLoginStrategy llStrategy, AccStatusStrategy asStrategy, LoyaltyTitleStrategy ltStrategy, CreatedStrategy cStrategy,
            List<AttributeStrategy> attrStrategiesList) {
        this.attributesStrategyMap = new HashMap<>();
        attrStrategiesList.forEach(strategy -> attributesStrategyMap.put(strategy.getKey(), strategy));
    }

    @Autowired
    public void setObjectsStrategies(SexStrategy sStrategy, LevelProgressStrategy lpStrategy, AchievementsStrategy aStrategy,
            WorldStrategy wStrategy, GuildStrategy gStrategy, HouseStrategy hStrategy, DeathStrategy dStrategy, List<ObjectStrategy> objStrategyList) {
        this.objectsStrategyMap = new HashMap<>();
        objStrategyList.forEach(strategy -> objectsStrategyMap.put(strategy.getKey(), strategy));
    }

    public Integer fetchCharacter(String url) {
        List<String> itens = null;
        try {
            itens = pHandler.reorderList(getContent.getTableContent(url, elementUtils.getTrBgcolor(), elementUtils.getTr()));

            if (!itens.isEmpty()) {
                personageProcessor(itens);
            }

            return personage.getId();

        } catch (IOException ex) {
            Logger.getLogger(CharacterService.class.getName()).log(Level.SEVERE, "Erro ao processar o personagem");
            // guardar a url do personagem para persistência posterior
            // dormir por n secs
        }
        return -1;
    }

    public void fetchCharacter(List<String> itens) {
        personageProcessor(itens);
    }

    private void personageProcessor(List<String> itens) {
        personageValidator(itens);
        personageAttributesHandler(itens);
        persistPersonage(personage); // É preciso persistir o personagem para certificar que a instância do objeto tem um ID para regras posteriores
        persistFormerName(personage);
        personageObjectsHandler(itens);
        //personage = null;
    }

    private void personageValidator(List<String> itens) {
        Personage recoveredPersonage = null;
        String name = null;

        for (String item : itens) {

            if (item.contains(NAME)) {
                name = StringUtils.splitAndReplace(item, ITEM).replace(TRADED, "").replaceAll(DELETED, "");// trata o nome do personagem, elima (traded) se o personagem vier do Bazaar
                recoveredPersonage = personagePersistence.findByName(name); // recupera personagem se existir

                personage = (recoveredPersonage == null) ? new Personage() : recoveredPersonage; // recupera ou cria novo personagem

                if (recoveredPersonage == null) {
                    needsPersistence = true;
                    personage.setName(name); // set name apenas se for um novo personagem
                }

            } else if (item.contains(FORMERNAMES)) {
                boolean existsName = recoveredPersonage != null; // se for falso, o personagem existe mas trocou de nome
                formerNameValidator(existsName, item, name);
            }
        }
    }

    private void formerNameValidator(boolean existsName, String formerName, String name) {
        String[] splittedFormerNames = StringUtils.multSplit(formerName, "[:,]");

        // Se não existe é novo ou trocou de nick
        if (!existsName) {
            for (int i = ITEM; i < splittedFormerNames.length; i++) {
                String currentFormerName = StringUtils.replaceFirstSpace(splittedFormerNames[i]);

                // pelo menos um former name existe na coluna de names? Personagem existe mas nome foi trocado
                if (personagePersistence.existsByName(currentFormerName)) {
                    personage = personagePersistence.findByName(currentFormerName); // Recupera o personagem existente com o antigo nome
                    personage.setName(name); // Seta novo nome
                }
                formerNames.add(new FormerName(currentFormerName, Calendar.getInstance())); // add novo fn
            }
        }
    }

    private void persistPersonage(Personage p) {

        if (needsPersistence) {
            logger.info("{} persistido.", p.getName());

            if (p.getRegisteredDate() == null) {
                p.setRegisteredDate(Calendar.getInstance()); // registra data caso não houver
            }
            personagePersistence.save(p);

        } else {
            logger.info("{} não precisa de persistência.", personage.getName());
        }
    }

    private void persistFormerName(Personage p) {

        for (FormerName formerName : formerNames) {
            Date date = formerNamePersistence.findDateOfLastFormerNameRegistered(formerName.getFormerName(), p.getId());

            // chama o bd só se date != null
            boolean greatherThan180days
                    = date != null && CalendarUtils.greaterThan180Days(Calendar.getInstance(), CalendarUtils.convertToCalendar(date));

            boolean isFnExists = formerNamePersistence.isFormerNameFromPersonage(formerName.getFormerName(), p.getId());

            // Se o former name NÃO existe, persistir
            // ou a data do último FN EXISTE associada ao Personage e a data de registro for maior ou igual a 180 dias, persistir.
            if (!isFnExists || (date != null && greatherThan180days)) {
                formerName.setPersonage(p);
                formerNamePersistence.save(formerName);

            } else { // Senão o formername EXISTE e está associado ao id do personage, não persistir pois já existe no bd
                logger.info("FN {} já existe no bd.", formerName.getFormerName());
            }
        }
    }

    private void personageAttributesHandler(List<String> itens) {

        for (String item : itens) { //16
            for (var entry : attributesStrategyMap.entrySet()) { //x7, = 112 verificações
                if (item.contains(entry.getKey()) || item.matches(entry.getKey())) {
                    needsPersistence = entry.getValue().apply(personage, item, needsPersistence);
                }
            }
        }
    }

    private void personageObjectsHandler(List<String> itens) {
        for (String item : itens) {
            for (var entry : objectsStrategyMap.entrySet()) {
                if (item.contains(entry.getKey()) || item.matches(entry.getKey())) {
                    entry.getValue().apply(personage, item);
                }
            }
        }
    }

}
