package com.tibiadata.tibia_crawler.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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

    @Column(nullable = false)
    private String formerName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false) // FK será associada ao "id" de Personage
    private Personage personage;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar untilDateOf;

    /**
     * Hibernate needs a default constructor
     */
    public FormerName() {
    }

    public FormerName(String formerName, Calendar registratedDate) {
        this.formerName = formerName;
        this.untilDateOf = registratedDate;
    }

    public FormerName(String formerName, Personage personage, Calendar registratedDate) {
        this.formerName = formerName;
        this.personage = personage;
        this.untilDateOf = registratedDate;
    }

    public String getFormerName() {
        return formerName;
    }

    public void setFormerName(String formerName) {
        this.formerName = formerName;
    }

    public Calendar getUntilDateOf() {
        return untilDateOf;
    }

    public void setUntilDateOf(Calendar untilDateOf) {
        this.untilDateOf = untilDateOf;
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
