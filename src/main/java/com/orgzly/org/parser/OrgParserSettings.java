package com.orgzly.org.parser;

import java.util.HashSet;
import java.util.Set;

public class OrgParserSettings {
    public enum SeparateNotesWithNewLine {
        ALWAYS,
        MULTI_LINE_NOTES_ONLY,
        NEVER
    }

    public SeparateNotesWithNewLine separateNotesWithNewLine;
    public boolean separateHeaderAndContentWithNewLine;

    /** org-property-format */
    String propertyFormat;

    /** org-todo-keywords */
    Set<String> todoKeywords;

    /** org-todo-keywords */
    Set<String> doneKeywords;

    /** The column to align tags to. A positive int left-aligns to the given
      * column; a negative int right-aligns.
      *
      * org-tags-column
      */
    public int tagsColumn;

    /** Support how `org-indent-mode' shifts tags to the left to offset its
      * virtual indentation.
      *
      * org-indent-mode
      */
    public boolean orgIndentMode;

    /** org-indent-indentation-per-level */
    public int orgIndentIndentationPerLevel;

    OrgParserSettings() {
        propertyFormat = "%-10s %s";

        todoKeywords = new HashSet<>();
        doneKeywords = new HashSet<>();

        separateNotesWithNewLine = SeparateNotesWithNewLine.MULTI_LINE_NOTES_ONLY;
        separateHeaderAndContentWithNewLine = true;
        tagsColumn = 0;

        orgIndentMode = false;
        orgIndentIndentationPerLevel = 2;
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
        this.tagsColumn = that.tagsColumn;

        this.orgIndentMode = that.orgIndentMode;
        this.orgIndentIndentationPerLevel = that.orgIndentIndentationPerLevel;
    }

    public static OrgParserSettings getBasic() {
        OrgParserSettings settings = new OrgParserSettings();

        settings.todoKeywords.add("TODO");
        settings.doneKeywords.add("DONE");

        return settings;
    }
}
