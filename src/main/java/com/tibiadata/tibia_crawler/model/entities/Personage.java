package com.tibiadata.tibia_crawler.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Devmachine
 */
@Entity
public class Personage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Calendar registratedDate;

    @OneToMany(mappedBy = "personage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormerName> formerNames = new ArrayList<>(); // Inicialização da lista

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getRegistratedDate() {
        return registratedDate;
    }

    public void setRegistratedDate(Calendar registratedDate) {
        this.registratedDate = registratedDate;
    }

    public List<FormerName> getFormerNames() {
        return formerNames;
    }

    public void setFormerNames(List<FormerName> formerNames) {
        this.formerNames = formerNames;
    }

    @Override
    public String toString() {
        return "Personage{" + "name=" + name + ", registratedDate=" + registratedDate + ", formerNames=" + formerNames + '}';
    }

}
