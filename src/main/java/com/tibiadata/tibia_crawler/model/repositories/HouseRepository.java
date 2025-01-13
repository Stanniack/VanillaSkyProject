package com.tibiadata.tibia_crawler.model.repositories;

import com.tibiadata.tibia_crawler.model.entities.House;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Devmachine
 */
@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    @Query(value = "SELECT h.* FROM house h JOIN Personage p ON h.parent_id = p.id WHERE p.name = :name ORDER BY h.registered_date DESC LIMIT 3",
            nativeQuery = true)
    List<House> findLastThreeHouses(@Param("name") String name);

    @Query(value = "SELECT * FROM house h WHERE parent_id = :personageId ORDER BY h.registered_date DESC LIMIT 3",
            nativeQuery = true)
    List<House> findLastThreeHousesById(@Param("personageId") Integer personageId);
}
