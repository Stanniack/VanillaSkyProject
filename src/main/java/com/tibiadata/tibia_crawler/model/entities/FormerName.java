package com.tibiadata.tibia_crawler.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Calendar;

/**
 *
 * @author Devmachine
 */
@Entity
public class FormerName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String formerName;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false) // FK será associada ao "id" de Personage
    private Personage personage;

    @Column(nullable = false)
    private Calendar registratedDate;

    public FormerName(String formerName, Calendar registratedDate) {
        this.formerName = formerName;
        this.registratedDate = registratedDate;
    }

    public String getFormerName() {
        return formerName;
    }

    public void setFormerName(String formerName) {
        this.formerName = formerName;
    }

    public Calendar getRegistratedDate() {
        return registratedDate;
    }

    public void setRegistratedDate(Calendar registratedDate) {
        this.registratedDate = registratedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Personage getPersonage() {
        return personage;
    }

    public void setPersonage(Personage personage) {
        this.personage = personage;
    }

    @Override
    public String toString() {
        return "FormerName{" + "formerName=" + formerName + ", personage=" + personage + '}';
    }

}
