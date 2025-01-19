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

    public House(String houseName, String paidUntil, Calendar registeredDate) {
        this.houseName = houseName;
        this.paidUntil = paidUntil;
        this.registeredDate = registeredDate;
    }
}
