package com.builtbroken.announcement.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Hennamann(Ole Henrik Stabell) on 02/03/2018.
 */
@Config(modid = "bbm_announcements", name = "AnnouncementsMod")
@Config.LangKey("snowpower.config.title")
public class AnnouncementConfig {

    @Config.Name("Announcement file path")
    @Config.Comment({"Web or local path that will be used to access the announcements file.", "Prefixed with URL: for a url or FILE: for a local file.", "The file should either be JSON or TXT more info on the curse page or wiki."})
    public static String ANNOUNCEMENT_PATH = "URL:https://gist.githubusercontent.com/Hennamann/8bd0d12ef254b95e82dbf6cabd267317/raw/e6701b1874a4d61195e2817417cef717aa1cda57/test-announcement.json";

    @Mod.EventBusSubscriber(modid = "bbm_announcements")
    private static class EventHandler {

        @SubscribeEvent
        public static void onConfigChangedEvent(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals("bbm_announcements")) {
                ConfigManager.sync("bbm_announcements", Config.Type.INSTANCE);
            }
        }
    }
}