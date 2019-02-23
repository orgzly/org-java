package com.orgzly.org.datetime;

/**
 * Interval (time) - the duration between two events.
 */
public class OrgInterval {
    public enum Unit {
        HOUR,   //  h
        DAY,    //  d
        WEEK,   //  w
        MONTH,  //  m
        YEAR    //  y
    }

    protected int value;

    protected Unit unit;

    protected OrgInterval() {
    }

    public OrgInterval(String value, String unit) {
        setValue(value);
        setUnit(unit);
    }

    public OrgInterval(int value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public int getValue() {
        return value;
    }

    public void setValue(String str) {
        try {
            if (str.length() > 1 && str.charAt(0) == '+') { // Before Java 7
                value = Integer.valueOf(str.substring(1));
            } else {
                value = Integer.valueOf(str);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Interval value " + str + " couldn't be parsed as integer", e);
        }
    }

    public void setValue(int val) {
        value = val;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(String str) {
        if ("h".equals(str)) {
            unit = Unit.HOUR;

        } else if ("d".equals(str)) {
            unit = Unit.DAY;

        } else if ("w".equals(str)) {
            unit = Unit.WEEK;

        } else if ("m".equals(str)) {
            unit = Unit.MONTH;

        } else if ("y".equals(str)) {
            unit = Unit.YEAR;

        } else {
            throw new IllegalArgumentException("Unknown unit " + str);
        }
    }

    public void setUnit(Unit u) {
        unit = u;
    }

    public String toString() {
       switch (unit) {
           case HOUR:
               return value + "h";
           case DAY:
               return value + "d";
           case WEEK:
               return value + "w";
           case MONTH:
               return value + "m";
           case YEAR:
               return value + "y";
           default:
               throw new IllegalArgumentException("Unknown unit " + unit);
       }
    }
}

