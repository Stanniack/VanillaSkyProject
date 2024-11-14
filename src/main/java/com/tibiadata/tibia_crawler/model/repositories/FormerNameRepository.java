package com.tibiadata.tibia_crawler.model.repositories;

import com.tibiadata.tibia_crawler.model.entities.FormerName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Devmachine
 */
@Repository
public interface FormerNameRepository extends JpaRepository<FormerName, Long> {

    FormerName findByFormerName(String formerName);
}
