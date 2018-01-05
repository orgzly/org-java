package com.orgzly.org.logbook;

import com.orgzly.org.logbook.LogbookEntry;

import java.util.LinkedList;

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
