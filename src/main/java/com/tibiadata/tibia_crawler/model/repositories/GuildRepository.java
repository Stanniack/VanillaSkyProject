package com.tibiadata.tibia_crawler.model.repositories;

import com.tibiadata.tibia_crawler.model.entities.Guild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Devmachine
 */
public interface GuildRepository extends JpaRepository<Guild, Long> {

    @Query("SELECT COUNT(g) > 0 FROM Guild g WHERE g.guildName = :guildName AND g.personage.id = :personageID")
    boolean personageGuildsExists(@Param("guildName") String guildName, @Param("personageID") Integer personageId);

}
