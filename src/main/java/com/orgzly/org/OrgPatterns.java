package com.orgzly.org;

import java.util.regex.Pattern;

/**
 * {@literal <2015-03-13 Fri 13:15 ++1y --2d>---[2015-03-13 金 13:20-16:00 .+1d/2d --1w]}
 */
public class OrgPatterns {
    // org-ts-regexp-both
    private static final String DT = "(([\\[<])[0-9]{4,}-[0-9]{2}-[0-9]{2} ?[^]\r\n>]*?[]>])";

    // org-tsr-regexp-both
    private static final String DT_OR_RANGE = "(" + DT + "(--?-?" + DT + ")?)";

    public static final Pattern DT_OR_RANGE_P = Pattern.compile(OrgPatterns.DT_OR_RANGE);

    //  org-repeat-re
    public static final Pattern REPEAT_P = Pattern.compile(
            "[0-9]{4,}-[0-9][0-9]-[0-9][0-9] [^>\n]*?([.+]?\\+[0-9]+[hdwmy](/[0-9]+[hdwmy])?)");

    public static final Pattern TIME_DELAY_P = Pattern.compile("([-]{1,2}+)([0-9]+)([hdwmy])");

    public static final Pattern REPEATER = Pattern.compile(
            "(([.+]?\\+)([0-9]+)([hdwmy]))(/([0-9]+)([hdwmy]))?");

    // org-ts-regexp0
    public static final Pattern DT_MAYBE_WITH_TIME_P = Pattern.compile(
            "(([0-9]{4,})-([0-9]{2})-([0-9]{2})( +[^]+0-9>\r\n -]+)?( +([0-9]{1,2}):([0-9]{2}))?)");

    /*
     * Time of day with optional end-time.
     * From org-get-compact-tod.
     */
    public static final Pattern TIME_OF_DAY_P = Pattern.compile(
            "(([012]?[0-9]):([0-5][0-9]))(-(([012]?[0-9]):([0-5][0-9])))?");

    public static final Pattern PLANNING_TIMES_P = Pattern.compile(
            "(SCHEDULED:|CLOSED:|DEADLINE:) *" + DT_OR_RANGE);

    public static final Pattern HEAD_P = Pattern.compile("^([*]+)\\s+(.*)\\s*$");
    public static final Pattern HEAD_PRIORITY_P = Pattern.compile("^\\s*\\[#([A-Z])](.*)");
    public static final Pattern HEAD_TAGS_P = Pattern.compile("^(.*)\\s+:(\\S+):\\s*$");

    public static final Pattern PROPERTY = Pattern.compile("^:([^:\\s]+):(\\s+.+|)$");

    /*
    https://orgmode.org/manual/In_002dbuffer-settings.html

    "In-buffer settings start with ‘#+’, followed by a keyword, a colon,
    and then a word for each setting. Org accepts multiple settings on the same line.
    Org also accepts multiple lines for a keyword."

    Here we don't allow multiple settings per line for now.
     */
    public static final Pattern KEYWORD_VALUE = Pattern.compile("^#\\+([A-Za-z0-9_]+):\\s*(.*?)\\s*$");
}
