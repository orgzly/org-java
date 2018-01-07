package com.orgzly.org.logbook;

import com.orgzly.org.logbook.LogbookEntry;

import java.util.LinkedList;

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

    public void addLogToFront(LogbookEntry log) {
        entries.addFirst(log);
    }

    public void addLog(LogbookEntry log) {
        entries.addLast(log);
    }
}
