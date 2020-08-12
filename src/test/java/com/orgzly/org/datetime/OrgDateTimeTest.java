package com.orgzly.org.datetime;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class OrgDateTimeTest {
    /* This test is kinda useless because string is kept and not parsed and then just returned. */
    @Test
    public void testToStringFromString() {
        Assert.assertEquals("<2014-05-26 Mon>", OrgDateTime.parse("<2014-05-26 Mon>").toString());
        Assert.assertEquals("[2014-05-26 Mon]", OrgDateTime.parse("[2014-05-26 Mon]").toString());
        Assert.assertEquals("<2014-05-26 Mon 09:15>", OrgDateTime.parse("<2014-05-26 Mon 09:15>").toString());
        Assert.assertEquals("[2014-05-26]", OrgDateTime.parse("[2014-05-26]").toString());
        Assert.assertEquals("<2000-01-01>--<2000-02-02>", OrgDateTime.parse("<2000-01-01>--<2000-02-02>").toString());
    }

    @Test
    public void testIsWithTime() {
        OrgDateTime time = OrgDateTime.parse("<2014-05-26 Mon 09:15>");
        Assert.assertTrue(time.hasTime());
    }

    @Test
    public void testIsNotWithTime() {
        OrgDateTime time = OrgDateTime.parse("<2014-05-26 Mon>");
        Assert.assertFalse(time.hasTime());
    }

    @Test
    public void testGetCalendar() {
        OrgDateTime time = OrgDateTime.parse("<2014-05-26>");

        Assert.assertEquals(2014, time.getCalendar().get(Calendar.YEAR));
        Assert.assertEquals(4, time.getCalendar().get(Calendar.MONTH));
        Assert.assertEquals(26, time.getCalendar().get(Calendar.DATE));
    }

    @Test
    public void testGetCalendarRange() {
        OrgDateTime time = OrgDateTime.parse("<2000-01-01>--<2000-02-02>");

        Assert.assertEquals(2000, time.getCalendar().get(Calendar.YEAR));
        Assert.assertEquals(0, time.getCalendar().get(Calendar.MONTH));
        Assert.assertEquals(1, time.getCalendar().get(Calendar.DATE));
    }

    @Test
    public void testSingleDigitHour() {
        OrgDateTime time = OrgDateTime.parse("[2014-06-03 Tue 3:34]");

        Assert.assertTrue(time.hasTime());
        Assert.assertEquals(3, time.getCalendar().get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void testIsActive() {
        OrgDateTime time = OrgDateTime.parse("<2014-05-26 Mon>");
        Assert.assertTrue(time.isActive());
    }

    @Test
    public void testIsNotActive() {
        OrgDateTime time = OrgDateTime.parse("[2014-05-26 Mon]");
        Assert.assertFalse(time.isActive());
    }

    @Test
    public void testNoWeekDay() {
        OrgDateTime time = OrgDateTime.parse("<2014-05-26 13:15>");
        Assert.assertTrue(time.isActive());
        Assert.assertTrue(time.hasTime());
        Assert.assertEquals(26, time.getCalendar().get(Calendar.DATE));
        Assert.assertEquals(13, time.getCalendar().get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void testSingleCharacterWeekDay() {
        OrgDateTime time = OrgDateTime.parse("<2015-03-13 金>");
        Assert.assertTrue(time.isActive());
        Assert.assertFalse(time.hasTime());
        Assert.assertEquals(2015, time.getCalendar().get(Calendar.YEAR));
        Assert.assertEquals(2, time.getCalendar().get(Calendar.MONTH));
        Assert.assertEquals(13, time.getCalendar().get(Calendar.DATE));
    }

    @Test
    public void testSingleCharacterWeekDayWithTime() {
        OrgDateTime time = OrgDateTime.parse("<2015-03-13 金 14:16>");
        Assert.assertTrue(time.isActive());
        Assert.assertTrue(time.hasTime());
        Assert.assertEquals("Year", 2015, time.getCalendar().get(Calendar.YEAR));
        Assert.assertEquals("Month", 2, time.getCalendar().get(Calendar.MONTH));
        Assert.assertEquals("Date", 13, time.getCalendar().get(Calendar.DATE));
        Assert.assertEquals("Hour", 14, time.getCalendar().get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals("Minute", 16, time.getCalendar().get(Calendar.MINUTE));
    }

    @Test
    public void testRepeaterEvery() {
        OrgDateTime time = OrgDateTime.parse("<2014-05-26 +4w>");
        Assert.assertEquals(4, time.getRepeater().getValue());
        Assert.assertEquals(OrgRepeater.Type.CUMULATE, time.getRepeater().getType());
        Assert.assertEquals(OrgRepeater.Unit.WEEK, time.getRepeater().getUnit());
    }

    @Test
    public void testRepeaterInFuture() {
        OrgDateTime time = OrgDateTime.parse("<2014-05-26 ++4w>");
        Assert.assertEquals(4, time.getRepeater().getValue());
        Assert.assertEquals(OrgRepeater.Type.CATCH_UP, time.getRepeater().getType());
        Assert.assertEquals(OrgRepeater.Unit.WEEK, time.getRepeater().getUnit());
    }

    @Test
    public void testRepeaterInAfterToday() {
        OrgDateTime time = OrgDateTime.parse("<2014-05-26 .+4w>");
        Assert.assertEquals(4, time.getRepeater().getValue());
        Assert.assertEquals(OrgRepeater.Type.RESTART, time.getRepeater().getType());
        Assert.assertEquals(OrgRepeater.Unit.WEEK, time.getRepeater().getUnit());
    }

    @Test
    public void testRepeaterWithoutWeekDay() {
        OrgDateTime time = OrgDateTime.parse("<2009-10-17 13:15 .+2d/4d>");

        Assert.assertTrue(time.isActive());
        Assert.assertTrue(time.hasTime());

        Assert.assertTrue(time.hasRepeater());
        Assert.assertEquals(2, time.getRepeater().getValue());
        Assert.assertEquals(OrgRepeater.Type.RESTART, time.getRepeater().getType());
        Assert.assertEquals(OrgRepeater.Unit.DAY, time.getRepeater().getUnit());

        Assert.assertTrue(time.getRepeater().hasHabitDeadline());
    }

    @Test
    public void testRepeaterWithHabitDeadline() {
        OrgDateTime time = OrgDateTime.parse("<2009-10-17 Sat 13:15 .+2d/4d>");

        Assert.assertTrue(time.isActive());
        Assert.assertTrue(time.hasTime());
        Assert.assertEquals(13, time.getCalendar().get(Calendar.HOUR_OF_DAY));

        Assert.assertTrue(time.hasRepeater());
        Assert.assertEquals(2, time.getRepeater().getValue());
        Assert.assertEquals(OrgRepeater.Type.RESTART, time.getRepeater().getType());
        Assert.assertEquals(OrgRepeater.Unit.DAY, time.getRepeater().getUnit());

        Assert.assertTrue(time.getRepeater().hasHabitDeadline());
        Assert.assertEquals(OrgRepeater.Unit.DAY, time.getRepeater().getHabitDeadline().getUnit());
        Assert.assertEquals(4, time.getRepeater().getHabitDeadline().getValue());
    }

    @Test
    public void testShiftTimeWithHabitDeadline() {
        OrgDateTime time = OrgDateTime.parse("<2009-10-17 Sat 13:15 +2d/4d>");
        time.shift(Calendar.getInstance());
        Assert.assertEquals("<2009-10-19 Mon 13:15 +2d/4d>", time.toString());
    }

    @Test
    public void testDelayOnlyWithTime() {
        OrgDateTime time = OrgDateTime.parse("<2009-10-17 13:15 -2d>");
        Assert.assertFalse(time.hasRepeater());
        Assert.assertTrue(time.hasDelay());
        Assert.assertEquals(OrgDelay.Type.ALL, time.getDelay().getType());
        Assert.assertEquals(2, time.getDelay().getValue());
        Assert.assertEquals(OrgDelay.Unit.DAY, time.getDelay().getUnit());
    }

    @Test
    public void testDelayOnlyWithWeekday() {
        OrgDateTime time = OrgDateTime.parse("<2009-10-17 Wed -2d>");
        Assert.assertFalse(time.hasRepeater());
        Assert.assertTrue(time.hasDelay());
        Assert.assertEquals(OrgDelay.Type.ALL, time.getDelay().getType());
        Assert.assertEquals(2, time.getDelay().getValue());
        Assert.assertEquals(OrgDelay.Unit.DAY, time.getDelay().getUnit());
    }

    @Test
    public void testDelayFirstOccurrence() {
        OrgDateTime time = OrgDateTime.parse("<2009-10-17 13:15 +1m --2d>");
        Assert.assertTrue(time.hasRepeater());
        Assert.assertTrue(time.hasDelay());
        Assert.assertEquals(OrgDelay.Type.FIRST_ONLY, time.getDelay().getType());
        Assert.assertEquals(2, time.getDelay().getValue());
        Assert.assertEquals(OrgDelay.Unit.DAY, time.getDelay().getUnit());
    }

    @Test
    public void testInvalidNoBrackets() {
        try {
            OrgDateTime time = OrgDateTime.parse("2014-05-26 Mon");
            Assert.assertFalse(time.hasTime());

            Assert.fail("Parsing time with no brackets should fail");

        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Timestamp \"2014-05-26 Mon\" must start with < or [", e.getMessage());
        }
    }

    @Test
    public void testDoParse() {
        OrgDateTime time = OrgDateTime.doParse("[2014-05-26 Mon]");
        Assert.assertNotNull(time);
    }

    @Test
    public void testDoParseInvalid() {
        OrgDateTime time = OrgDateTime.doParse("2014-05-26 Mon");
        Assert.assertNull(time);
    }

    @Test
    public void testEndTime() {
        OrgDateTime time = OrgDateTime.parse("<2009-10-17 13:15-14:30>");

        Assert.assertTrue(time.hasTime());
        Assert.assertEquals(13, time.getCalendar().get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(15, time.getCalendar().get(Calendar.MINUTE));
        Assert.assertEquals(0, time.getCalendar().get(Calendar.SECOND));
        Assert.assertEquals(0, time.getCalendar().get(Calendar.MILLISECOND));

        Assert.assertTrue(time.hasEndTime());
        Assert.assertEquals(14, time.getEndCalendar().get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals(30, time.getEndCalendar().get(Calendar.MINUTE));
        Assert.assertEquals(0, time.getEndCalendar().get(Calendar.SECOND));
        Assert.assertEquals(0, time.getEndCalendar().get(Calendar.MILLISECOND));
    }

    /*
     * Generating string.
     *
     * toStringWithoutBrackets required Calendar, so first original string will be parsed and
     * then string for toStringWithoutBrackets will be generated.
     *
     * If that wasn't the case, orginal string would just be returned and no generation would occur.
     */

    @Test
    public void testGeneratedString1() {
        OrgDateTime time = OrgDateTime.parse("<2009-10-17 13:15 .+1m>");
        Assert.assertEquals("2009-10-17 Sat 13:15 .+1m", time.toStringWithoutBrackets());
    }

    @Test
    public void testGeneratedString2() {
        OrgDateTime time = OrgDateTime.parse("<2009-10-17 13:15-14:30 ++1y --2d>");
        Assert.assertEquals("2009-10-17 Sat 13:15-14:30 ++1y --2d", time.toStringWithoutBrackets());
    }

    /*
     * From calendar to string.
     */

    @Test
    public void testToStringFromDate() {
        OrgDateTime timestamp = new OrgDateTime.Builder()
                .setIsActive(true)
                .setYear(2014)
                .setMonth(4)
                .setDay(26)
                .build();

        Assert.assertEquals("<2014-05-26 Mon>", timestamp.toString());
    }

    @Test
    public void testFromCalendarTimeToString() {
        OrgDateTime timestamp = new OrgDateTime.Builder()
                .setIsActive(false)
                .setHasTime(true)
                .setYear(2014)
                .setMonth(4)
                .setDay(26)
                .setHour(9)
                .setMinute(15)
                .build();

        Assert.assertEquals("[2014-05-26 Mon 09:15]", timestamp.toString());
    }

    @Test
    public void testFromCalendarTimeToStringWithoutBrackets() {
        OrgDateTime timestamp = new OrgDateTime.Builder()
                .setIsActive(false)
                .setHasTime(true)
                .setYear(2014)
                .setMonth(4)
                .setDay(26)
                .setHour(9)
                .setMinute(15)
                .build();

        Assert.assertEquals("2014-05-26 Mon 09:15", timestamp.toStringWithoutBrackets());
    }

    @Test
    public void testToStringFromPmTime() {
        OrgDateTime timestamp = new OrgDateTime.Builder()
                .setIsActive(true)
                .setHasTime(true)
                .setYear(2014)
                .setMonth(4)
                .setDay(26)
                .setHour(15)
                .setMinute(15)
                .build();

        Assert.assertEquals("<2014-05-26 Mon 15:15>", timestamp.toString());
    }

    @Test
    public void testFromCalendarInactive() {
        OrgDateTime timestamp = new OrgDateTime.Builder()
                .setYear(2001)
                .setMonth(1)
                .setDay(1)
                .build();

        Assert.assertEquals("[2001-02-01 Thu]", timestamp.toString());
    }

    @Test
    public void testFromCalendarActive() {
        OrgDateTime timestamp = new OrgDateTime.Builder()
                .setIsActive(true)
                .setYear(2001)
                .setMonth(1)
                .setDay(1)
                .build();

        Assert.assertEquals("<2001-02-01 Thu>", timestamp.toString());
    }

    @Test
    public void testBuilderClone() {
        String s = "<2020-01-01 Wed 15:00-16:00 .+1w -2d>";
        Assert.assertEquals(s, new OrgDateTime.Builder(OrgDateTime.parse(s)).build().toString());
    }
}
