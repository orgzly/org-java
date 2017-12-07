package com.orgzly.org;

import org.junit.Test;

import java.util.regex.Matcher;

import static org.junit.Assert.*;

public class OrgPatternsTest {
    @Test
    public void testRepeatNotFound1() {
        String s = "SCHEDULED: <2015-03-13 Fri>";
        Matcher m = OrgPatterns.REPEAT_P.matcher(s);
        assertFalse(m.find());
    }

    @Test
    public void testRepeatNotFound2() {
        String s = "SCHEDULED: <2015-03-13 金>";
        Matcher m = OrgPatterns.REPEAT_P.matcher(s);
        assertFalse(m.find());
    }

    @Test
    public void testRepeat2() {
        String s = "SCHEDULED: <2015-03-13 Fri +1d>";
        Matcher m = OrgPatterns.REPEAT_P.matcher(s);
        assertTrue(m.find());
        assertEquals("+1d", m.group(1));
    }

    @Test
    public void testRepeat3() {
        String s = "SCHEDULED: <2015-03-13 +4w/2d>";
        Matcher m = OrgPatterns.REPEAT_P.matcher(s);
        assertTrue(m.find());
        assertEquals("+4w/2d", m.group(1));
    }

    @Test
    public void testRepeat4() {
        String s = "SCHEDULED: <2015-03-13 20:00-22:00 -2d>";
        Matcher m = OrgPatterns.TIME_DELAY_P.matcher(s);
        assertTrue(m.find());
        assertEquals("-", m.group(1));
        assertEquals("2", m.group(2));
        assertEquals("d", m.group(3));
    }

    @Test
    public void testNoteEntry1() {
        String s = "- Note taken on [2015-02-07 Sat 20:58]";
        Matcher m = OrgPatterns.LOGBOOK_NOTE_P.matcher(s);
        assertFalse(m.find());
    }

    @Test
    public void testNoteEntry2() {
        String s = "- Note taken on [2015-02-07 Sat 20:58] \\\\";
        Matcher m = OrgPatterns.LOGBOOK_NOTE_P.matcher(s);
        assertTrue(m.find());
        assertEquals("[2015-02-07 Sat 20:58]", m.group(1));
    }

    @Test
    public void testClockEntry1() {
        String s = "CLOCK: [2017-09-02 Sat 12:38]--[2017-09-02 Sat 12:54] =>  0:16";
        Matcher m = OrgPatterns.LOGBOOK_CLOCK_P.matcher(s);
        assertTrue(m.find());
        assertEquals("[2017-09-02 Sat 12:38]--[2017-09-02 Sat 12:54]", m.group(1));
    }

    @Test
    public void testClockEntry2() {
        String s = "CLOCK: [2017-09-02 Sat 12:38]";
        Matcher m = OrgPatterns.LOGBOOK_CLOCK_P.matcher(s);
        assertTrue(m.find());
        assertEquals("[2017-09-02 Sat 12:38]", m.group(1));
    }
}
