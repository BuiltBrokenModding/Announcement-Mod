package com.builtbroken.announcement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/28/2016.
 */
public class Announcement
{
    String text = "test";
    int delayToStartInSeconds = -1;
    int repeatIntervalInSeconds = -1;

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
            player.addChatComponentMessage(new ChatComponentText(text));
        }
    }
}
