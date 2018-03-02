package com.builtbroken.tests;

import com.builtbroken.announcement.config.AnnouncementConfig;
import com.builtbroken.announcement.proxy.CommonProxy;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Hennamann(Ole Henrik Stabell) on 02/03/2018.
 */
public class TestJsonProcessing extends TestCase {

    @Test
    public void testJsonURLandMessage() {
        // {"messages":[{"message":"Hello this is a test message","delayTime":"10s","waitTime":"100s"},{"message":"Hello this is a second test message","delayTime":"20s","waitTime":"100s"}]
        AnnouncementConfig.ANNOUNCEMENT_PATH = "URL:https://gist.githubusercontent.com/Hennamann/8bd0d12ef254b95e82dbf6cabd267317/raw/e6701b1874a4d61195e2817417cef717aa1cda57/test-announcement.json";
        CommonProxy proxy = new CommonProxy();
        proxy.process();
        assertTrue(proxy.announcementList.size() == 2);
        assertTrue(proxy.announcementList.get(0).text, proxy.announcementList.get(0).text.equals("Hello this is a test message"));
        assertTrue(proxy.announcementList.get(1).text, proxy.announcementList.get(1).text.equals("Hello this is a second test message"));
    }
}