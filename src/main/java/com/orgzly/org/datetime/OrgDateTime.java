package com.orgzly.org.datetime;

import com.orgzly.org.OrgPatterns;
import com.orgzly.org.OrgStringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Org mode timestamp.
 *
 * For example {@literal <2014-05-26>} or {@literal [2014-05-26 Mon 09:15]}.
 *
 * http://orgmode.org/manual/Timestamps.html
 * http://orgmode.org/manual/Repeated-tasks.html
 *
 */
public class OrgDateTime {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd EEE");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    private boolean isActive;

    /*
     * Lazy usage of strings and calendars.
     * TODO: Too confusing? Perhaps remove it and let the user do it, if he wants to?
     */
    private String string;
    private String stringWithoutBrackets;

    private Calendar cal;
    private boolean hasTime;

    private Calendar endCal;

    private OrgRepeater repeater;

    private OrgDelay delay;

    private OrgDateTime() {
    }

    /**
     * Creates instance representing current time.
     *
     * @param isActive {@code true} to create active {@link OrgDateTime}, {@code false} for inactive
     *
     * @return current time
     */
    public static OrgDateTime getInstance(boolean isActive) {
        OrgDateTime time = new OrgDateTime();

        time.isActive = isActive;

        time.cal = GregorianCalendar.getInstance();
        time.cal.set(Calendar.SECOND, 0);
        time.cal.set(Calendar.MILLISECOND, 0);

        time.hasTime = true;

        return time;
    }

    /**
     * Creates instance from the given string
     *
     * @param str Org timestamp such as {@code <2014-05-26> or [2014-05-26 Mon 09:15]}
     *
     * @return instance if the provided string is not empty
     */
    public static OrgDateTime getInstance(String str) {
        if (str == null) {
            throw new IllegalArgumentException("null string cannot be parsed as OrgDateTime");
        }

        if (str.length() == 0) {
            throw new IllegalArgumentException("empty string cannot be parsed as OrgDateTime");
        }

        OrgDateTime time = new OrgDateTime();
        time.string = str;

        return time;
    }

    public static OrgDateTime getInstanceOrNull(String str) {
        if (OrgStringUtils.isEmpty(str)) {
            return null;
        }

        OrgDateTime time = new OrgDateTime();
        time.string = str;

        return time;
    }

    /**
     * Returns {@link Calendar} representing this time.
     *
     * @return {@link Calendar} representation of the time.
     * @throws java.lang.IllegalStateException if time is not set
     */
    public Calendar getCalendar() {
        ensureCalendar();
        return cal;
    }

    public boolean isActive() {
        ensureCalendar();
        return isActive;
    }

    public boolean hasTime() {
        ensureCalendar();
        return hasTime;
    }

    public boolean hasEndTime() {
        ensureCalendar();
        return endCal != null;
    }

    public Calendar getEndCalendar() {
        ensureCalendar();
        return endCal;
    }

    public boolean hasRepeater() {
        ensureCalendar();
        return repeater != null;
    }

    public OrgRepeater getRepeater() {
        ensureCalendar();
        return repeater;
    }

    public boolean hasDelay() {
        ensureCalendar();
        return delay != null;
    }

    public OrgDelay getDelay() {
        ensureCalendar();
        return delay;
    }

    /*
     * Convert from Calendar to String.
     *   <2013-06-15 Sat>
     *   [2013-06-15 Sat 12:32]
     */
    private String fromCalendar(boolean withBrackets) {
        StringBuilder result = new StringBuilder();

        if (withBrackets) {
            result.append(isActive() ? '<' : '[');
        }

        result.append(DATE_FORMAT.format(cal.getTime()));

        if (hasTime) {
            result.append(" ");
            result.append(TIME_FORMAT.format(cal.getTime()));

            if (endCal != null) {
                result.append("-");
                result.append(TIME_FORMAT.format(endCal.getTime()));
            }
        }

        if (hasRepeater()) {
            result.append(" ");
            result.append(repeater);
        }

        if (hasDelay()) {
            result.append(" ");
            result.append(delay);
        }

        if (withBrackets) {
            result.append(isActive() ? '>' : ']');
        }

        return result.toString();
    }


    private void ensureCalendar() {
        if (cal == null) {
            if (string == null) {
                throw new IllegalStateException("Missing string");
            }

            parseString();
        }
    }

