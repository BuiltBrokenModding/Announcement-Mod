package com.builtbroken.announcement;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/28/2016.
 */
@net.minecraftforge.fml.common.Mod(modid = "bbm_announcements", name = "External Announcements", version = "@MAJOR@.@MINOR@.@REVIS@.@BUILD@", acceptableRemoteVersions = "*", canBeDeactivated = true)
public class Mod
{
    public static Logger LOGGER;

    public static String announcementPath;

    @net.minecraftforge.fml.common.Mod.Metadata("bbm_announcements")
    public static ModMetadata meta;

    @SidedProxy(clientSide = "com.builtbroken.announcement.ClientProxy", serverSide = "com.builtbroken.announcement.ServerProxy")
    public static CommonProxy proxy;

    @net.minecraftforge.fml.common.Mod.EventHandler
    public void disableEvent(FMLModDisabledEvent event)
    {
        LOGGER.info("Disabling mod");
    }

    @net.minecraftforge.fml.common.Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER = LogManager.getLogger("External Announcements");
        this.loadModMeta();
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "AnnouncementsMod.cfg"));
        config.load();
        announcementPath = config.getString("AnnouncementPath", Configuration.CATEGORY_GENERAL, "URL:https://gist.githubusercontent.com/Hennamann/8bd0d12ef254b95e82dbf6cabd267317/raw/e6701b1874a4d61195e2817417cef717aa1cda57/test-announcement.json", "Web or local path that will be used to access the announcements file.");
        config.save();
        proxy.preInit();
        MinecraftForge.EVENT_BUS.register(proxy);
    }

    public void loadModMeta()
    {
        meta.modId = "bbm_announcements";
        meta.name = "External Announcements";
        meta.description = "Loads announcements from an external json or txt file either locally or from a url.";
        meta.url = "http://www.builtbroken.com";
        meta.version = "@MAJOR@.@MINOR@.@REVIS@.@BUILD@";
        meta.authorList = Arrays.asList("DarkGuardsman", "Hennamann");
        meta.autogenerated = false;
    }
}
