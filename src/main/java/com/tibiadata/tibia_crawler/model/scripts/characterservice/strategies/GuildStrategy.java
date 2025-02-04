package com.tibiadata.tibia_crawler.model.scripts.characterservice.strategies;

import com.tibiadata.tibia_crawler.model.entities.Guild;
import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.persistence.GuildPersistence;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author Devmachine
 */
@Service
@Scope("prototype")
public class GuildStrategy implements ObjectStrategy {

    @Autowired
    private GuildPersistence gp;
    
    private static final String GUILD = "Guild Membership:";
    private static final int ITEM = 1;

    private Guild guild;

    @Override
    public <T> void apply(Personage personage, String newValue) {
        String currentGuild = StringUtils.splitAndReplace(newValue, ITEM);
        String currentRank = StringUtils.splitAndReplace(currentGuild, "of the", 2, 0);
        String currentGuildName = StringUtils.splitAndReplace(currentGuild, "of the", 2, 1);

        guildValidator(personage, currentRank, currentGuildName);
        ObjectHandler.persistObject(guild, currGuild -> currGuild.setPersonage(personage), currGuild -> gp.save(currGuild));
    }

    private void guildValidator(Personage personage, String rank, String guildName) {
        //Guild dbGuild = (oldName != null) ? gp.findLastGuildById(oldName) : gp.findLastGuild(personage.getName());
        Guild dbGuild = gp.findLastGuildById(personage.getId().toString());

        // Se último guildName existe no bd e é igual ao guildName atual
        if (dbGuild != null && dbGuild.getGuildName().equals(guildName)) {
            // Se o rank não é igual ao do bd, alterar rank e persistir
            if (!dbGuild.getGuildRank().equals(rank)) {
                dbGuild.setGuildRank(rank);
                this.guild = dbGuild; // atribui guild com valores alterados para persistir
            }

        } else { // Senão: guildName não existe, instanciar guild
            this.guild = new Guild(rank, guildName, Calendar.getInstance());
        }
    }

    @Override
    public String getKey() {
        return GUILD;
    }
}
