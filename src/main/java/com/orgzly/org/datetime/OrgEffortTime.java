package com.orgzly.org.datetime;

/**
 * The estimated amount of time that a user thinks a task will take.
 *
 * Input in H:MM format.
 *
 * Examples: 0:30, 1:00
 */
public class OrgEffortTime {
    private int hours;
    private int minutes;

    public int getTotalMinutes() {
        return hours * 60 + minutes;
    }

    OrgEffortTime(int h, int m) {
        hours = h;
        minutes = m;
    }


    public static OrgEffortTime parse(String string) {
        if (string == null) {
            return null;
        }
        String[] hhmm = string.split(":");
        if (hhmm.length != 2) {
            return null;
        }
        int hours;
        int minutes;
        try {
            hours = hhmm[0].isEmpty() ? 0 : Integer.parseInt(hhmm[0]);
            minutes = Integer.parseInt(hhmm[1]);
        } catch (NumberFormatException e) {
            return null;
        }
        return new OrgEffortTime(hours, minutes);
    }
}
