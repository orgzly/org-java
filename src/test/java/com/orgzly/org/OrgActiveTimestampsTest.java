package com.orgzly.org;

import com.orgzly.org.datetime.OrgRange;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class OrgActiveTimestampsTest extends OrgTestParser {
    @Test
    public void testPlainTimestamp() {
        String str = "<2018-01-15 Mon>\n";

        List<OrgRange> timestamps = OrgActiveTimestamps.parse(str);

        Assert.assertEquals(1, timestamps.size());
        Assert.assertEquals("<2018-01-15 Mon>", timestamps.get(0).toString());
    }

    @Test
    public void testPlainTimestamps() {
        String str =
                "<2018-01-15 Mon> some text between timestamps <2018-01-16 Tue>\n" +
                "Blabla some other text in front <2018-01-17 Wed> text after\n" +
                "\n\n" +
                "[2018-01-18 Thu]\n\n";

        List<OrgRange> timestamps = OrgActiveTimestamps.parse(str);

        Assert.assertEquals(3, timestamps.size());
        Assert.assertEquals("<2018-01-15 Mon>", timestamps.get(0).toString());
        Assert.assertEquals("<2018-01-16 Tue>", timestamps.get(1).toString());
        Assert.assertEquals("<2018-01-17 Wed>", timestamps.get(2).toString());
    }
}