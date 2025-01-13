package com.tibiadata.tibia_crawler.model.repositories;

import com.tibiadata.tibia_crawler.model.entities.World;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Devmachine
 */
@Repository
public interface WorldRepository extends JpaRepository<World, Long> {

    @Query(value = "SELECT server FROM World w JOIN Personage p ON w.parent_id = p.id WHERE p.name = :name ORDER BY w.change_date DESC LIMIT 1",
            nativeQuery = true)
    String findLastWorld(@Param("name") String personageName);

    @Query(value = "SELECT server FROM World w WHERE parent_id = :personageId ORDER BY w.change_date DESC LIMIT 1",
            nativeQuery = true)
    String findLastWorldById(@Param("personageId") Integer personageId);
}
