package com.tibiadata.tibia_crawler.model.repositories;

import com.tibiadata.tibia_crawler.model.entities.Sex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Devmachine
 */
@Repository
public interface SexRepository extends JpaRepository<Sex, Long> {

    @Query(value = "SELECT genre FROM Sex s JOIN Personage p ON s.parent_id = p.id WHERE p.name = :name ORDER BY s.change_date DESC LIMIT 1",
            nativeQuery = true)
    String findLastSex(@Param("name") String personageName);

}
