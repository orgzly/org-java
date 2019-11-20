package com.orgzly.org.datetime;

import com.orgzly.org.OrgPatterns;

import java.util.regex.Matcher;

/**
 * Delay for scheduled time.
 *
 * FIXME: Also used as a warning period for deadline time.
 *
 * http://orgmode.org/manual/Deadlines-and-scheduling.html
 */
public class OrgDelay extends OrgInterval {
    public enum Type {
        ALL,         //  -
        FIRST_ONLY   //  --
    }

    private Type type;

    public static OrgDelay parse(String str) {
        OrgDelay delay = new OrgDelay();

        Matcher m = OrgPatterns.TIME_DELAY_P.matcher(str);

        if (m.find()) {
            if (m.groupCount() == 3) {
                delay.setTypeFromString(m.group(1));
                delay.setValue(m.group(2));
                delay.setUnit(m.group(3));

            } else {
                throw new IllegalArgumentException("Expected 3 groups (got " + m.groupCount() + ") when matching time delay " + str + " against " + OrgPatterns.TIME_DELAY_P);
            }

        } else {
            throw new IllegalArgumentException("Failed matching time delay " + str + " against " + OrgPatterns.TIME_DELAY_P);
        }

        return delay;
    }

    private OrgDelay() {
    }

    public OrgDelay(Type type, int value, Unit unit) {
        this.type = type;
        this.value = value;
        this.unit = unit;
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
