package com.tibiadata.tibia_crawler.model.repositories;

import com.tibiadata.tibia_crawler.model.entities.FormerName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Devmachine
 */
@Repository
public interface FormerNameRepository extends JpaRepository<FormerName, Long> {

    FormerName findByFormerName(String formerName);

    @Query("SELECT COUNT(fn) > 0 FROM FormerName fn WHERE fn.formerName = :formerName AND fn.personage.id = :personageID")
    boolean isFormerNameFromPersonage(@Param("formerName") String formerName, @Param("personageID") Integer personageID);

}
