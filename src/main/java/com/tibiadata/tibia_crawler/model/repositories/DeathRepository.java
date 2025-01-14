package com.tibiadata.tibia_crawler.model.repositories;

import com.tibiadata.tibia_crawler.model.entities.Death;
import java.util.Calendar;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Devmachine
 */
@Repository
public interface DeathRepository extends JpaRepository<Death, Long> {

    @Query(value = "SELECT death_date FROM Death d JOIN Personage p ON d.parent_id = p.id "
            + "WHERE p.name = :name and death_date = :deathDate ORDER BY d.death_date DESC LIMIT 1", nativeQuery = true)
    Date findDeathByDate(@Param("deathDate") Calendar deathDate, @Param("name") String pName);

    @Query(value = "SELECT death_date FROM Death d WHERE parent_id = :personageId AND death_date = :deathDate ORDER BY d.death_date DESC LIMIT 1",
            nativeQuery = true)
    Date findDeathByDateAndPersonageId(@Param("deathDate") Calendar deathDate, @Param("personageId") Integer personageId);

}
