package com.tibiadata.tibia_crawler.model.repositories;

import com.tibiadata.tibia_crawler.model.entities.Guild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Devmachine
 */
@Repository
public interface GuildRepository extends JpaRepository<Guild, Long> {

    @Query("SELECT COUNT(g) > 0 FROM Guild g WHERE g.guildName = :guildName AND g.personage.id = :personageID")
    boolean personageGuildsExists(@Param("guildName") String guildName, @Param("personageID") Integer personageId);

    @Query(value = "SELECT g.* FROM guild g JOIN Personage p ON g.parent_id = p.id WHERE p.name = :name ORDER BY g.registered_date DESC LIMIT 1",
            nativeQuery = true)
    Guild findLastGuild(@Param("name") String personageName);

}
