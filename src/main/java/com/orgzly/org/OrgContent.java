package com.orgzly.org;

import com.orgzly.org.datetime.OrgRange;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Text below a heading.
 */
public class OrgContent {
    private StringBuilder value;
    private List<OrgRange> timestamps;
    private boolean dirty; /** Whether timestamps are out of sync with `value`,
                            * and need to be reparsed. */

    public OrgContent() {
        value = new StringBuilder();
        dirty = false;
    }

    /**
     * @return {@code false} if there is a text below heading, {@code true}
     * otherwise
     */
    public boolean isEmpty() {
        return value.length() == 0;
    }

    public void set(String value) {
        if (value == null) {
            this.value = new StringBuilder();
        } else {
            this.value = new StringBuilder(value);
        }
        dirty = true;
    }

    public void append(String s) {
        value.append(s);
        dirty = true;
    }

    /**
     * @return the content text
     */
    public String toString() {
        return value.toString();
    }

    /**
     * @return the list of plain timestamps in this content text
     */
    public List<OrgRange> getTimestamps() {
        if (timestamps == null) {
            timestamps = new ArrayList<>();
        }
        if (dirty) {
            reparse();
        }
        return timestamps;
    }

    public boolean hasTimestamps() {
        if (timestamps == null) {
            return false;
        }
        if (dirty) {
            reparse();
        }
        return !timestamps.isEmpty();
    }

    /** Parse all plain timestamps in this content and rebuild the timestamps list. */
    private void reparse() {
        timestamps.clear();
        Matcher m = OrgPatterns.DT_OR_RANGE_P.matcher(toString());
        while (m.find()) {
            OrgRange range = OrgRange.parse(m.group());
            if (range.getStartTime().isActive()) {
                timestamps.add(range);
            }
        }
        dirty = false;
    }

}
