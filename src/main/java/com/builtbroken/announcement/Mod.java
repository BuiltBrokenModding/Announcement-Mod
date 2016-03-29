package com.builtbroken.announcement;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLModDisabledEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/28/2016.
 */
@cpw.mods.fml.common.Mod(modid = "bbm_announcements", name = "Announcements", version = "@MAJOR@.@MINOR@.@REVIS@.@BUILD@", acceptableRemoteVersions = "*", canBeDeactivated = true)
public class Mod
{
    public static Logger LOGGER;

    public static String announcementPath;

    @SidedProxy(clientSide = "com.builtbroken.announcement.ClientProxy", serverSide = "com.builtbroken.announcement.ServerProxy")
    public static CommonProxy proxy;

    @cpw.mods.fml.common.Mod.EventHandler
    public void disableEvent(FMLModDisabledEvent event)
    {
        LOGGER.info("Disabling mod");
    }

    @cpw.mods.fml.common.Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), "bbm/AI_Improvements.cfg"));
        config.load();
        announcementPath = config.getString("AnnouncementPath", Configuration.CATEGORY_GENERAL, "URL:https://dl.dropboxusercontent.com/u/70622753/work/announcementTestFile.txt", "Web or local path that will be used to access the announcements file.");
        config.save();
        proxy.preInit();
        FMLCommonHandler.instance().bus().register(proxy);
    }
}
