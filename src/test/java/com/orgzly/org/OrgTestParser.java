package com.orgzly.org;

import com.orgzly.org.parser.OrgParser;

public class OrgTestParser {
    /** Basic parser used by tests. */
    protected OrgParser.Builder parserBuilder = new OrgParser.Builder()
            .setTodoKeywords(new String[]{"TODO"})
            .setDoneKeywords(new String[]{"DONE"});
}
