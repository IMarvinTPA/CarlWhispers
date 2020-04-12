package com.imarvintpa.carl_whispers;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CarlWhispersMod.MODID, name = CarlWhispersMod.NAME, version = CarlWhispersMod.VERSION, clientSideOnly=true)
public class CarlWhispersMod {
    public static final String MODID = "carl_whispers";
    public static final String NAME = "Carl Whispers";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	MinecraftForge.EVENT_BUS.register(new Whispering());
    }
        

}
