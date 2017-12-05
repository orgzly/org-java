package com.orgzly.org.logbook;

import com.orgzly.org.logbook.LogbookEntry;

public class GenericLogbookEntry extends LogbookEntry {
    String line;

    public GenericLogbookEntry(String line) {
        this.line = line;
    }

    public String toString() {
        return this.line;
    }
}
