package com.tibiadata.tibia_crawler.model.scripts;

import com.tibiadata.tibia_crawler.model.connections.GetContent;
import com.tibiadata.tibia_crawler.model.entities.FormerName;
import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.persistence.FormerNamePersistence;
import com.tibiadata.tibia_crawler.model.persistence.PersonagePersistence;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tibiadata.tibia_crawler.model.utils.ElementsUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

    private static final int TITLE = 0;
    private static final int ITEM = 1;

    boolean existsName = false;
    boolean needsPersistence = false;

    @Autowired
    private PersonagePersistence pp;
    @Autowired
    private FormerNamePersistence fnp;

    Calendar calendar;

    private Personage personage;
    private final List<FormerName> formerNames = new ArrayList<>();

    private final GetContent getContent = new GetContent();
    private final ElementsUtils elementUtils = new ElementsUtils();

    public GetCharacter() {
        this.calendar = Calendar.getInstance();
        this.calendar.set(Calendar.MILLISECOND, 0); // eliminar pontos flutuantes de MS ao persistir datas
    }

    public void getCharacter(String url) {
        try {
            List<String> itens = getContent.getTableContent(url, elementUtils.getTrBgcolor(), elementUtils.getTr());
            if (!itens.isEmpty()) {
                this.flowScript(getContent.getTableContent(url, elementUtils.getTrBgcolor(), elementUtils.getTr()));
                this.persistPersonage(personage);
                this.persistFormerName(personage);
            }
        } catch (IOException ex) {
            Logger.getLogger(GetCharacter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getCharacter(List<String> itens) {
        this.flowScript(itens);
        this.persistPersonage(personage);
        this.persistFormerName(personage);
    }

    private void persistPersonage(Personage p) {
        if (needsPersistence) {
            System.out.println("\n" + p.getName() + " persistido");
            if (p.getRegisteredDate() == null) {
                p.setRegisteredDate(Calendar.getInstance()); // registra data caso não houver
            }
            this.pp.save(p);

        } else {
            System.out.println("Personage " + this.personage.getName() + " não precisa de persistência");
        }
    }

    private void persistFormerName(Personage p) {
        for (FormerName formerName : this.formerNames) {

            System.out.print("\n" + this.fnp.isFormerNameFromPersonage(formerName.getFormerName(), p.getId())
                    + " ID: " + p.getId() + " " + formerName.getFormerName());

            // Se o formername existe e está associado ao id do personage, não persistir pois já existe no bd !!!!!!!!!!!!!!
            if (!this.fnp.isFormerNameFromPersonage(formerName.getFormerName(), p.getId())) {
                formerName.setPersonage(this.personage);
                this.fnp.save(formerName);

            } else {
                System.out.println(" FN já existe no bd\n");
            }
        }
    }

    private void formerNameValidator(boolean existsName, String formerName) {
        String[] splittedFormerNames = this.splitAndReplace(formerName, "[:,]");

        // Se não existe é novo ou trocou de nick
        if (!existsName) {
            for (int i = ITEM; i < splittedFormerNames.length; i++) {
                // pelo menos um former name existe na coluna de names? Name foi trocado
                String currentFormerName = splittedFormerNames[i].replaceFirst("^\\s+", "");

                if (pp.existsByName(currentFormerName)) {
                    this.personage.setId(this.pp.findIdByName(currentFormerName)); // add id já existente do personagem
                    this.personage.setRegisteredDate(this.pp.findRegisteredDateByName(currentFormerName)); // puxa data de registro caso o personagem já existir
                }
                this.formerNames.add(new FormerName(currentFormerName, Calendar.getInstance())); // add novo fn
            }
        }
    }

    private void flowScript(List<String> itens) {
        String name = null;

        for (String item : itens) {

            if (item.contains(NAME)) {
                name = this.splitAndReplace(item, ":")[ITEM].replace(" ", ""); // trata o nome do personagem
                this.personage = this.recoverPersonage(name); // recupera personagem se existir

                if (this.personage == null) {
                    this.needsPersistence = true;
                    this.personage = new Personage();
                    this.personage.setName(name); // set name
                }

            } else if (item.contains(FORMERNAMES)) {
                // se for falso, então é preciso checar os former names
                this.existsName = this.personageValidator(name);
                this.formerNameValidator(existsName, item);
            }
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
     * @param name personage nickname
     * @return true if name exists in database or false if not exists
     */
    private boolean personageValidator(String name) {
        return this.pp.existsByName(name); // se existir, retorna verdadeiro
    }

    private Personage recoverPersonage(String name) {
        return this.pp.findByName(name);
    }
}
