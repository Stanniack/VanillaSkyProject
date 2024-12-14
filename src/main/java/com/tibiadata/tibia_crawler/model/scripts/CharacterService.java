package com.tibiadata.tibia_crawler.model.scripts;

import com.tibiadata.tibia_crawler.model.connections.GetContent;
import com.tibiadata.tibia_crawler.model.entities.Achievements;
import com.tibiadata.tibia_crawler.model.entities.FormerName;
import com.tibiadata.tibia_crawler.model.entities.LevelProgress;
import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.entities.Sex;
import com.tibiadata.tibia_crawler.model.entities.World;
import com.tibiadata.tibia_crawler.model.persistence.AchievementsPersistence;
import com.tibiadata.tibia_crawler.model.persistence.FormerNamePersistence;
import com.tibiadata.tibia_crawler.model.persistence.LevelProgressPersistence;
import com.tibiadata.tibia_crawler.model.persistence.PersonagePersistence;
import com.tibiadata.tibia_crawler.model.persistence.SexPersistence;
import com.tibiadata.tibia_crawler.model.persistence.WorldPersistence;
import com.tibiadata.tibia_crawler.model.utils.CalendarUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tibiadata.tibia_crawler.model.utils.ElementsUtils;
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

    private static final String NAME = "Name:";
    private static final String FORMERNAMES = "Former Names:";
    private static final String TITLE = ".*[0-9]+ titles unlocked.*";
    private static final String SEX = "Sex:";
    private static final String VOCATION = "Vocation:";
    private static final String LEVEL = "Level:";
    private static final String ACHIEVEMENTS = "Achievement Points:";
    private static final String WORLD = "World:";

    private static final int ITEM = 1;

    private boolean existsName = false;
    private boolean needsPersistence = false;
    private String oldName;

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

    private Calendar calendar;

    private Personage personage;
    private List<FormerName> formerNames = new ArrayList<>();
    private Sex sex = null;
    private LevelProgress levelProgress = null;
    private Achievements achievements = null;
    private World world = null;

    private GetContent getContent;
    private ElementsUtils elementUtils;
    private CalendarUtils calendarUtils;

    public CharacterService() {
        this.calendar = Calendar.getInstance();
        this.calendar.set(Calendar.MILLISECOND, 0); // eliminar pontos flutuantes de MS ao persistir datas
        this.getContent = new GetContent();
        this.elementUtils = new ElementsUtils();
        this.calendarUtils = new CalendarUtils();
    }

    public void fetchCharacter(String url) {
        try {
            List<String> itens = getContent.getTableContent(url, elementUtils.getTrBgcolor(), elementUtils.getTr());
            if (!itens.isEmpty()) {
                flowScript(getContent.getTableContent(url, elementUtils.getTrBgcolor(), elementUtils.getTr()));
                handlerPersister();
            }
        } catch (IOException | ValidationException ex) {
            Logger.getLogger(CharacterService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fetchCharacter(List<String> itens) {
        flowScript(itens);
        handlerPersister();
    }

    private void handlerPersister() {
        persistPersonage(personage); // É preciso persistir o personagem para certificar que a instância do objeto tem um ID
        persistFormerName2(personage);
        persistObject(sex, _sex -> _sex.setPersonage(personage), _sex -> sp.save(_sex));
        persistObject(levelProgress, lp -> lp.setPersonage(personage), lp -> lpp.save(lp));
        persistObject(achievements, achiev -> achiev.setPersonage(personage), achiev -> ap.save(achiev));
        persistObject(world, worldServer -> worldServer.setPersonage(personage), worldServer -> wp.save(worldServer));
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
            // Se o formername existe e está associado ao id do personage, não persistir pois já existe no bd !!!!!!!!!!!!!!
            if (!fnp.isFormerNameFromPersonage(formerName.getFormerName(), p.getId())) {
                formerName.setPersonage(personage);
                fnp.save(formerName);

            } else { // Senão o formername EXISTE e está associado ao id do personage, não persistir pois já existe no bd
                System.out.println(" FN já existe no bd\n");
            }
        }

    }

    private void persistFormerName2(Personage p) {

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

    private <T> void persistObject(T object, Consumer<T> setPersonage, Consumer<T> dbPersister) {
        // Se for diferente de nulo, então foi criado ou alterado
        if (object != null) {
            setPersonage.accept(object); //object.setPersonage(personage)
            dbPersister.accept(object); // persistence.save(object)
        }
    }

    /**
     *
     * @param itens A list containing all itens scrapped to be treated
     */
    private void flowScript(List<String> itens) {
        Personage recoveredPersonage = null;
        String name = null;

        for (String item : itens) {

            // Criar método auxiliar para reordenar a lista de strings
            // TODO NAME AND FORMER NAMES
            // TODO FLOW ATTRIBUTES
            // TODO FLOW OBJECTS
            if (item.contains(NAME)) {
                name = splitAndReplace(item, ":")[ITEM].replace(" ", ""); // trata o nome do personagem
                recoveredPersonage = pp.findByName(name); // recupera personagem se existir

                if (recoveredPersonage == null) {
                    needsPersistence = true;
                    personage = new Personage(); // Instancia um novo objeto caso realmente for um personagem novo
                    personage.setName(name); // set name
                } else {
                    personage = recoveredPersonage; // personagem existe no bd
                }

            } else if (item.contains(FORMERNAMES)) {
                existsName = recoveredPersonage != null; // se for falso, o personagem existe mas trocou de nome
                formerNameValidator(existsName, item, name);

            } else if (item.matches(TITLE)) {
                String title = replaceFirstSpace(splitAndReplace(item, ":")[ITEM]);
                persistenceValidator(personage::getTitle, Personage::setTitle, title);

            } else if (item.contains(SEX)) {
                String genre = replaceFirstSpace(splitAndReplace(item, ":")[ITEM]);

                objectValidator(
                        genre,
                        param -> sp.findLastSex(param),
                        (value, date) -> new Sex(value, date),
                        newSex -> this.sex = newSex);

            } else if (item.contains(VOCATION)) {
                String vocation = replaceFirstSpace(splitAndReplace(item, ":")[ITEM]);
                persistenceValidator(personage::getVocation, Personage::setVocation, vocation);

            } else if (item.contains(LEVEL)) {
                String level = replaceFirstSpace(splitAndReplace(item, ":")[ITEM]);

                objectValidator(
                        level,
                        param -> lpp.findLastLevelProgress(param),
                        (value, date) -> new LevelProgress(value, date),
                        newLevelProgress -> this.levelProgress = newLevelProgress);

            } else if (item.contains(ACHIEVEMENTS)) {
                String points = replaceFirstSpace(splitAndReplace(item, ":")[ITEM]);
                objectValidator(
                        points,
                        param -> ap.findLastPoints(param),
                        (value, date) -> new Achievements(value, date),
                        newAchievements -> this.achievements = newAchievements);

            } else if (item.contains(WORLD)) {
                String server = replaceFirstSpace(splitAndReplace(item, ":")[ITEM]);
                objectValidator(
                        server,
                        param -> wp.findLastWorld(param),
                        (value, date) -> new World(value, date),
                        newWorld -> this.world = newWorld);
            }

        }
    }

    private void formerNameValidator(boolean existsName, String formerName, String name) {
        String[] splittedFormerNames = splitAndReplace(formerName, "[:,]");

        // Se não existe é novo ou trocou de nick
        if (!existsName) {
            for (int i = ITEM; i < splittedFormerNames.length; i++) {
                String currentFormerName = replaceFirstSpace(splittedFormerNames[i]);

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

    private <T> void objectValidator(String newValue, Function<String, String> dbPersister, BiFunction<String, Calendar, T> object, Consumer<T> setter) {
        // Se oldName existir, então verifica o último valor do antigo name
        String dbValue = (oldName != null) ? dbPersister.apply(oldName) : dbPersister.apply(personage.getName());

        // Se valor buscado no db é null ou não é igual o valor atual, setar valor atual para persistência
        if (dbValue == null || !dbValue.equals(newValue)) {
            T newObject = object.apply(newValue, Calendar.getInstance());
            setter.accept(newObject);
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

    /**
     *
     * @param item var type string to be processed by split and replace
     * @param regex regex to use for process string
     * @return an array of processed strings
     */
    private String[] splitAndReplace(String item, String regex) {
        return item.split(regex);
    }

    /**
     *
     * @param str string to replace first index
     * @return string treated without first index containing space
     */
    private String replaceFirstSpace(String str) {
        return str.replaceFirst("^\\s+", "");
    }

}
