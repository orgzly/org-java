package com.orgzly.org;

/**
 * Text below a heading.
 */
public class OrgContent {
    private StringBuilder value;

    public OrgContent() {
        value = new StringBuilder();
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
    }

    public void append(String s) {
        value.append(s);
    }

    /**
     * @return the content text
     */
    public String toString() {
        return value.toString();
    }
}
