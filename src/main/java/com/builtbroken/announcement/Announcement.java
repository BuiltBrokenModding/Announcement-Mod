package com.builtbroken.announcement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.concurrent.TimeUnit;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/28/2016.
 */
public class Announcement
{
    String text = "test";
    boolean firstCall = false;
    int delayToStartInSeconds = 10;
    int repeatIntervalInSeconds = -1;

    long lastTrigger = 0L;

    public Announcement()
    {

    }

    /**
     * Sends the text to the player
     *
     * @param player
     */
    public void outputToPlayer(EntityPlayer player)
    {
        if (player != null)
        {
            lastTrigger = System.currentTimeMillis();
            firstCall = true;
            player.addChatComponentMessage(new ChatComponentText(text));
        }
    }

    public boolean shouldTrigger(long time)
    {
        if (lastTrigger == 0)
        {
            lastTrigger = time;
        }
        if (!firstCall && lastTrigger + TimeUnit.SECONDS.toMillis(delayToStartInSeconds) <= time)
        {
            return true;
        }
        return repeatIntervalInSeconds >= 0 && lastTrigger + TimeUnit.SECONDS.toMillis(repeatIntervalInSeconds) <= time;
    }
}
