package com.builtbroken.tests;

import com.builtbroken.announcement.ExternalAnnouncements;
import com.builtbroken.announcement.config.AnnouncementConfig;
import com.builtbroken.announcement.proxy.CommonProxy;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/29/2016.
 */
public class TestTextProcessing extends TestCase
{
    @Test
    public void testTextURL()
    {
        //{"Hello this is a test message", 10s, 100s}, {"Hello this is a second test message", 20s, 100s}
        AnnouncementConfig.ANNOUNCEMENT_PATH = "URL:https://gist.githubusercontent.com/Hennamann/9a0f686213b0bb377e9600fe55bd7736/raw/c437c705c6d2c3e2972060eabf766a687c2e60c3/test-announcement.txt";
        CommonProxy proxy = new CommonProxy();
        proxy.process();
        assertTrue(proxy.announcementList.size() == 2);
        assertTrue(proxy.announcementList.get(0).text, proxy.announcementList.get(0).text.equals("Hello this is a test message"));
        assertTrue(proxy.announcementList.get(1).text, proxy.announcementList.get(1).text.equals("Hello this is a second test message"));
    }

    @Test
    public void testSimpleMessage()
    {
        //Single message, no settings
        CommonProxy proxy = new CommonProxy();
        proxy.processTextFile("{\"Message\"}");
        assertTrue(proxy.announcementList.size() == 1);
        assertTrue(proxy.announcementList.get(0).text, proxy.announcementList.get(0).text.equals("Message"));

        //several messages, no settings
        proxy = new CommonProxy();
        proxy.processTextFile("{\"Message\"},{\"Message2\"},{\"Message3\"}");
        assertTrue(proxy.announcementList.size() == 3);
        assertTrue(proxy.announcementList.get(0).text, proxy.announcementList.get(0).text.equals("Message"));
        assertTrue(proxy.announcementList.get(1).text, proxy.announcementList.get(1).text.equals("Message2"));
        assertTrue(proxy.announcementList.get(2).text, proxy.announcementList.get(2).text.equals("Message3"));
    }

    @Test
    public void testDelaySettings()
    {
        //Single message, no settings
        CommonProxy proxy = new CommonProxy();
        proxy.processTextFile("{\"Message\", 10s}");
        assertTrue(proxy.announcementList.size() == 1);
        assertTrue(proxy.announcementList.get(0).text, proxy.announcementList.get(0).text.equals("Message"));
        assertEquals(proxy.announcementList.get(0).delayToStartInSeconds, 10);

        //several messages, no settings
        proxy = new CommonProxy();
        proxy.processTextFile("{\"Message\", 10s},{\"Message2\", 12s},{\"Message3\", 13s}");
        assertTrue(proxy.announcementList.size() == 3);
        assertTrue(proxy.announcementList.get(0).text, proxy.announcementList.get(0).text.equals("Message"));
        assertTrue(proxy.announcementList.get(1).text, proxy.announcementList.get(1).text.equals("Message2"));
        assertTrue(proxy.announcementList.get(2).text, proxy.announcementList.get(2).text.equals("Message3"));
        assertEquals(proxy.announcementList.get(0).delayToStartInSeconds, 10);
        assertEquals(proxy.announcementList.get(1).delayToStartInSeconds, 12);
        assertEquals(proxy.announcementList.get(2).delayToStartInSeconds, 13);
    }

    @Test
    public void testRepeatSettings()
    {
        //Single message, no settings
        CommonProxy proxy = new CommonProxy();
        proxy.processTextFile("{\"Message\", 10s, 100s}");
        assertTrue(proxy.announcementList.size() == 1);
        assertTrue(proxy.announcementList.get(0).text, proxy.announcementList.get(0).text.equals("Message"));
        assertEquals(proxy.announcementList.get(0).delayToStartInSeconds, 10);
        assertEquals(proxy.announcementList.get(0).repeatIntervalInSeconds, 100);

        //several messages, no settings
        proxy = new CommonProxy();
        proxy.processTextFile("{\"Message\", 10s, 122s},{\"Message2\", 12s, 133s},{\"Message3\", 13s, 144s}");
        assertTrue(proxy.announcementList.size() == 3);
        assertTrue(proxy.announcementList.get(0).text, proxy.announcementList.get(0).text.equals("Message"));
        assertTrue(proxy.announcementList.get(1).text, proxy.announcementList.get(1).text.equals("Message2"));
        assertTrue(proxy.announcementList.get(2).text, proxy.announcementList.get(2).text.equals("Message3"));
        assertEquals(proxy.announcementList.get(0).delayToStartInSeconds, 10);
        assertEquals(proxy.announcementList.get(1).delayToStartInSeconds, 12);
        assertEquals(proxy.announcementList.get(2).delayToStartInSeconds, 13);
        assertEquals(proxy.announcementList.get(0).repeatIntervalInSeconds, 122);
        assertEquals(proxy.announcementList.get(1).repeatIntervalInSeconds, 133);
        assertEquals(proxy.announcementList.get(2).repeatIntervalInSeconds, 144);
    }

