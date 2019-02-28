package com.orgzly.org;

import com.orgzly.org.OrgContent;
import org.junit.Assert;
import org.junit.Test;

public class OrgContentTest {
    @Test
    public void testTimestamp() {
        OrgContent o = new OrgContent();
        o.append("<2000-01-01 10:10>");
        Assert.assertEquals(1, o.getTimestamps().size());
        Assert.assertEquals("<2000-01-01 10:10>", o.getTimestamps().get(0).toString());
    }

    @Test
    public void testTimestampMultiline() {
        OrgContent o = new OrgContent();
        o.append("<2000-01-01 10:10>\n<2001-01-01 10:10>");
        Assert.assertEquals(2, o.getTimestamps().size());
        Assert.assertEquals("<2000-01-01 10:10>", o.getTimestamps().get(0).toString());
        Assert.assertEquals("<2001-01-01 10:10>", o.getTimestamps().get(1).toString());
    }

    @Test
    public void testReparse() {
        OrgContent o = new OrgContent();
        o.append("<2000-01-01 10:10>");
        o.set("\n\n<2001-01-01 10:10>");
        Assert.assertEquals(1, o.getTimestamps().size());
        Assert.assertEquals("<2001-01-01 10:10>", o.getTimestamps().get(0).toString());
    }

    @Test
    public void testIgnoreInactive() {
        OrgContent o = new OrgContent();
        o.set("[2000-01-01 10:10]");
        Assert.assertEquals(0, o.getTimestamps().size());
    }

    @Test
    public void testEmptyContent() {
        OrgContent o = new OrgContent();
        Assert.assertEquals(0, o.getTimestamps().size());
        Assert.assertFalse(o.hasTimestamps());
    }
}
