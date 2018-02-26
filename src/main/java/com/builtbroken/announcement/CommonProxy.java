package com.builtbroken.announcement;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    public List<Announcement> announcementList = new ArrayList();

    public void preInit()
    {
        process();
    }

    /**
     * Called to process the announcement path loaded from config or sent by packets
     */
    @SuppressWarnings("unchecked")
    public void process()
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
                        try
                        {
                            processTextFile(textSoFar);
                        }
                        catch (Exception e)
                        {
                            Mod.LOGGER.error("Failed to parse announcement[" + textSoFar + "]\n", e);
                        }
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
            else if (path.endsWith(".json")) {
                BufferedReader in = null;

                //Get out data stream
                if (isURL) {
                    try {
                        URL url = new URL(path);
                        in = new BufferedReader(new InputStreamReader(url.openStream()));
                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (isFile) {
                    File file = new File(path);
                    try {
                        in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                //If we have a stream
                if (in != null) {
                    try {
                        //Convert stream into a large string TODO find better way
                        String textSoFar = "";
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            textSoFar += inputLine;
                        }
                        in.close();
                        try {
                            processJsonFile(textSoFar);
                        }
                        catch (Exception e) {
                            Mod.LOGGER.error("Failed to parse announcement[" + textSoFar + "]\n", e);
                        }
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Parses the JSON file using Gson.
     * TODO Add more error/exception handling.
     */
    public void processJsonFile(String string) throws RuntimeException {

        JsonElement jelement = new JsonParser().parse(string);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray jarray = jobject.getAsJsonArray("messages");
        for(int i = 0 ; i < jarray.size(); i++) {

            Announcement announcement = new Announcement();

            jobject = jarray.get(i).getAsJsonObject();
            String message = jobject.get("message").getAsString();
            String delay = jobject.get("delayTime").getAsString();
            String waitTime = jobject.get("waitTime").getAsString();

            announcement.text = message;
            if (delay.endsWith("s")) {
                announcement.delayToStartInSeconds = Integer.parseInt(delay.trim().replace("s", ""));
            } else if (delay.endsWith("m")) {
                announcement.delayToStartInSeconds = Integer.parseInt(delay.trim().replace("m", ""));
            } else {
                throw new RuntimeException("Invalid delayTime it can only be set in seconds(s) or minutes(m)");
            }

            if (waitTime.endsWith("s")) {
                announcement.repeatIntervalInSeconds = Integer.parseInt(waitTime.trim().replace("s", ""));
            } else if (waitTime.endsWith("m")) {
                announcement.repeatIntervalInSeconds = Integer.parseInt(waitTime.trim().replace("m", ""));
            } else {
                throw new RuntimeException("Invalid waitTime it can only be set in seconds(s) or minutes(m)");
            }

            announcementList.add(announcement);
        }
    }

    @SuppressWarnings("unchecked")
    public void processTextFile(String string) throws RuntimeException
    {
        if (string.contains("{") && string.contains("}"))
        {
            while (string.contains("{") && string.contains("}"))
            {
                //Get start and end point for this line
                int start = string.indexOf("{");
                int end = string.indexOf("}");
                if (start != -1 && end != -1)
                {
                    //Get sub string for out line
                    String sub = string.substring(start + 1, end).replace("{", "").trim();

                    if (sub.contains(","))
                    {
                        String[] split = sub.split(",");

                        if (!split[0].contains("\"") || split[0].replace("\"", "").trim().isEmpty())
                        {
                            throw new RuntimeException("Invalid format for section[" + sub + "] as message can not be empty");
                        }
                        Announcement announcement = new Announcement();
                        announcement.text = split[0].replace("\"", "").trim();

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
                            else
                            {
                                throw new RuntimeException("Invalid format for section[" + sub + "] when parsing " + split[1] + " it should end in an s or m.");
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            throw new RuntimeException("Invalid format for section[" + sub + "] when parsing " + split[1], e);
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
                                else
                                {
                                    throw new RuntimeException("Invalid format for section[" + sub + "] when parsing " + split[1] + " it should end in an s or m.");
                                }
                            }
                            catch (NumberFormatException e)
                            {
                                throw new RuntimeException("Invalid format for section[" + sub + "] when parsing " + split[1], e);
                            }
                        }
                        announcementList.add(announcement);
                    }
                    else if (sub.contains("\""))
                    {
                        sub = sub.replace("\"", "").trim();
                        if (!sub.isEmpty())
                        {
                            Announcement announcement = new Announcement();
                            announcement.text = sub.replaceAll("\"", "").trim();
                            announcementList.add(announcement);
                        }
                        else
                        {
                            throw new RuntimeException("Invalid format for section[" + sub + "] as message is empty");
                        }
                    }
                    else
                    {
                        throw new RuntimeException("Invalid format for section[" + sub + "]");
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
        }
        else
        {
            throw new RuntimeException("Invalid format for section input[" + string + "]\nIt should start with '{' and end with '}' for each section");
        }
    }
}
