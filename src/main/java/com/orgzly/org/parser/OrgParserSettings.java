package com.orgzly.org.parser;

import java.util.HashSet;
import java.util.Set;

public class OrgParserSettings {
    public enum SeparateNotesWithNewLine {
        ALWAYS,
        MULTI_LINE_NOTES_ONLY,
        NEVER
    }

    String propertyFormat;

    Set<String> todoKeywords;
    Set<String> doneKeywords;

    public SeparateNotesWithNewLine separateNotesWithNewLine;

    OrgParserSettings() {
        propertyFormat = "%-10s %s";

        todoKeywords = new HashSet<>();
        doneKeywords = new HashSet<>();

        separateNotesWithNewLine = SeparateNotesWithNewLine.MULTI_LINE_NOTES_ONLY;
    }

    /**
     * Copy settings.
     */
    OrgParserSettings(OrgParserSettings that) {
        this();

        this.propertyFormat = that.propertyFormat;

        this.todoKeywords.addAll(that.todoKeywords);
        this.doneKeywords.addAll(that.doneKeywords);

        this.separateNotesWithNewLine = that.separateNotesWithNewLine;
    }

    public static OrgParserSettings getBasic() {
        OrgParserSettings settings = new OrgParserSettings();

        settings.todoKeywords.add("TODO");
        settings.doneKeywords.add("DONE");

        return settings;
    }
}
