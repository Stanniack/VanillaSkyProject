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
public class OnlineTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer seconds;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar registeredDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private Personage personage;

    public OnlineTime(Integer seconds, Calendar registeredDate, Personage personage) {
        this.seconds = seconds;
        this.registeredDate = registeredDate;
        this.personage = personage;
    }

}
