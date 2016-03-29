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
        if (true || Mod.announcementPath != null && !Mod.announcementPath.isEmpty() && !Mod.announcementPath.equals("null"))
        {
            String path = Mod.announcementPath.trim();
            boolean isURL = path.startsWith("URL:");
            boolean isFile = path.startsWith("FILE:");
            path = path.replaceFirst("URL:", "").replaceFirst("FILE:", "");

            if (path.endsWith(".txt"))
            {
                BufferedReader in = null;

                //Get out data stream
                if (isURL)
                {
                    try
                    {
                        URL url = new URL(path);
                        in = new BufferedReader(new InputStreamReader(url.openStream()));
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
                else if (isFile)
                {
                    File file = new File(path);
                    try
                    {
                        in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
                //If we have a stream
                if (in != null)
                {
                    try
                    {
                        //Convert stream into a large string TODO find better way
                        String textSoFar = "";
                        String inputLine;
                        while ((inputLine = in.readLine()) != null)
                        {
                            textSoFar += inputLine;
                        }
                        in.close();
                        processTextFile(textSoFar);
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
            else if (path.endsWith(".xml"))
            {

            }
            else if (path.endsWith(".json"))
            {

            }
        }
    }

    protected void processTextFile(String string)
    {
        if (string.contains("{") && string.contains("}"))
        {
            while (string.contains("{") && string.contains("}"))
            {
                //Get start and end point for this line
                int start = string.indexOf("{");
                int end = string.indexOf("}");
                //Get sub string for out line
                String sub = string.substring(start, end).replace("{", "");

                if (sub.contains(","))
                {
                    String[] split = sub.split(",");
                    Announcement announcement = new Announcement();
                    announcement.text = split[0].replace("\"", "");

                    try
                    {
                        if (split[1].endsWith("s"))
                        {
                            announcement.delayToStartInSeconds = Integer.parseInt(split[1].trim().replace("s", ""));
                        }
                        else if (split[1].endsWith("m"))
                        {
                            announcement.delayToStartInSeconds = Integer.parseInt(split[1].trim().replace("m", "")) * 60;
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        //TODO errors
                        e.printStackTrace();
                    }
                    if (split.length > 2)
                    {
                        try
                        {
                            if (split[1].endsWith("s"))
                            {
                                announcement.repeatIntervalInSeconds = Integer.parseInt(split[2].trim().replace("s", ""));
                            }
                            else if (split[1].endsWith("m"))
                            {
                                announcement.repeatIntervalInSeconds = Integer.parseInt(split[2].trim().replace("m", "")) * 60;
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            //TODO errors
                            e.printStackTrace();
                        }
                    }
                    announcementList.add(announcement);
                }
                else if (string.contains("\""))
                {
                    Announcement announcement = new Announcement();
                    announcement.text = sub;
                    announcementList.add(announcement);
                }
                else
                {
                    //TODO errors
                }
                //Set what is left
                if (end + 2 < string.length())
                {
                    string = string.substring(end + 2);
                }
                else
                {
                    break;
                }
            }
        }
        else
        {
            //TODO send error message
        }
    }
}
