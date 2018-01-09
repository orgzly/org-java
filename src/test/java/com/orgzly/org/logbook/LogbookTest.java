package com.orgzly.org.logbook;

import org.junit.Assert;
import org.junit.Test;

import com.orgzly.org.logbook.Logbook;
import com.orgzly.org.logbook.GenericLogbookEntry;

public class LogbookTest {
    @Test
    public void testAddLogbookEntry() {
        GenericLogbookEntry first = new GenericLogbookEntry("first");
        GenericLogbookEntry second = new GenericLogbookEntry("second");
        Logbook logbook = new Logbook();
        logbook.addLogbookEntry(first);
        logbook.addLogbookEntry(second);
        Assert.assertEquals(logbook.getEntries().get(0), first);
        Assert.assertEquals(logbook.getEntries().get(1), second);
    }

    public void testAddLogbookEntryToFront() {
        GenericLogbookEntry first = new GenericLogbookEntry("first");
        GenericLogbookEntry second = new GenericLogbookEntry("second");
        Logbook logbook = new Logbook();
        logbook.addLogbookEntry(second);
        logbook.addLogbookEntryToFront(first);
        Assert.assertEquals(logbook.getEntries().get(0), first);
        Assert.assertEquals(logbook.getEntries().get(1), second);
    }

    public void testLogbookToString() {
        GenericLogbookEntry first = new GenericLogbookEntry("first line");
        GenericLogbookEntry second = new GenericLogbookEntry("second line");
        Logbook logbook = new Logbook();
        logbook.addLogbookEntry(first);
        logbook.addLogbookEntry(second);
        String expectedString = ":LOGBOOK:\n"
            + "second line\n"
            + "first line\n"
            + ":END:";
        Assert.assertEquals(logbook.toString(), expectedString);
    }
}
