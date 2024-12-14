package com.tibiadata.tibia_crawler.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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

    @OneToMany(mappedBy = "personage", fetch = FetchType.LAZY)
    private List<FormerName> formerNames = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "personage", fetch = FetchType.LAZY)
    private List<FormerName> sexes = new ArrayList<>();

    @Column(nullable = false)
    private String vocation;

    @OneToMany(mappedBy = "personage", fetch = FetchType.LAZY)
    private List<LevelProgress> levelProgresses = new ArrayList<>();

    @OneToMany(mappedBy = "personage", fetch = FetchType.LAZY)
    private List<Achievements> achievements = new ArrayList<>();

    /**/
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar registeredDate;

    /**
     * Hibernate needs a default constructor
     */
    public Personage() {
    }

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

    public Calendar getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Calendar registeredDate) {
        this.registeredDate = registeredDate;
    }

    public List<FormerName> getFormerNames() {
        return formerNames;
    }

    public void setFormerNames(List<FormerName> formerNames) {
        this.formerNames = formerNames;
    }

    @Override
    public String toString() {
        return "Personage{" + "name=" + name + ", registratedDate=" + registeredDate + ", formerNames=" + formerNames + '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FormerName> getSexes() {
        return sexes;
    }

    public void setSexes(List<FormerName> sexes) {
        this.sexes = sexes;
    }

    public String getVocation() {
        return vocation;
    }

    public void setVocation(String vocation) {
        this.vocation = vocation;
    }

    public List<LevelProgress> getLevelProgresses() {
        return levelProgresses;
    }

    public void setLevelProgresses(List<LevelProgress> levelProgresses) {
        this.levelProgresses = levelProgresses;
    }

    public List<Achievements> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievements> achievements) {
        this.achievements = achievements;
    }

}
