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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Devmachine
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Personage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String vocation;

    @Column(nullable = false)
    private String residence;

    @Column(nullable = false)
    private String lastLogin;

    @Column(nullable = false)
    private String accStatus;

    private String loyaltyTitle;

    private String created;

    @OneToMany(mappedBy = "personage", fetch = FetchType.LAZY)
    private List<FormerName> formerNames = new ArrayList<>();

    @OneToMany(mappedBy = "personage", fetch = FetchType.LAZY)
    private List<FormerName> sexes = new ArrayList<>();

    @OneToMany(mappedBy = "personage", fetch = FetchType.LAZY)
    private List<LevelProgress> levelProgresses = new ArrayList<>();

    @OneToMany(mappedBy = "personage", fetch = FetchType.LAZY)
    private List<Achievements> achievements = new ArrayList<>();

    @OneToMany(mappedBy = "personage", fetch = FetchType.LAZY)
    private List<Guild> guilds = new ArrayList<>();

    @OneToMany(mappedBy = "personage", fetch = FetchType.LAZY)
    private List<House> houses = new ArrayList<>();

    @OneToMany(mappedBy = "personage", fetch = FetchType.LAZY)
    private List<Death> deaths = new ArrayList<>();

    @OneToMany(mappedBy = "personage", fetch = FetchType.LAZY)
    private List<Death> onlineTimes = new ArrayList<>();

    /**/
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar registeredDate;

}