    @Test
    public void testNoCommas()
    {
        //several messages, no settings
        CommonProxy proxy = new CommonProxy();
        proxy.processTextFile("{\"Message\", 10s},{\"Message2\", 12s},{\"Message3\", 13s}");
        assertTrue(proxy.announcementList.size() == 3);
        assertTrue(proxy.announcementList.get(0).text, proxy.announcementList.get(0).text.equals("Message"));
        assertTrue(proxy.announcementList.get(1).text, proxy.announcementList.get(1).text.equals("Message2"));
        assertTrue(proxy.announcementList.get(2).text, proxy.announcementList.get(2).text.equals("Message3"));
        assertEquals(proxy.announcementList.get(0).delayToStartInSeconds, 10);
        assertEquals(proxy.announcementList.get(1).delayToStartInSeconds, 12);
        assertEquals(proxy.announcementList.get(2).delayToStartInSeconds, 13);
    }

    @Test
    public void testBadFormat()
    {
        CommonProxy proxy;

        try
        {
            proxy = new CommonProxy();
            proxy.processTextFile("{\"\"");
            fail("Should have errored");
        }
        catch (RuntimeException e)
        {
            if(!e.getMessage().contains("Invalid format for section"))
            {
                fail("Wrong error");
            }
        }
        try
        {
            proxy = new CommonProxy();
            proxy.processTextFile("\"\"}");
            fail("Should have errored");
        }
        catch (RuntimeException e)
        {
            if(!e.getMessage().contains("Invalid format for section"))
            {
                fail("Wrong error");
            }
        }
        try
        {
            proxy = new CommonProxy();
            proxy.processTextFile("\"\"");
            fail("Should have errored");
        }
        catch (RuntimeException e)
        {
            if(!e.getMessage().contains("Invalid format for section"))
            {
                fail("Wrong error");
            }
        }
        //Empty message, simple
        try
        {
            proxy = new CommonProxy();
            proxy.processTextFile("{\"\"}");
            fail("Should have errored");
        }
        catch (RuntimeException e)
        {
            if(!e.getMessage().contains("Invalid format for section"))
            {
                fail("Wrong error");
            }
        }
        //Empty message, with data
        try
        {
            proxy = new CommonProxy();
            proxy.processTextFile("{\"\", 10s}");
            fail("Should have errored");
        }
        catch (RuntimeException e)
        {
            if(!e.getMessage().contains("Invalid format for section"))
            {
                fail("Wrong error");
            }
        }

        //Bad data for start delay
        try
        {
            proxy = new CommonProxy();
            proxy.processTextFile("{\"dddd\", 10dsasd}");
            fail("Should have errored");
        }
        catch (RuntimeException e)
        {
            if(!e.getMessage().contains("Invalid format for section"))
            {
                fail("Wrong error");
            }
        }
        try
        {
            proxy = new CommonProxy();
            proxy.processTextFile("{\"dddd\", 10}");
            fail("Should have errored");
        }
        catch (RuntimeException e)
        {
            if(!e.getMessage().contains("Invalid format for section"))
            {
                fail("Wrong error");
            }
        }

        //Bad data for start delay
        try
        {
            proxy = new CommonProxy();
            proxy.processTextFile("{\"dddd\", 10s, 100sdsad}");
            fail("Should have errored");
        }
        catch (RuntimeException e)
        {
            if(!e.getMessage().contains("Invalid format for section"))
            {
                fail("Wrong error");
            }
        }
        try
        {
            proxy = new CommonProxy();
            proxy.processTextFile("{\"dddd\", 10s, 100d}");
            fail("Should have errored");
        }
        catch (RuntimeException e)
        {
            if(!e.getMessage().contains("Invalid format for section"))
            {
                fail("Wrong error");
            }
        }
    }
}
