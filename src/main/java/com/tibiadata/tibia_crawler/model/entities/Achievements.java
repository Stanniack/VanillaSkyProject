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
public class Achievements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false) // FK será associada ao "id" de Personage
    private Personage personage;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dayProgress;

    /**
     * Hibernate needs a default constructor
     */
    public Achievements() {
    }

    public Achievements(String points, Calendar dayProgress) {
        this.points = points;
        this.dayProgress = dayProgress;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public Personage getPersonage() {
        return personage;
    }

    public void setPersonage(Personage personage) {
        this.personage = personage;
    }

    public Calendar getDayProgress() {
        return dayProgress;
    }

    public void setDayProgress(Calendar dayProgress) {
        this.dayProgress = dayProgress;
    }

}
