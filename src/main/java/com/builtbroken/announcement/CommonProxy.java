package com.builtbroken.announcement;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/28/2016.
 */
public class CommonProxy
{
    protected List<Announcement> announcementList = new ArrayList();

    public void preInit()
    {
        process();
    }

    /**
     * Called to process the announcement path loaded from config or sent by packets
     */
    protected void process()
    {
        if (Mod.announcementPath != null && !Mod.announcementPath.isEmpty() && !Mod.announcementPath.equals("null"))
        {
            String path = Mod.announcementPath.trim();
            if (path.startsWith("URL:"))
            {
                path = path.replace("URL:", "");
                try
                {
                    URL url = new URL(path);
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                    String inputLine;
                    while ((inputLine = in.readLine()) != null)
                    {
                        System.out.println(inputLine);
                    }
                    in.close();
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else if(path.startsWith("FILE:"))
            {
                path = path.replace("FILE:", "");
                File file = new File(path);
                try
                {
                    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null)
                    {
                        System.out.println(inputLine);
                    }
                    in.close();
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
