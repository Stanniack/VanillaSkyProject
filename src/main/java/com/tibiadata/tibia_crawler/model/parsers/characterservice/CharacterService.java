package com.tibiadata.tibia_crawler.model.parsers.characterservice;

import com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy.GuildStrategy;
import com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy.*;
import com.tibiadata.tibia_crawler.model.connections.GetContent;
import com.tibiadata.tibia_crawler.model.entities.Achievements;
import com.tibiadata.tibia_crawler.model.entities.Death;
import com.tibiadata.tibia_crawler.model.entities.FormerName;
import com.tibiadata.tibia_crawler.model.entities.Guild;
import com.tibiadata.tibia_crawler.model.entities.House;
import com.tibiadata.tibia_crawler.model.entities.LevelProgress;
import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.entities.Sex;
import com.tibiadata.tibia_crawler.model.entities.World;
import com.tibiadata.tibia_crawler.model.handler.PriorityHandler;
import com.tibiadata.tibia_crawler.model.persistence.AchievementsPersistence;
import com.tibiadata.tibia_crawler.model.persistence.DeathPersistence;
import com.tibiadata.tibia_crawler.model.persistence.FormerNamePersistence;
import com.tibiadata.tibia_crawler.model.persistence.GuildPersistence;
import com.tibiadata.tibia_crawler.model.persistence.HousePersistence;
import com.tibiadata.tibia_crawler.model.persistence.LevelProgressPersistence;
import com.tibiadata.tibia_crawler.model.persistence.PersonagePersistence;
import com.tibiadata.tibia_crawler.model.persistence.SexPersistence;
import com.tibiadata.tibia_crawler.model.persistence.WorldPersistence;
import com.tibiadata.tibia_crawler.model.utils.CalendarUtils;
import java.util.List;
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
import org.jsoup.helper.ValidationException;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Devmachine
 */
@Scope("prototype")
@Service
public class CharacterService {

    //
    private static final String NAME = "Name:";
    private static final String FORMERNAMES = "Former Names:";
    //
    private Map<String, AttributeStrategy> attributesStrategyMap;
    private static final String TITLE = ".*[0-9]+ titles unlocked.*";
    private static final String VOCATION = "Vocation:";
    private static final String RESIDENCE = "Residence:";
    private static final String LASTLOGIN = "Last Login:";
    private static final String ACCSTATUS = "Account Status:";
    private static final String LOYALTYTITLE = "Loyalty Title:";
    private static final String CREATED = "Created:";
    //
    private Map<String, ObjectStrategy> objectsStrategyMap = new HashMap<>();
    private static final String SEX = "Sex:";
    private static final String LEVEL = "Level:";
    private static final String ACHIEVEMENTS = "Achievement Points:";
    private static final String WORLD = "World:";
    private static final String GUILD = "Guild Membership:";
    private static final String HOUSE = "House:";
    private static final String DEATH = "^\\w{3} \\d{2} \\d{4}, \\d{2}:\\d{2}:\\d{2} \\w+.*";
    //
    private static final String TRADED = "(traded)";
    //
    private static final int ITEM = 1;
    //
    private boolean existsName = false;
    private boolean needsPersistence = false;
    private String oldName;
    //
    @Autowired
    private PersonagePersistence pp;
    @Autowired
    private FormerNamePersistence fnp;
    //
    private Personage personage;
    private List<FormerName> formerNames;
    //
    private Calendar calendar;
    private GetContent getContent;
    private ElementsUtils elementUtils;
    private PriorityHandler pHandler;

    public CharacterService() {
        this.attributesStrategyMap = new HashMap<>();
        this.attributesStrategyMap.put(TITLE, new TitleStrategy());
        this.attributesStrategyMap.put(VOCATION, new VocationStrategy());
        this.attributesStrategyMap.put(RESIDENCE, new ResidenceStrategy());
        this.attributesStrategyMap.put(LASTLOGIN, new LastLoginStrategy());
        this.attributesStrategyMap.put(ACCSTATUS, new AccStatusStrategy());
        this.attributesStrategyMap.put(LOYALTYTITLE, new LoyaltyTitleStrategy());
        this.attributesStrategyMap.put(CREATED, new CreatedStrategy());

        this.formerNames = new ArrayList<>();

        this.calendar = Calendar.getInstance();
        this.calendar.set(Calendar.MILLISECOND, 0); // eliminar pontos flutuantes de MS ao persistir datas
        this.getContent = new GetContent();
        this.elementUtils = new ElementsUtils();
        this.pHandler = new PriorityHandler();
    }

