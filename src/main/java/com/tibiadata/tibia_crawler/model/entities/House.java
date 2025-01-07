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
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String houseName;

    @Column(nullable = false)
    private String paidUntil;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar registeredDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false) // FK será associada ao "id" de Personage
    private Personage personage;

    public House() {
    }

    public House(String houseName, String paidUntil, Calendar registeredDate) {
        this.houseName = houseName;
        this.paidUntil = paidUntil;
        this.registeredDate = registeredDate;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getPaidUntil() {
        return paidUntil;
    }

    public void setPaidUntil(String paidUntil) {
        this.paidUntil = paidUntil;
    }

    public Calendar getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Calendar registeredDate) {
        this.registeredDate = registeredDate;
    }

    public Personage getPersonage() {
        return personage;
    }

    public void setPersonage(Personage personage) {
        this.personage = personage;
    }

}
