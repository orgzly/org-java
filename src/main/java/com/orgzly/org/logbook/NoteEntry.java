package com.orgzly.org.logbook;

import com.orgzly.org.logbook.LogbookEntry;
import com.orgzly.org.datetime.OrgDateTime;

import java.util.List;

public class NoteEntry extends LogbookEntry {
    OrgDateTime datetime;
    List<String> lines;

    public NoteEntry(String datetime, List<String> lines) {
        this.datetime = OrgDateTime.parse(datetime);
        this.lines = lines;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("- Note taken on " + this.datetime.toString() + " \\\\\n");
        for (String line : lines) {
            builder.append("  " + line + "\n");
        }
        return builder.toString();
    }
}
