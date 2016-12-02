package com.orgzly.orgjava.datetime;

import com.orgzly.orgjava.OrgPatterns;

import java.util.regex.Matcher;

/**
 *
 */
public class OrgDelay extends OrgInterval {
    public enum Type {
        ALL,         //  -
        FIRST_ONLY   //  --
    }

    private Type type;

    public static OrgDelay getInstance(String str) {
        return new OrgDelay(str);
    }

    public OrgDelay(String str) {
        Matcher m = OrgPatterns.TIME_DELAY_P.matcher(str);

        if (m.find()) {
            if (m.groupCount() == 3) {
                setTypeFromString(m.group(1));
                setValue(m.group(2));
                setUnit(m.group(3));

            } else {
                throw new IllegalArgumentException("Expected 3 groups (got " + m.groupCount() + ") when matching time delay " + str + " against " + OrgPatterns.TIME_DELAY_P);
            }

        } else {
            throw new IllegalArgumentException("Failed matching time delay " + str + " against " + OrgPatterns.TIME_DELAY_P);
        }
    }

    private void setTypeFromString(String str) {
        if ("-".equals(str)) {
            type = Type.ALL;

        } else if ("--".equals(str)) {
            type = Type.FIRST_ONLY;

        } else {
            throw new IllegalArgumentException("Unknown time delay type " + str);
        }
    }

    public Type getType() {
        return type;
    }

    public String toString() {
        switch (type) {
            case ALL:
                return "-" + super.toString();
            case FIRST_ONLY:
                return "--" + super.toString();
            default:
                throw new IllegalArgumentException("Unknown time delay type " + type);
        }
    }
}
