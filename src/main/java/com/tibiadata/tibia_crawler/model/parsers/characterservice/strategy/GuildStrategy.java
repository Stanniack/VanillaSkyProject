package com.tibiadata.tibia_crawler.model.parsers.characterservice.strategy;

import com.tibiadata.tibia_crawler.model.entities.Guild;
import com.tibiadata.tibia_crawler.model.entities.Personage;
import com.tibiadata.tibia_crawler.model.persistence.GuildPersistence;
import com.tibiadata.tibia_crawler.model.utils.StringUtils;
import jakarta.transaction.Transactional;
import java.util.Calendar;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Devmachine
 */
@Service
public class GuildStrategy implements ObjectStrategy {

    private static final int ITEM = 1;
    //
    @Autowired
    private GuildPersistence gp;
    //
    private Guild guild;

    @Override
    @Transactional
    public <T> void apply(Personage personage, String newValue) {
        String currentGuild = StringUtils.splitAndReplace(newValue, ITEM);
        String currentRank = StringUtils.splitAndReplace(currentGuild, "of the", 2, 0);
        String currentGuildName = StringUtils.splitAndReplace(currentGuild, "of the", 2, 1);

        guildValidator(personage, currentRank, currentGuildName);
        ObjectHandler.persistObject(guild, currGuild -> currGuild.setPersonage(personage), currGuild -> gp.save(currGuild));
    }

    @Transactional
    private void guildValidator(Personage personage, String rank, String guildName) {
        //Guild dbGuild = (oldName != null) ? gp.findLastGuildById(oldName) : gp.findLastGuild(personage.getName());
        Guild dbGuild = gp.findLastGuildById(personage.getId().toString());

        // Se último guildName existe no bd e é igual ao guildName atual
        if (dbGuild != null && dbGuild.getGuildName().equals(guildName)) {
            // Se o rank não é igual ao do bd, alterar rank e persistir
            if (!dbGuild.getRank().equals(rank)) {
                dbGuild.setRank(rank);
                this.guild = dbGuild; // atribui guild com valores alterados para persistir
            }

        } else { // Senão: guildName não existe, instanciar guild
            this.guild = new Guild(rank, guildName, Calendar.getInstance());
        }
    }
}
