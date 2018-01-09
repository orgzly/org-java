package com.orgzly.org;

import com.orgzly.org.datetime.OrgRange;

import com.orgzly.org.logbook.Logbook;
import com.orgzly.org.logbook.LogbookEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Heading with text below it.
 *
 * Does not contain any coordinates (position within the outline), not even a level.
 */
public class OrgHead {
    private String title;

    private List<String> tags;

    private String state;

    private String priority;

    private OrgRange scheduled;
    private OrgRange deadline;
    private OrgRange closed;

    private OrgRange clock; // TODO: Create OrgClock with elapsed time?

    private List<OrgProperty> properties;

    private Logbook logbook;

    private StringBuilder content;

    /**
     * Creates an empty heading.
     */
    public OrgHead() {
        this("");
    }

    public OrgHead(String str) {
        this.title = str;
        this.content = new StringBuilder();
    }

    /**
     * Title.
     *
     * @return title
     */
    public String getTitle() {
        if (title == null) {
            return "";
        } else {
            return title;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Tags.
     *
     * @return list of tags
     */
    public List<String> getTags() {
        if (tags == null) {
            return new ArrayList<>();
        } else {
            return tags;
        }
    }

    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }

    public void setTags(String[] tags) {
        if (tags == null) {
            throw new IllegalArgumentException("Tags passed to setTags cannot be null");
        }

        this.tags = new ArrayList<>();

        /* Only add non-null and non-empty strings. */
        for (String tag: tags) {
            if (!OrgStringUtils.isEmpty(tag)) {
                this.tags.add(tag);
            }
        }
    }

    /**
     * Content (body). Text after the heading.
     *
     * @return content
     */
    public String getContent() {
        return content.toString();
    }

    /**
     * @return {@code true} if there is a text below heading, {@code false} otherwise
     */
    public boolean hasContent() {
        return content.length() > 0;
    }

    public void setContent(String content) {
        if (content != null) {
            this.content = new StringBuilder(content);
        } else {
            this.content = new StringBuilder("");
        }
    }

    public void appendContent(String s) {
        content.append(s);
    }

    /**
     * Scheduled time.
     *
     * @return scheduled time or {@code null} if not set
     */
    public OrgRange getScheduled() {
        if (hasScheduled()) {
            return scheduled;
        }
        return null;
    }

    public boolean hasScheduled() {
        return scheduled != null && scheduled.isSet();
    }

    public void setScheduled(OrgRange time) {
        scheduled = time;
    }

    /**
     * Closed time.
     *
     * @return closed time or {@code null} if not set
     */
    public OrgRange getClosed() {
        if (hasClosed()) {
            return closed;
        }
        return null;
    }

    public boolean hasClosed() {
        return closed != null && closed.isSet();
    }

    public void setClosed(OrgRange time) {
        closed = time;
    }

    /**
     * Deadline time.
     *
     * @return deadline time or {@code null} if not set
     */
    public OrgRange getDeadline() {
        if (hasDeadline()) {
            return deadline;
        }
        return null;
    }

    public boolean hasDeadline() {
        return deadline != null && deadline.isSet();
    }

    public void setDeadline(OrgRange time) {
        deadline = time;
    }

    /**
     * CLOCK time.
     *
     * @return clock time or {@code null} if not set
     */
    public OrgRange getClock() {
        if (hasClock()) {
            return clock;
        }
        return null;
    }

    public boolean hasClock() {
        return clock != null && clock.isSet();
    }

    public void setClock(OrgRange time) {
        clock = time;
    }

    /**
     * Priority.
     *
     * @return priority
     */
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * State.
     *
     * @return state
     */
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    /**
     * Properties.
     *
     * @return list of properties
     */
    public List<OrgProperty> getProperties() {
        if (properties == null) {
            return new ArrayList<>();
        } else {
            return properties;
        }
    }

    public boolean hasProperties() {
        return properties != null && !properties.isEmpty();
    }

    public void addProperty(OrgProperty property) {
        if (properties == null) {
            properties = new ArrayList<>();
        }

        properties.add(property);
    }

    public void setProperties(List<OrgProperty> properties) {
        this.properties = properties;
    }

    public void removeProperties() {
        properties = null;
    }

    // Logbook
    public boolean hasLogbook() {
        return logbook != null;
    }

    public void initLogbook() {
        if (logbook == null) {
            logbook = new Logbook();
        }
    }

    public Logbook getLogbook() {
        return logbook;
    }

    public Logbook addLogbookEntry(LogbookEntry log) {
        initLogbook();
        logbook.addLogbookEntry(log);
        return logbook;
    }

    public Logbook addLogbookEntryToFront(LogbookEntry log) {
        initLogbook();
        logbook.addLogbookEntryToFront(log);
        return logbook;
    }

    public String toString() {
        return title;
    }
}
