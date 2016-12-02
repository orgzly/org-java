package com.orgzly.orgjava;

import com.orgzly.orgjava.parser.OrgParser;

public class OrgTestParser {
    /** Basic parser used by tests. */
    protected OrgParser.Builder parserBuilder = new OrgParser.Builder()
            .setTodoKeywords(new String[]{"TODO"})
            .setDoneKeywords(new String[]{"DONE"});
}
