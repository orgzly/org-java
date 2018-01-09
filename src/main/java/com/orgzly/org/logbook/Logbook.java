package com.orgzly.org.logbook;

import com.orgzly.org.logbook.LogbookEntry;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * The internal representation for a LOGBOOK drawer.
 *
 * Logbook entries are stored within a linked list, in chronological order.
 * That is, more recent entries are located at the tail of the list.
 *
 * This is the opposite of how Org handles logbook entries, where
 * more recent entries are at the start of the drawer. Hence, when
 * converting the LOGBOOK into its textual representation,
 * it is important to reverse the order.
 *
 */
public class Logbook {
    LinkedList<LogbookEntry> entries;

    public Logbook() {
        entries = new LinkedList<LogbookEntry>();
    }

    public LinkedList<LogbookEntry> getEntries() {
        return entries;
    }

    public void addLogbookEntryToFront(LogbookEntry log) {
        entries.addFirst(log);
    }

    public void addLogbookEntry(LogbookEntry log) {
        entries.addLast(log);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(":LOGBOOK:");
        Iterator<LogbookEntry> reverseIterator = entries.descendingIterator();
        while (reverseIterator.hasNext()) {
            LogbookEntry log = reverseIterator.next();
            String[] lines = log.toString().split("\\r?\\n");
            for (String line : lines) {
                s.append("\n");
                s.append(line);
            }
        }
        s.append("\n:END:");
        return s.toString();
    }
}
