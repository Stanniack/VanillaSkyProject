package com.tibiadata.tibia_crawler.model.repositories;

import com.tibiadata.tibia_crawler.model.entities.Personage;
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

    Personage findByName(String name);

    boolean existsByName(String name);
}
