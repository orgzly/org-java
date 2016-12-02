package com.orgzly.orgjava.datetime;

import com.orgzly.orgjava.OrgPatterns;
import com.orgzly.orgjava.OrgStringUtils;

import java.util.Calendar;
import java.util.regex.Matcher;

/**
 * Org mode range.
 *
 * {@link #endTime} can be {@code null}.
 *
 * For example <2004-08-23 Mon> or <2004-08-23 Mon>--<2004-08-26 Thu>.
 *
 */
public class OrgRange {
    private OrgDateTime startTime;
    private OrgDateTime endTime;

    public static OrgRange getInstance(OrgDateTime fromTime) {
        return getInstance(fromTime, null);
    }

    public static OrgRange getInstance(OrgDateTime fromTime, OrgDateTime endTime) {
        if (fromTime == null) {
            throw new IllegalArgumentException("null from OrgDateTime cannot be used for OrgRange");
        }

        OrgRange t = new OrgRange();

        t.startTime = fromTime;
        t.endTime = endTime;

        return t;
    }

    public static OrgRange getInstanceOrNull(String str) {
        if (OrgStringUtils.isEmpty(str)) {
            return null;
        }

        return getInstance(str);
    }


    public static OrgRange getInstance(String str) {
        if (str == null) {
            throw new IllegalArgumentException("null string cannot be parsed as OrgRange");
        }

        if (str.length() == 0) {
            throw new IllegalArgumentException("empty string cannot be parsed as OrgRange");
        }

        OrgRange t = new OrgRange();

        Matcher m = OrgPatterns.DT_OR_RANGE_P.matcher(str);

        if (m.find()) {
//            for (int i = 0; i < m.groupCount() + 1; i++) {
//                System.out.println("group(" + i + ") " + m.group(i));
//            }

            if (m.groupCount() == 6 && m.group(6) != null) { // Range - two timestamps
                t.startTime = OrgDateTime.getInstance(m.group(2));
                t.endTime = OrgDateTime.getInstance(m.group(5));

            } else { // Single timestamp
                t.startTime = OrgDateTime.getInstance(m.group(2));
                t.endTime = null;
            }

            return t;

        } else {
            throw new IllegalArgumentException(
                    "string " + str +
                    " cannot be parsed as OrgRange using pattern " + OrgPatterns.DT_OR_RANGE_P);
        }
    }

    private OrgRange() {
    }

    public OrgDateTime getStartTime() {
        return startTime;
    }

    public OrgDateTime getEndTime() {
        return endTime;
    }

    public boolean isSet() {
        return startTime != null;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(startTime);

        if (endTime != null) {
            s.append("--");
            s.append(endTime);
        }

        return s.toString();
    }

    public String toStringWithoutBrackets() {
        StringBuilder s = new StringBuilder();

        s.append(startTime.toStringWithoutBrackets());

        if (endTime != null) {
            s.append("--");
            s.append(endTime.toStringWithoutBrackets());
        }

        return s.toString();
    }

    public boolean shift() {
        return shift(Calendar.getInstance());
    }

    /**
     * Shifts both timestamps by their repeater intervals.
     */
    public boolean shift(Calendar now) {
        boolean shifted = false;

        if (startTime != null) {
            if (startTime.shift(now)) {
                shifted = true;
            }
        }

        if (endTime != null) {
            if (endTime.shift(now)) {
                shifted = true;
            }
        }

        return shifted;
    }
}