    @Autowired
    public void setObjectsStrategies(SexStrategy sStrategy, LevelProgressStrategy lpStrategy, AchievementsStrategy aStrategy,
            WorldStrategy wStrategy, GuildStrategy gStrategy, HouseStrategy hStrategy, DeathStrategy dStrategy) {
        this.objectsStrategyMap.put(SEX, sStrategy);
        this.objectsStrategyMap.put(LEVEL, lpStrategy);
        this.objectsStrategyMap.put(ACHIEVEMENTS, aStrategy);
        this.objectsStrategyMap.put(WORLD, wStrategy);
        this.objectsStrategyMap.put(GUILD, gStrategy);
        this.objectsStrategyMap.put(HOUSE, hStrategy);
        this.objectsStrategyMap.put(DEATH, dStrategy);
    }

    public void fetchCharacter(String url) {

        try {
            List<String> itens = pHandler.reorderList(getContent.getTableContent(url, elementUtils.getTrBgcolor(), elementUtils.getTr()));

            if (!itens.isEmpty()) {
                personageHandler(itens);
                personageAttributesHandler(itens);
                persistPersonage(personage); // É preciso persistir o personagem para certificar que a instância do objeto tem um ID para regras posteriores
                persistFormerName(personage);
                personageObjectsHandler(itens);
            }
        } catch (IOException | ValidationException ex) {
            Logger.getLogger(CharacterService2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fetchCharacter(List<String> itens) {
        personageHandler(itens);
        personageAttributesHandler(itens);
        persistPersonage(personage); // É preciso persistir o personagem para certificar que a instância do objeto tem um ID para regras posteriores
        persistFormerName(personage);
        personageObjectsHandler(itens);
    }

    private void persistPersonage(Personage p) {

        if (needsPersistence) {
            System.out.println("\n" + p.getName() + " persistido");
            if (p.getRegisteredDate() == null) {
                p.setRegisteredDate(Calendar.getInstance()); // registra data caso não houver
            }
            pp.save(p);

        } else {
            System.out.println("Personage " + personage.getName() + " não precisa de persistência");
        }
    }

    private void persistFormerName(Personage p) {

        for (FormerName formerName : formerNames) {
            Date date = fnp.findDateOfLastFormerNameRegistered(formerName.getFormerName(), p.getId());

            // chama o bd só se date != null
            boolean greatherThan180days
                    = date != null && CalendarUtils.greaterThan180Days(calendar, CalendarUtils.convertToCalendar(date));

            boolean isFnExists = fnp.isFormerNameFromPersonage(formerName.getFormerName(), p.getId());

            // Se o former name NÃO existe, persistir
            // ou a data do último FN EXISTE associada ao Personage e a data de registro for maior ou igual a 180 dias, persistir.
            if (!isFnExists || (date != null && greatherThan180days)) {
                formerName.setPersonage(personage);
                fnp.save(formerName);

            } else { // Senão o formername EXISTE e está associado ao id do personage, não persistir pois já existe no bd
                System.out.println("FN já existe no bd\n");
            }
        }
    }

    private void personageHandler(List<String> itens) {
        Personage recoveredPersonage = null;
        String name = null;

        for (String item : itens) {

            if (item.contains(NAME)) {
                name = StringUtils.splitAndReplace(item, ITEM).replace(TRADED, ""); // trata o nome do personagem, elima (traded) se o personagem vier do Bazaar
                recoveredPersonage = pp.findByName(name); // recupera personagem se existir

                personage = (recoveredPersonage == null) ? new Personage() : recoveredPersonage; // recupera ou cria novo personagem

                if (recoveredPersonage == null) {
                    needsPersistence = true;
                    personage.setName(name); // set name apenas se for um novo personagem
                }

            } else if (item.contains(FORMERNAMES)) {
                existsName = recoveredPersonage != null; // se for falso, o personagem existe mas trocou de nome
                formerNameValidator(existsName, item, name);
            }
        }
    }

    private void personageAttributesHandler(List<String> itens) {

        for (String item : itens) { //16
            for (var entry : attributesStrategyMap.entrySet()) { //x7, = 112 verificações
                if (item.contains(entry.getKey()) || item.matches(entry.getKey())) {
                    entry.getValue().apply(personage, item, needsPersistence);
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

    private void formerNameValidator(boolean existsName, String formerName, String name) {
        String[] splittedFormerNames = StringUtils.multSplit(formerName, "[:,]");

        // Se não existe é novo ou trocou de nick
        if (!existsName) {
            for (int i = ITEM; i < splittedFormerNames.length; i++) {
                String currentFormerName = StringUtils.replaceFirstSpace(splittedFormerNames[i]);

                // pelo menos um former name existe na coluna de names? Personagem existe mas nome foi trocado
                if (pp.existsByName(currentFormerName)) {
                    oldName = currentFormerName; // Guarda antigo nome para validações de atributos posteriores
                    personage = pp.findByName(currentFormerName); // Recupera o personagem existente com o antigo nome
                    personage.setName(name); // Seta novo nome
                }
                formerNames.add(new FormerName(currentFormerName, Calendar.getInstance())); // add novo fn
            }
        }
    }
}
