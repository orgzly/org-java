package com.orgzly.org.datetime;

import com.orgzly.org.OrgPatterns;
import com.orgzly.org.OrgStringUtils;

import java.util.Calendar;
import java.util.regex.Matcher;

/**
 * http://orgmode.org/manual/Repeated-tasks.html
 */
public class OrgRepeater extends OrgInterval {
    public enum Type {
        CUMULATE,  //  +
        CATCH_UP,  //  ++
        RESTART    //  .+
    }

    private Type type;

    /** 4d in .+2d/4d (http://orgmode.org/manual/Tracking-your-habits.html) */
    private OrgInterval habitDeadline;


    public static OrgRepeater parse(String str) {
        OrgRepeater repeater = new OrgRepeater();

        Matcher m = OrgPatterns.REPEATER.matcher(str);

        if (m.find()) {
            if (m.groupCount() == 7) {
                repeater.setTypeFromString(m.group(2));
                repeater.setValue(m.group(3));
                repeater.setUnit(m.group(4));

                if (! OrgStringUtils.isEmpty(m.group(6))) {
                    repeater.habitDeadline = new OrgInterval();
                    repeater.habitDeadline.setValue(m.group(6));
                    repeater.habitDeadline.setUnit(m.group(7));
                }

            } else {
                throw new IllegalArgumentException("Expected 7 groups (got " + m.groupCount() +
                                                   ") when matching repeater " + str + " against " + OrgPatterns.REPEATER);
            }

        } else {
            throw new IllegalArgumentException("Failed matching repeater " +
                                               str + " against " + OrgPatterns.REPEATER);
        }

        return repeater;
    }

    private OrgRepeater() {
    }

    public OrgRepeater(Type type, int value, OrgInterval.Unit unit) {
        this.type = type;
        this.value = value;
        this.unit = unit;
    }

    private void setTypeFromString(String str) {
        if ("+".equals(str)) {
            type = Type.CUMULATE;

        } else if ("++".equals(str)) {
            type = Type.CATCH_UP;

        } else if (".+".equals(str)) {
            type = Type.RESTART;

        } else {
            throw new IllegalArgumentException("Unknown repeater type " + str);
        }
    }

    public Type getType() {
        return type;
    }

    public boolean hasHabitDeadline() {
        return habitDeadline != null;
    }

    public OrgInterval getHabitDeadline() {
        return habitDeadline;
    }

    /**
     * Shifts calendar by this repeater interval.
     *
     * @param cal Time to shift
     * @param now Current time
     */
    public void shiftCalendar(Calendar cal, Calendar now) {
        switch (type) {
            case CUMULATE:
                /* Just shift it! */
                shiftByInterval(cal);
                break;

            case CATCH_UP:
                /* Shift at least once, but also until present is behind us.
                 * Simple but potentially slow.
                 */
                do {
                    shiftByInterval(cal);
                } while (! cal.after(now));

                break;

            case RESTART:
                /* Set to today. */
                cal.set(Calendar.YEAR, now.get(Calendar.YEAR));
                cal.set(Calendar.MONTH, now.get(Calendar.MONTH));
                cal.set(Calendar.DATE, now.get(Calendar.DATE));

                /* For hourly repeater, set hour to current hour.
                 * This is just one of the options how it could work -- org-mode doesn't
                 * change the day, it just shifts the hour, which is probably a bug).
                 * See testShiftByIntervalAfterTodayHour().
                 */
                if (getUnit() == Unit.HOUR) {
                    cal.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
                    // cal.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
                }

                shiftByInterval(cal);
                break;
        }
    }

    private void shiftByInterval(Calendar cal) {
        switch (getUnit()) {
            case HOUR:
                cal.add(Calendar.HOUR_OF_DAY, getValue());
                break;
            case DAY:
                cal.add(Calendar.DATE, getValue());
                break;
            case WEEK:
                cal.add(Calendar.WEEK_OF_YEAR, getValue());
                break;
            case MONTH:
                cal.add(Calendar.MONTH, getValue());
                break;
            case YEAR:
                cal.add(Calendar.YEAR, getValue());
                break;
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        switch (type) {
            case CUMULATE:
                s.append("+");
                break;
            case CATCH_UP:
                s.append("++");
                break;
            case RESTART:
                s.append(".+");
                break;
        }

        /* Interval. */
        s.append(super.toString());

        if (habitDeadline != null) {
            s.append("/").append(habitDeadline);
        }

        return s.toString();
    }
}
