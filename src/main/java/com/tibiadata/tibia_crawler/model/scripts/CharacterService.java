package com.tibiadata.tibia_crawler.model.scripts;

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
import com.tibiadata.tibia_crawler.model.exceptions.StringLengthException;
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
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
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
    private int STRLENTOLERANCE = 500;

    //
    private static final String NAME = "Name:";
    private static final String FORMERNAMES = "Former Names:";
    private static final String TITLE = ".*[0-9]+ titles unlocked.*";
    private static final String SEX = "Sex:";
    private static final String VOCATION = "Vocation:";
    private static final String LEVEL = "Level:";
    private static final String ACHIEVEMENTS = "Achievement Points:";
    private static final String WORLD = "World:";
    private static final String RESIDENCE = "Residence:";
    private static final String LASTLOGIN = "Last Login:";
    private static final String ACCSTATUS = "Account Status:";
    private static final String LOYALTYTITLE = "Loyalty Title:";
    private static final String CREATED = "Created:";
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
    @Autowired
    private SexPersistence sp;
    @Autowired
    private LevelProgressPersistence lpp;
    @Autowired
    private AchievementsPersistence ap;
    @Autowired
    private WorldPersistence wp;
    @Autowired
    private GuildPersistence gp;
    @Autowired
    private HousePersistence hp;
    @Autowired
    private DeathPersistence dp;

    //
    private Personage personage;
    private List<FormerName> formerNames;
    private Sex sex = null;
    private LevelProgress levelProgress = null;
    private Achievements achievements = null;
    private World world = null;
    private Guild guild = null;
    private List<House> houses;
    private List<House> cacheHouses;
    private List<Death> deaths;

    //
    private Calendar calendar;
    private GetContent getContent;
    private ElementsUtils elementUtils;
    private CalendarUtils calendarUtils;
    private StringUtils strUtils;
    private PriorityHandler pHandler;

    public CharacterService2() {
        this.formerNames = new ArrayList<>();
        this.houses = new ArrayList();
        this.deaths = new ArrayList<>();

        this.calendar = Calendar.getInstance();
        this.calendar.set(Calendar.MILLISECOND, 0); // eliminar pontos flutuantes de MS ao persistir datas
        this.getContent = new GetContent();
        this.elementUtils = new ElementsUtils();
        this.calendarUtils = new CalendarUtils();
        this.strUtils = new StringUtils();
        this.pHandler = new PriorityHandler();
    }

    public void fetchCharacter(String url) {

        try {
            List<String> itens = pHandler.reorderList(getContent.getTableContent(url, elementUtils.getTrBgcolor(), elementUtils.getTr()));

            if (!itens.isEmpty()) {
                personageHandler(itens);
                personageAttributesHandler(itens);
                personageObjectsHandler(itens);
                persistHandler();
            }
        } catch (IOException | ValidationException ex) {
            Logger.getLogger(CharacterService2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fetchCharacter(List<String> itens) {
        personageHandler(itens);
        personageAttributesHandler(itens);
        personageObjectsHandler(itens);
        persistHandler();
    }

    private void persistHandler() {
        persistPersonage(personage); // É preciso persistir o personagem para certificar que a instância do objeto tem um ID
        persistFormerName(personage);
        persistObject(sex, _sex -> _sex.setPersonage(personage), _sex -> sp.save(_sex));
        persistObject(levelProgress, lp -> lp.setPersonage(personage), lp -> lpp.save(lp));
        persistObject(achievements, achiev -> achiev.setPersonage(personage), achiev -> ap.save(achiev));
        persistObject(world, worldServer -> worldServer.setPersonage(personage), worldServer -> wp.save(worldServer));
        persistObject(guild, currentGuild -> currentGuild.setPersonage(personage), currentGuild -> gp.save(currentGuild));
        persistHouses(personage);
        persistDeaths(personage);
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
                    = (date != null) ? calendarUtils.greaterThan180Days(calendar, calendarUtils.convertToCalendar(date)) : false;

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

    private void persistHouses(Personage p) {
        houses.stream().forEach(house -> {
            house.setPersonage(p);
            hp.save(house);
        });
    }

    private void persistDeaths(Personage p) {
        deaths.stream().forEach(death -> {
            death.setPersonage(p);
            dp.save(death);
        });
    }

    private <T> void persistObject(T object, Consumer<T> setPersonage, Consumer<T> dbPersister) {
        // Se for diferente de nulo, então foi criado ou alterado
        if (object != null) {
            setPersonage.accept(object); //object.setPersonage(personage)
            dbPersister.accept(object); // persistence.save(object)
        }
    }

    private void personageHandler(List<String> itens) {
        Personage recoveredPersonage = null;
        String name = null;

        for (String item : itens) {

            if (item.contains(NAME)) {
                name = strUtils.splitAndReplace(item, ITEM).replace(TRADED, ""); // trata o nome do personagem, elima (traded) se o personagem vier do Bazaar
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
        for (String item : itens) {

            if (item.matches(TITLE)) {
                String title = strUtils.splitAndReplace(item, ITEM);
                persistenceValidator(personage::getTitle, Personage::setTitle, title);

            } else if (item.contains(VOCATION)) {
                String vocation = strUtils.splitAndReplace(item, ITEM);
                persistenceValidator(personage::getVocation, Personage::setVocation, vocation);

            } else if (item.contains(RESIDENCE)) {
                String residence = strUtils.splitAndReplace(item, ITEM);
                persistenceValidator(personage::getResidence, Personage::setResidence, residence);

            } else if (item.contains(LASTLOGIN)) {
                String lastLogin = strUtils.splitAndReplace(item, ITEM);
                persistenceValidator(personage::getLastLogin, Personage::setLastLogin, lastLogin);

            } else if (item.contains(ACCSTATUS)) {
                String accStatus = strUtils.splitAndReplace(item, ITEM);
                persistenceValidator(personage::getAccStatus, Personage::setAccStatus, accStatus);

            } else if (item.contains(LOYALTYTITLE)) {
                String loyaltyTitle = strUtils.splitAndReplace(item, ITEM);
                persistenceValidator(personage::getLoyaltyTitle, Personage::setLoyaltyTitle, loyaltyTitle);

            } else if (item.contains(CREATED)) {
                String created = strUtils.splitAndReplace(item, ITEM);
                persistenceValidator(personage::getCreated, Personage::setCreated, created);
            }
        }
    }

    private void personageObjectsHandler(List<String> itens) {
        for (String item : itens) {

            if (item.contains(SEX)) {
                String genre = strUtils.splitAndReplace(item, ITEM);

                genericValidator(
                        genre,
                        param -> sp.findLastSex(param),
                        (value, date) -> new Sex(value, date),
                        newSex -> this.sex = newSex);

            } else if (item.contains(LEVEL)) {
                String level = strUtils.splitAndReplace(item, ITEM);

                genericValidator(
                        level,
                        param -> lpp.findLastLevelProgress(param),
                        (value, date) -> new LevelProgress(value, date),
                        newLevelProgress -> this.levelProgress = newLevelProgress);

            } else if (item.contains(ACHIEVEMENTS)) {
                String points = strUtils.splitAndReplace(item, ITEM);
                genericValidator(
                        points,
                        param -> ap.findLastPoints(param),
                        (value, date) -> new Achievements(value, date),
                        newAchievements -> this.achievements = newAchievements);

            } else if (item.contains(WORLD)) {
                String server = strUtils.splitAndReplace(item, ITEM);
                genericValidator(
                        server,
                        param -> wp.findLastWorld(param),
                        (value, date) -> new World(value, date),
                        newWorld -> this.world = newWorld);

            } else if (item.contains(GUILD)) {
                String currentGuild = strUtils.splitAndReplace(item, ITEM);
                String currentRank = strUtils.splitAndReplace(currentGuild, "of the", 2, 0);
                String currentGuildName = strUtils.splitAndReplace(currentGuild, "of the", 2, 1);
                guildValidator(currentRank, currentGuildName);

            } else if (item.contains(HOUSE)) {
                String houseName = strUtils.splitAndReplace(item, ":|\\s?is paid until\\s?", 3, 1);
                String paidUntil = strUtils.splitAndReplace(item, ":|\\s?is paid until\\s?", 3, 2);
                houseValidator(houseName, paidUntil);

            } else if (item.matches(DEATH)) {
                try {
                    String[] occurrence = strUtils.split(item, "\\s?(CET|CEST)\\s?");
                    deathValidator(occurrence[0], occurrence[1]);
                } catch (StringLengthException ex) {
                    Logger.getLogger(CharacterService2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void formerNameValidator(boolean existsName, String formerName, String name) {
        String[] splittedFormerNames = strUtils.multSplit(formerName, "[:,]");

        // Se não existe é novo ou trocou de nick
        if (!existsName) {
            for (int i = ITEM; i < splittedFormerNames.length; i++) {
                String currentFormerName = strUtils.replaceFirstSpace(splittedFormerNames[i]);

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

    private void guildValidator(String rank, String guildName) {
        Guild dbGuild = (oldName != null) ? gp.findLastGuild(oldName) : gp.findLastGuild(personage.getName());

        // Se último guildName existe no bd e é igual ao guildName atual
        if (dbGuild != null && dbGuild.getGuildName().equals(guildName)) {
            // Se o rank não é igual ao do bd, alterar rank e persistir
            if (!dbGuild.getRank().equals(rank)) {
                dbGuild.setRank(rank);
                this.guild = dbGuild; // atribui guild com valores alterados para persistir
            }

        } else { // Senão: guildName não existe, instanciar guild
            this.guild = new Guild(rank, guildName, Calendar.getInstance());
        }
    }

    private void houseValidator(String curHouseName, String curPaidUntil) {

        // Armezena a verificação do banco de dados para evitar chamadas repetidas inúteis, uma vez que o valor no db não se altera dinamicamente enquanto a classe estiver sendo instanciada
        if (cacheHouses == null) {
            cacheHouses = (oldName != null) ? hp.findLastThreeHouses(oldName) : hp.findLastThreeHouses(personage.getName());
        }

        if (cacheHouses == null) {// Se dbHouse for nulo, persiste as houses que encontrar
            houses.add(new House(curHouseName, curPaidUntil, Calendar.getInstance()));

        } else {
            boolean found = false;
            House curHouse = null;

            for (House house : cacheHouses) {
                if (house.getHouseName().equals(curHouseName)) { // Se encontrar a house na lista do bd, flag para persistência
                    found = true;
                    curHouse = house;
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

    private void deathValidator(String deathDate, String occurrence) throws StringLengthException {

        if (occurrence.length() <= STRLENTOLERANCE) {
            Calendar convertedDeathDate = calendarUtils.parseToCalendar(deathDate);
            Date dbDate = (oldName != null) ? dp.findDeathByDate(convertedDeathDate, oldName) : dp.findDeathByDate(convertedDeathDate, personage.getName());

            //Se a data da morte (horário incluso) buscada for nula (não existe!) a morte é nova, persistir
            if (dbDate == null) {
                deaths.add(new Death(occurrence, convertedDeathDate));
            }

        } else {
            throw new StringLengthException("The string death occurrence is too long.");
        }
    }

    private <T> void genericValidator(String newValue, Function<String, String> dbPersister, BiFunction<String, Calendar, T> object, Consumer<T> setter) {
        // Se oldName existir, então verifica o último valor do antigo name
        String dbValue = (oldName != null) ? dbPersister.apply(oldName) : dbPersister.apply(personage.getName());

        // Se valor buscado no db é null ou não é igual o valor atual, setar valor atual para persistência
        if (dbValue == null || !dbValue.equals(newValue)) {
            T newObject = object.apply(newValue, Calendar.getInstance()); // instancia novo objeto
            setter.accept(newObject); // altera valores
        }
    }

    /**
     * Validates and updates a string attribute if it is null or different from
     * a new value.
     *
     * @param getter a Supplier that retrieves the current value of the
     * attribute.
     * @param setter a BiConsumer that sets the new value for the attribute.
     * @param newValue the new value to be validated and potentially set.
     * @return {@code true} if the attribute was updated; {@code false}
     * otherwise.
     */
    private boolean attributeValidator(Supplier<String> getter, BiConsumer<Personage, String> setter, String newValue) {
        String currentValue = getter.get(); // Obtém o valor atual do atributo usando o getter

        // Verifica se o valor atual é nulo ou diferente do novo valor
        if (currentValue == null || !currentValue.equals(newValue)) {
            setter.accept(personage, newValue); // Atualiza o atributo usando o setter
            return true;
        }
        return false;
    }

    /**
     * Validates and updates an attribute, handling the needsPersistence logic.
     *
     * @param needsPersistence the current state of the persistence flag.
     * @param getter a Supplier to retrieve the current attribute value.
     * @param setter a BiConsumer to set the new attribute value.
     * @param newValue the new value to validate and potentially set.
     */
    private void persistenceValidator(Supplier<String> getter, BiConsumer<Personage, String> setter, String newValue) {
        if (!needsPersistence) {
            needsPersistence = attributeValidator(getter, setter, newValue); //verifica se é true ou false
        } else {
            attributeValidator(getter, setter, newValue); //needsPersistence permanece true
        }
    }

}
