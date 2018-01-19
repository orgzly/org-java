package com.orgzly.org;

import com.orgzly.org.datetime.OrgRange;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Text below a heading.
 */
public class OrgContent {
    StringBuilder value;
    private List<OrgRange> timestamps;

    public OrgContent() {
        this.value = new StringBuilder();
    }

    /**
     * @return {@code false} if there is a text below heading, {@code true} otherwise
     */
    public boolean isEmpty() {
        return value.length() == 0;
    }

    public void set(String value) {
        this.value = new StringBuilder(value);
        timestamps = null;
        reparse();
    }

    public void append(String s) {
        value.append(s);
        parseLine(s);
    }

    /**
     * @return the content text
     */
    public String toString() {
        return value.toString();
    }

    /**
     * Plain timestamps.
     */
    public List<OrgRange> getTimestamps() {
        if (timestamps == null) {
            return new ArrayList<>();
        } else {
            return timestamps;
        }
    }

    public boolean hasTimestamps() {
        return timestamps != null && !timestamps.isEmpty();
    }

    public void addTimestamp(OrgRange timestamp) {
        if (timestamps == null) {
            timestamps = new ArrayList<>();
        }

        timestamps.add(timestamp);
    }

    /** Parse all plain timestamps in this line and add them to the timestamps list. */
    public void parseLine(String line) {
        Matcher m = OrgPatterns.DT_OR_RANGE_P.matcher(line);
        while (m.find()) {
            addTimestamp(OrgRange.parse(m.group()));
        }
    }

    /** Parse the whole content to rebuild the timestamps list. */
    public void reparse() {
        String content = toString();
        for (String line: content.split("\n")) {
            parseLine(line);
        }
    }

}
