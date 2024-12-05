package com.tibiadata.tibia_crawler.model.scripts;

import com.tibiadata.tibia_crawler.model.connections.GetContent;
import com.tibiadata.tibia_crawler.model.entities.FormerName;
import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.entities.Sex;
import com.tibiadata.tibia_crawler.model.persistence.FormerNamePersistence;
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

    private static final int ITEM = 1;

    private boolean existsName = false;
    private boolean needsPersistence = false;
    private String oldName;

    @Autowired
    private PersonagePersistence pp;
    @Autowired
    private FormerNamePersistence fnp;
    @Autowired
    private SexPersistence sr;

    private Calendar calendar;

    private Personage personage;
    private List<FormerName> formerNames = new ArrayList<>();
    private Sex sex = null;

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

            boolean greatherThan180days
                    = date != null
                            ? calendarUtils
                                    .isDifferenceGreaterThan180Days(calendarUtils.convertToCalendar(date), calendar) : false; // chama o bd só se date != null
            System.out.println("greatherThan180days? " + greatherThan180days);

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
            sr.save(sex);
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

                // !!!!!!
                if (!needsPersistence) {
                    needsPersistence = titleValidator(title);
                } else {
                    titleValidator(title);
                }

            } else if (item.contains(SEX)) {
                String genre = replaceFirstSpace(splitAndReplace(item, ":")[ITEM]);
                sexValidator(genre);
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
     * if Personage's title is null or changed, set the new title
     *
     * @param title current Personage's title
     * @return true if needs persistence
     */
    private boolean titleValidator(String title) {
        if (personage.getTitle() == null || !personage.getTitle().equals(title)) {
            personage.setTitle(title);
            return true;
        }
        return false;
    }

    /**
     * @param genre Personage' genre
     */
    private void sexValidator(String genre) {
        // Se oldName existir, então verifica o último gênero do antigo name
        Sex dbSex = (oldName != null) ? sr.findLastSex(oldName) : sr.findLastSex(personage.getName());

        // Se sex não existir ou sexo é !diferente, então trocou o sexo do personagem
        if (dbSex == null || !dbSex.getGenre().equals(genre)) {
            this.sex = new Sex(genre, Calendar.getInstance());
        }
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
}