    /**
     * Parse {@link OrgDateTime#string} and populate other fields.
     */
    private void parseString() {
        Matcher m;

        cal = Calendar.getInstance();
        endCal = null;

        switch (string.charAt(0)) {
            case '<':
                isActive = true;
                break;

            case '[':
                isActive = false;
                break;

            default:
                throw new IllegalArgumentException("Timestamp \"" + string + "\" must start with < or [");
        }

        m = OrgPatterns.DT_MAYBE_WITH_TIME_P.matcher(string);
        if (! m.find()) {
            matchFailed(string, OrgPatterns.DT_MAYBE_WITH_TIME_P);
        }

        cal.set(Calendar.YEAR, Integer.valueOf(m.group(2)));
        cal.set(Calendar.MONTH, Integer.valueOf(m.group(3)) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(m.group(4)));

        if (! OrgStringUtils.isEmpty(m.group(6))) { // Has time of day.
            parseTimeOfDay(string.substring(m.start(6)));
        } else {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            hasTime = false;
        }

        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        /* Repeater with at-least interval. */
        m = OrgPatterns.REPEAT_P.matcher(string);
        if (m.find()) {
            repeater = OrgRepeater.getInstance(m.group(1));
        }

        /* Delay. */
        m = OrgPatterns.TIME_DELAY_P.matcher(string);
        if (m.find()) {
            delay = OrgDelay.getInstance(m.group(0));
        }
    }


    private void parseTimeOfDay(String str) {
        Matcher m = OrgPatterns.TIME_OF_DAY_P.matcher(str);
        if (! m.find()) {
            matchFailed(str, OrgPatterns.TIME_OF_DAY_P);
        }

        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(m.group(2)));
        cal.set(Calendar.MINUTE, Integer.valueOf(m.group(3)));
        hasTime = true;

        if (! OrgStringUtils.isEmpty(m.group(4))) { // End time exists
            endCal = Calendar.getInstance();
            endCal.setTime(cal.getTime());
            endCal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(m.group(6)));
            endCal.set(Calendar.MINUTE, Integer.valueOf(m.group(7)));
            endCal.set(Calendar.SECOND, 0);
            endCal.set(Calendar.MILLISECOND, 0);
        }
    }

    private void matchFailed(String string, Pattern pattern) {
        throw new IllegalArgumentException("Failed matching \"" + string + "\" against " + pattern);
    }

    public boolean shift(Calendar now) {
        /*
         * Calling the method below will also make sure
         * that string is parsed and calendars are updated.
         */
        if (hasRepeater()) {
            cal = getCalendar();
            endCal = getEndCalendar();

            /* Shift both calendars. */

            repeater.shiftCalendar(cal, now);

            if (endCal != null) {
                repeater.shiftCalendar(endCal, now);
            }

            /* Invalidate string representations. */
            string = null;
            stringWithoutBrackets = null;
        }

        return repeater != null;
    }

    public String toString() {
        if (string == null && cal != null) {
            string = fromCalendar(true);
        }
        return string;
    }

    public String toStringWithoutBrackets() {
        ensureCalendar();

        if (stringWithoutBrackets == null && cal != null) {
            stringWithoutBrackets = fromCalendar(false);
        }

        return stringWithoutBrackets;
    }

    public static class Builder {
        private boolean isActive;

        private boolean hasTime;
        private boolean hasRepeater;

        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute;

        private OrgRepeater repeater;

        public Builder setIsActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder setHasTime(boolean isTimeUsed) {
            this.hasTime = isTimeUsed;
            return this;
        }

        public Builder setHasRepeater(boolean isRepeaterUsed) {
            this.hasRepeater = isRepeaterUsed;
            return this;
        }

        public Builder setYear(int year) {
            this.year = year;
            return this;
        }

        public Builder setMonth(int month) {
            this.month = month;
            return this;
        }

        public Builder setDay(int day) {
            this.day = day;
            return this;
        }

        public Builder setHour(int hour) {
            this.hour = hour;
            return this;
        }

        public Builder setMinute(int minute) {
            this.minute = minute;
            return this;
        }

        public Builder setRepeater(OrgRepeater repeater) {
            this.repeater = repeater;
            return this;
        }

        public OrgDateTime build() {
            OrgDateTime time = new OrgDateTime();

            time.isActive = isActive;

            time.hasTime = hasTime;

            time.cal = new GregorianCalendar(year, month, day, hour, minute);

            if (hasRepeater) {
                time.repeater = repeater;
            }

            return time;
        }
    }
}
