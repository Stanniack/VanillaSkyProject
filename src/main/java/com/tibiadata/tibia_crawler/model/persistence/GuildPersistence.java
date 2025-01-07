package com.tibiadata.tibia_crawler.model.persistence;

import com.tibiadata.tibia_crawler.model.entities.Guild;
import com.tibiadata.tibia_crawler.model.repositories.GuildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Devmachine
 */
@Component
public class GuildPersistence {

    @Autowired
    private GuildRepository gr;

    public Guild save(Guild guild) {
        return gr.save(guild);
    }
    
    public boolean personageGuildsExists(String guildName, Integer personageId){
        return gr.personageGuildsExists(guildName, personageId);
    }
    
    public Guild findLastGuild(String pName) {
        return gr.findLastGuild(pName);
    }

}
