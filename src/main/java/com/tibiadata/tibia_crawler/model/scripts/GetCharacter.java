package com.tibiadata.tibia_crawler.model.scripts;

import com.tibiadata.tibia_crawler.model.connections.GetContent;
import com.tibiadata.tibia_crawler.model.entities.FormerName;
import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.entities.Sex;
import com.tibiadata.tibia_crawler.model.persistence.FormerNamePersistence;
import com.tibiadata.tibia_crawler.model.persistence.PersonagePersistence;
import com.tibiadata.tibia_crawler.model.persistence.SexPersistence;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tibiadata.tibia_crawler.model.utils.ElementsUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Devmachine
 */
@Service
public class GetCharacter {

    private static final String NAME = "Name:";
    private static final String FORMERNAMES = "Former Names:";
    private static final String TITLE = ".*[0-9]+ titles unlocked.*";
    private static final String SEX = "Sex:";

    private static final int ITEM = 1;

    boolean existsName = false;
    boolean needsPersistence = false;

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

    public GetCharacter() {
        this.calendar = Calendar.getInstance();
        this.calendar.set(Calendar.MILLISECOND, 0); // eliminar pontos flutuantes de MS ao persistir datas
        this.getContent = new GetContent();
        this.elementUtils = new ElementsUtils();
    }

    public void getCharacter(String url) {
        try {
            List<String> itens = getContent.getTableContent(url, elementUtils.getTrBgcolor(), elementUtils.getTr());
            if (!itens.isEmpty()) {
                flowScript(getContent.getTableContent(url, elementUtils.getTrBgcolor(), elementUtils.getTr()));
                persistPersonage(personage); // É preciso persistir o personagem para certificar que a instância do objeto tem um ID
                persistFormerName(personage);
                persistSex(personage);
            }
        } catch (IOException ex) {
            Logger.getLogger(GetCharacter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getCharacter(List<String> itens) {
        flowScript(itens);
        persistPersonage(personage);
        persistFormerName(personage);
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

            System.out.print("\n" + fnp.isFormerNameFromPersonage(formerName.getFormerName(), p.getId())
                    + " ID: " + p.getId() + " " + formerName.getFormerName());

            // Se o formername existe e está associado ao id do personage, não persistir pois já existe no bd !!!!!!!!!!!!!!
            // Nova lógica: Se o FN existe e está associado ao id do personage e tem a mesma data de registro, não persistir. Caso contrário, persistir !!!!
            if (!fnp.isFormerNameFromPersonage(formerName.getFormerName(), p.getId())) {
                formerName.setPersonage(personage);
                fnp.save(formerName);

            } else {
                System.out.println(" FN já existe no bd\n");
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
        String name = null;

        for (String item : itens) {

            if (item.contains(NAME)) {
                name = splitAndReplace(item, ":")[ITEM].replace(" ", ""); // trata o nome do personagem
                personage = this.recoverPersonage(name); // recupera personagem se existir

                if (this.personage == null) {
                    needsPersistence = true;
                    personage = new Personage(); // Instancia um novo objeto caso realmente for um personagem novo
                    personage.setName(name); // set name
                }

            } else if (item.contains(FORMERNAMES)) {
                // se for falso, então é preciso checar os former names
                existsName = personageValidator(name);
                formerNameValidator(existsName, item, name);

            } else if (item.matches(TITLE)) {
                String title = replaceFirstSpace(splitAndReplace(item, ":")[ITEM]);
                needsPersistence = titleValidator(title);

            } else if (item.contains(SEX)) {
                String genre = replaceFirstSpace(splitAndReplace(item, ":")[ITEM]);
                sexValidator(genre);
            }
        }
    }

    /**
     *
     * @param name Personage nickname
     * @return true if name exists in database or false if not exists
     */
    private boolean personageValidator(String name) {
        return pp.existsByName(name); // se existir, retorna verdadeiro
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
                // pelo menos um former name existe na coluna de names? Name foi trocado
                if (pp.existsByName(currentFormerName)) {
                    personage = recoverPersonage(currentFormerName); // Puxa apenas a tabela principal Personage
                    personage.setName(name); // Char existe mas name foi trocado
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

    private void sexValidator(String genre) {
        Sex dbSex = sr.findLastSex(personage.getName());

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
