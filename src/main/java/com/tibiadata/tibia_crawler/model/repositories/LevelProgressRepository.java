package com.tibiadata.tibia_crawler.model.repositories;

import com.tibiadata.tibia_crawler.model.entities.LevelProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Devmachine
 */
@Repository
public interface LevelProgressRepository extends JpaRepository<LevelProgress, Long> {

    @Query(value = "SELECT level FROM level_progress lp JOIN Personage p ON lp.parent_id = p.id WHERE p.name = :name ORDER BY lp.day_progress DESC LIMIT 1",
            nativeQuery = true)
    String findLastLevelProgress(@Param("name") String name);

}
