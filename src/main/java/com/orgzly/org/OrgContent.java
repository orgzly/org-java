package com.orgzly.org;

import com.orgzly.org.datetime.OrgRange;

import java.util.ArrayList;
import java.util.List;

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
     * Content (body). Text after the heading.
     *
     * @return content
     */
    public String toString() {
        return value.toString();
    }

    /**
     * @return {@code false} if there is a text below heading, {@code true} otherwise
     */
    public boolean isEmpty() {
        return value.length() == 0;
    }

    public void set(String value) {
        if (value != null) {
            this.value = new StringBuilder(value);
        }
    }

    public void append(String s) {
        value.append(s);
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

}
