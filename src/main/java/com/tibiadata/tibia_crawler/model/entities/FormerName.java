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
    private Calendar changeDate;

    public FormerName(String formerName, Calendar registratedDate) {
        this.formerName = formerName;
        this.changeDate = registratedDate;
    }

    public FormerName(String formerName, Personage personage, Calendar registratedDate) {
        this.formerName = formerName;
        this.personage = personage;
        this.changeDate = registratedDate;
    }
}
