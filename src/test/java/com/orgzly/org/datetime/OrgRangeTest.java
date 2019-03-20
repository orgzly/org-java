package com.orgzly.org.datetime;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class OrgRangeTest {
    @Test
    public void testGetInstanceForNullString() {
        try {
            OrgRange.parse(null);
            Assert.fail("Parsing null String must throw exception");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().equals("OrgRange cannot be created from null string"));
        }
    }

    @Test
    public void testGetInstanceForNullOrgDateTime() {
        try {
            new OrgRange((OrgDateTime) null);
            Assert.fail("Parsing null OrgDateTime must throw exception");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().equals("OrgRange cannot be created from null OrgDateTime"));
        }
    }

    @Test
    public void testGetInstanceForEmptyString() {
        try {
            OrgRange.parse("");
            Assert.fail("Parsing empty string must throw exception");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().equals("OrgRange cannot be created from null string"));
        }
    }

    @Test
    public void testFromString1() {
        OrgRange time = OrgRange.parse("[2000-01-01]--<2000-02-02 10:20>");

        Assert.assertNotNull(time);
        Assert.assertNotNull(time.getStartTime());
        Assert.assertNotNull(time.getEndTime());

        Assert.assertEquals("[2000-01-01]", time.getStartTime().toString());
        Assert.assertFalse(time.getStartTime().isActive());

        Assert.assertEquals("<2000-02-02 10:20>", time.getEndTime().toString());
        Assert.assertTrue(time.getEndTime().isActive());

        Assert.assertEquals("[2000-01-01]--<2000-02-02 10:20>", time.toString());
    }

    @Test
    public void testFromString2() {
        OrgRange time = OrgRange.parse("[2000-01-01 00:12 .+1h]--<2000-02-02 Mon +1d>");

        Assert.assertNotNull(time);
        Assert.assertNotNull(time.getStartTime());
        Assert.assertNotNull(time.getEndTime());

        Assert.assertNotNull(time.getStartTime());
        Assert.assertNotNull(time.getEndTime());

        Assert.assertEquals("[2000-01-01 00:12 .+1h]", time.getStartTime().toString());
        Assert.assertFalse(time.getStartTime().isActive());
        Assert.assertEquals(2000, time.getStartTime().getCalendar().get(Calendar.YEAR));
        Assert.assertEquals(0, time.getStartTime().getCalendar().get(Calendar.MONTH));
        Assert.assertEquals(1, time.getStartTime().getCalendar().get(Calendar.DATE));
        Assert.assertEquals(0, time.getStartTime().getCalendar().get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(12, time.getStartTime().getCalendar().get(Calendar.MINUTE));

        Assert.assertEquals("<2000-02-02 Mon +1d>", time.getEndTime().toString());
        Assert.assertTrue(time.getEndTime().isActive());
    }

    @Test
    public void testRangeWithOneDash() {
        OrgRange time = OrgRange.parse("[2000-01-01 00:12 .+1h]-<2000-02-02 Mon +1d>");
        Assert.assertNotNull(time.getStartTime());
        Assert.assertNotNull(time.getEndTime());
    }

    @Test
    public void testRangeWith3Dashes() {
        OrgRange time = OrgRange.parse("[2000-01-01 00:12 .+1h]---<2000-02-02 Mon +1d>");
        Assert.assertNotNull(time.getStartTime());
        Assert.assertNotNull(time.getEndTime());
    }

    @Test
    public void testRangeWithRepeaterAndHabitDeadline() {
        OrgRange time = OrgRange.parse("<2015-01-11 Sun .+1d/2d>");
        Assert.assertNotNull(time.getStartTime());
        Assert.assertNull(time.getEndTime());
    }

    @Test
    public void testInvalidDateTimeWithNoSpacing() {
        OrgRange time = OrgRange.parse("[2011-08-1819:12]");

        Assert.assertNotNull(time.getStartTime());
        Assert.assertNull(time.getEndTime());

        Assert.assertEquals(2011, time.getStartTime().getCalendar().get(Calendar.YEAR));
        Assert.assertEquals(7, time.getStartTime().getCalendar().get(Calendar.MONTH));
        Assert.assertEquals(18, time.getStartTime().getCalendar().get(Calendar.DATE));
    }

    @Test
    public void testEndTimes() {
        OrgRange time = OrgRange.parse("<2015-01-13 уто 13:00-14:14>--<2015-01-14 сре 14:10-15:20>");

        Assert.assertNotNull(time.getStartTime());
        Assert.assertNotNull(time.getEndTime());

        Assert.assertEquals(14, time.getStartTime().getEndCalendar().get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(14, time.getStartTime().getEndCalendar().get(Calendar.MINUTE));

        Assert.assertEquals(15, time.getEndTime().getEndCalendar().get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(20, time.getEndTime().getEndCalendar().get(Calendar.MINUTE));
    }

    @Test
    public void testJeuWeekDay() {
        OrgRange time = OrgRange.parse("[2011-08-18 jeu. 19:12]");

        Assert.assertNotNull(time.getStartTime());
        Assert.assertNull(time.getEndTime());

        Assert.assertEquals(2011, time.getStartTime().getCalendar().get(Calendar.YEAR));
        Assert.assertEquals(7, time.getStartTime().getCalendar().get(Calendar.MONTH));
        Assert.assertEquals(18, time.getStartTime().getCalendar().get(Calendar.DATE));
        Assert.assertEquals(19, time.getStartTime().getCalendar().get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(12, time.getStartTime().getCalendar().get(Calendar.MINUTE));
    }

    @Test
    public void testGeneratedString1() {
        OrgRange range = OrgRange.parse("<2009-10-17 13:15-14:30 ++1y --2d>--<2015-01-18>");
        Assert.assertEquals("2009-10-17 Sat 13:15-14:30 ++1y --2d--2015-01-18 Sun", range.toStringWithoutBrackets());
    }

    @Test
    public void testLargeYear() {
        OrgRange.parse("<10016-04-03 Sun ++100y>");
    }

    @Test
    public void testDoParse() {
        OrgRange time = OrgRange.doParse("[2014-05-26 Mon]");
        Assert.assertNotNull(time);
    }

    @Test
    public void testDoParseInvalid() {
        OrgRange time = OrgRange.doParse("2014-05-26 Mon");
        Assert.assertNull(time);
    }
}
