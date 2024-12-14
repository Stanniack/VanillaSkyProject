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
public class World {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String server;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false) // FK será associada ao "id" de Personage
    private Personage personage;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar changeDate;

    /**
     * Hibernate needs a default constructor
     */
    public World() {
    }

    public World(String server, Calendar changeDate) {
        this.server = server;
        this.changeDate = changeDate;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Personage getPersonage() {
        return personage;
    }

    public void setPersonage(Personage personage) {
        this.personage = personage;
    }

    public Calendar getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Calendar changeDate) {
        this.changeDate = changeDate;
    }

}
