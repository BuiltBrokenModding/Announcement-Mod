package com.builtbroken.announcement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/28/2016.
 */
public class ClientProxy extends CommonProxy
{
    int ticks = 0;

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent event)
    {
        ticks++;
        //Minecraft needs to be loaded, with a player, in a world, and the game needs to have focus
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().theWorld != null && ticks % 20 == 0)
        {
            for (Announcement announcement : announcementList)
            {
                if (announcement.shouldTrigger(System.currentTimeMillis()))
                {
                    announcement.outputToPlayer(Minecraft.getMinecraft().thePlayer);
                }
            }
        }
        if (ticks >= Integer.MAX_VALUE - 1)
        {
            ticks = 1;
        }
    }
}
