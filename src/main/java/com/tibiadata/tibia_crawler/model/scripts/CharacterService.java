package com.tibiadata.tibia_crawler.model.scripts;

import com.tibiadata.tibia_crawler.model.connections.GetContent;
import com.tibiadata.tibia_crawler.model.entities.FormerName;
import com.tibiadata.tibia_crawler.model.entities.LevelProgress;
import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.entities.Sex;
import com.tibiadata.tibia_crawler.model.persistence.FormerNamePersistence;
import com.tibiadata.tibia_crawler.model.persistence.LevelProgressPersistence;
import com.tibiadata.tibia_crawler.model.persistence.PersonagePersistence;
import com.tibiadata.tibia_crawler.model.persistence.SexPersistence;
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
import java.util.function.Supplier;
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

    private static final String NAME = "Name:";
    private static final String FORMERNAMES = "Former Names:";
    private static final String TITLE = ".*[0-9]+ titles unlocked.*";
    private static final String SEX = "Sex:";
    private static final String VOCATION = "Vocation:";
    private static final String LEVEL = "Level:";

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

    private Calendar calendar;

    private Personage personage;
    private List<FormerName> formerNames = new ArrayList<>();
    private Sex sex = null;
    private LevelProgress levelProgress = null;

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
                persistAll();
            }
        } catch (IOException ex) {
            Logger.getLogger(CharacterService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fetchCharacter(List<String> itens) {
        flowScript(itens);
        persistAll();
    }

    private void persistAll() {
        persistPersonage(personage); // É preciso persistir o personagem para certificar que a instância do objeto tem um ID
        persistFormerName2(personage);
        persistSex(personage);
        persistLevelProgress(personage);
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

    private void persistSex(Personage p) {
        // se sex != null então foi criado ou alterado
        if (sex != null) {
            sex.setPersonage(p);
            sp.save(sex);
        }
    }
    
    private void persistLevelProgress(Personage p){
        if (levelProgress != null) {
            levelProgress.setPersonage(p);
            lpp.save(levelProgress);
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

            if (item.contains(NAME)) {
                name = splitAndReplace(item, ":")[ITEM].replace(" ", ""); // trata o nome do personagem
                recoveredPersonage = this.recoverPersonage(name); // recupera personagem se existir

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
                sexValidator(genre);

            } else if (item.contains(VOCATION)) {
                String vocation = replaceFirstSpace(splitAndReplace(item, ":")[ITEM]);
                persistenceValidator(personage::getVocation, Personage::setVocation, vocation);

            } else if (item.contains(LEVEL)) {
                String level = replaceFirstSpace(splitAndReplace(item, ":")[ITEM]);
                levelProgressValidator(level);
            }
        }
    }

    /**
     * JAVADOC TODO
     */
    private void formerNameValidator(boolean existsName, String formerName, String name) {
        String[] splittedFormerNames = splitAndReplace(formerName, "[:,]");

        // Se não existe é novo ou trocou de nick
        if (!existsName) {
            for (int i = ITEM; i < splittedFormerNames.length; i++) {
                String currentFormerName = replaceFirstSpace(splittedFormerNames[i]);

                // pelo menos um former name existe na coluna de names? Personagem existe mas nome foi trocado
                if (pp.existsByName(currentFormerName)) {
                    oldName = currentFormerName; // Guarda antigo nome para validações de atributos posteriores
                    personage = recoverPersonage(currentFormerName); // Puxa o personagem existente com o antigo nome
                    personage.setName(name); // Seta novo nome
                }
                formerNames.add(new FormerName(currentFormerName, Calendar.getInstance())); // add novo fn
            }
        }
    }

    /**
     * @param genre Personage' genre
     */
    private void sexValidator(String genre) {
        // Se oldName existir, então verifica o último gênero do antigo name
        String dbGenre = (oldName != null) ? sp.findLastSex(oldName) : sp.findLastSex(personage.getName());

        // Se sex não existir ou sexo é diferente, então trocou o sexo do personagem
        if (dbGenre == null || !dbGenre.equals(genre)) {
            this.sex = new Sex(genre, Calendar.getInstance());
        }
    }
    
    private void levelProgressValidator(String level){
        String dbLevel = (oldName != null) ? lpp.findLastLevelProgress(oldName) : lpp.findLastLevelProgress(personage.getName());
        
        // Se nível buscado no db é null ou não é igual ao atual nível
        if (dbLevel == null || !dbLevel.equals(level)){
            this.levelProgress = new LevelProgress(level, Calendar.getInstance());
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
     *
     * @param name Personame nickname
     * @return Personage object and its attrs - fetch lazy
     */
    private Personage recoverPersonage(String name) {
        return pp.findByName(name);
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
