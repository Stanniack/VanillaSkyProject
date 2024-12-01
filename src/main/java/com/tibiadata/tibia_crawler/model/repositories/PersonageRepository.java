package com.tibiadata.tibia_crawler.model.repositories;

import com.tibiadata.tibia_crawler.model.entities.Personage;
import java.util.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Devmachine
 */
@Repository
public interface PersonageRepository extends JpaRepository<Personage, Long> {

    @Query("SELECT p.registeredDate FROM Personage p WHERE p.name = :name")
    Calendar findRegisteredDateByName(@Param("name") String name);

    Personage findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT p.id FROM Personage p WHERE p.name = :name")
    Integer findIdByName(@Param("name") String name);
}
