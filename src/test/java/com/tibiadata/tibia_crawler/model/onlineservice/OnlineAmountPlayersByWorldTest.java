package com.tibiadata.tibia_crawler.model.onlineservice;

import com.tibiadata.tibia_crawler.model.scripts.onlineservice.OnlineAmountPlayersByWorld;
import com.tibiadata.tibia_crawler.model.utils.TibiaUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
public class OnlineAmountPlayersByWorldTest {
    @Autowired
    OnlineAmountPlayersByWorld onlineAmountPlayersByWorld;

    @Test
    public void testAllWorldsOnline() {
        assertTrue("Erro de requisição ou os servidores podem estar offline", onlineAmountPlayersByWorld.worldTypes(TibiaUtils.getAllWorlds()) > 0);
    }

    @Test
    public void testOptionalPvpWorldsOnline() {
        assertTrue("Erro de requisição ou os servidores podem estar offline", onlineAmountPlayersByWorld.worldTypes(TibiaUtils.getOptionalPvpWorlds()) > 0);
    }

    @Test
    public void testOpenPvpWorldsOnline() {
        assertTrue("Erro de requisição ou os servidores podem estar offline", onlineAmountPlayersByWorld.worldTypes(TibiaUtils.getOpenPvpWorlds()) > 0);
    }

    @Test
    public void testRetroOpenPvpWorldsOnline() {
        assertTrue("Erro de requisição ou os servidores podem estar offline", onlineAmountPlayersByWorld.worldTypes(TibiaUtils.getRetroOpenPvpWorlds()) > 0);
    }

    @Test
    public void testRetroHardcorePvpWorldsOnline() {
        assertTrue("Erro de requisição ou os servidores podem estar offline", onlineAmountPlayersByWorld.worldTypes(TibiaUtils.getRetroHardcorePvpWorlds()) > 0);
    }

    @Test
    public void testExperimentalWorldsOnline() {
        assertTrue("Erro de requisição ou os servidores podem estar offline", onlineAmountPlayersByWorld.worldTypes(TibiaUtils.getExperimentalWorlds()) > 0);
    }

    @Test
    public void testUniqueWorld(){
        assertTrue("Erro de requisição ou os servidores podem estar offline", onlineAmountPlayersByWorld.uniqueWorld("Antica") > 0);
    }
}
