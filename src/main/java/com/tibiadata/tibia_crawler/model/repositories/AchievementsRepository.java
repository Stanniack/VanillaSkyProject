package com.tibiadata.tibia_crawler.model.repositories;

import com.tibiadata.tibia_crawler.model.entities.Achievements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Devmachine
 */
@Repository
public interface AchievementsRepository extends JpaRepository<Achievements, Long> {

    @Query(value = "SELECT points FROM achievements a JOIN Personage p ON a.parent_id = p.id WHERE p.name = :name ORDER BY a.day_progress DESC LIMIT 1",
            nativeQuery = true)
    String findLastPoints(@Param("name") String name);

    @Query(value = "SELECT points FROM achievements a WHERE parent_id = :personageId ORDER BY a.day_progress DESC LIMIT 1",
            nativeQuery = true)
    String findLastPointsById(@Param("personageId") Integer personageId);
}
