package com.tibiadata.tibia_crawler;

import com.tibiadata.tibia_crawler.model.persistence.DeathPersistence;
import com.tibiadata.tibia_crawler.model.persistence.FormerNamePersistence;
import com.tibiadata.tibia_crawler.model.persistence.GuildPersistence;
import com.tibiadata.tibia_crawler.model.utils.CalendarUtils;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TibiaCrawlerApplication3 {

    public static void main(String[] args) throws IOException, ParseException {
        ApplicationContext context = SpringApplication.run(TibiaCrawlerApplication3.class, args);
        FormerNamePersistence fnp = context.getBean(FormerNamePersistence.class);
        GuildPersistence gp = context.getBean(GuildPersistence.class);
        DeathPersistence dp = context.getBean(DeathPersistence.class);
        //System.out.println(fnp.findDateOfLastFormerNameRegistered("Vitor", 1));
        //System.out.println(gp.findLastGuild("Vepeh").getGuildName());
        System.out.println(dp.findDeathByDate(Calendar.getInstance(), "Mat Vit Cele"));

        //boolean diff = new CalendarUtils().greaterThan180Days(new CalendarUtils().parseDate("01/01/1999"), Calendar.getInstance());
        //System.out.println(diff);

    }

}
