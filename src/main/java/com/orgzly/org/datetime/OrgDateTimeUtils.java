package com.orgzly.org.datetime;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;

import java.util.ArrayList;
import java.util.List;

public class OrgDateTimeUtils {
    private static final int MAX_INSTANTS_IN_INTERVAL = 100;

    /**
     * @param orgDateTime {@link OrgDateTime}
     * @param fromTime Inclusive
     * @param beforeTime Exclusive. Can be null in which case limit has to be specified
     * @param limit When {@code orgTime} has a repeater, limit the number of results to this number
     * @return List of times within specified interval
     */
    public static List<DateTime> getAllInstantsInInterval(
            OrgDateTime orgDateTime,
            ReadableInstant fromTime,
            ReadableInstant beforeTime,
            int limit) {

        DateTime time = new DateTime(orgDateTime.getCalendar());

        List<DateTime> result = new ArrayList<>();

        if (! orgDateTime.hasRepeater()) {
            if (!time.isBefore(fromTime) && (beforeTime == null || time.isBefore(beforeTime))) {
                result.add(time);
            }

        } else {
            if (beforeTime == null && limit == 0) {
                throw new IllegalArgumentException("When beforeTime is not specified, limit is mandatory");
            }

            if (limit > MAX_INSTANTS_IN_INTERVAL) {
                limit = MAX_INSTANTS_IN_INTERVAL;
            }

            OrgRepeater repeater = orgDateTime.getRepeater();

            /* Interval between org time and from time. */
            Interval gap = new Interval(time, fromTime);

            /* How many units in gap. */
            Period intervalPeriod = gap.toPeriod(OrgDateTimeUtils.getPeriodType(repeater.getUnit()));
            int units = intervalPeriod.getValue(0);

            /* How many units to add to get to just before fromTime.
             * This is multiples of repeater's value.
             */
            int repeatTimes = units / repeater.getValue();
            int addUnits = repeater.getValue() * repeatTimes;

            /* Time just before the interval we are interested in. */
            DateTime justBefore = time.withFieldAdded(OrgDateTimeUtils.getDurationFieldType(repeater.getUnit()), addUnits);

            /* Shift once. */
            DateTime curr = justBefore.withFieldAdded(
                    OrgDateTimeUtils.getDurationFieldType(repeater.getUnit()),
                    repeater.getValue()
            );

            while (beforeTime == null || curr.isBefore(beforeTime)) {
                result.add(curr);

                if (limit > 0 && result.size() >= limit) {
                    break;
                }

                /* Shift. */
                curr = curr.withFieldAdded(
                        OrgDateTimeUtils.getDurationFieldType(repeater.getUnit()),
                        repeater.getValue()
                );
            }
        }

        return result;
    }

    private static PeriodType getPeriodType(OrgInterval.Unit unit) {
        switch (unit) {
            case HOUR:
                return PeriodType.hours();
            case DAY:
                return PeriodType.days();
            case WEEK:
                return PeriodType.weeks();
            case MONTH:
                return PeriodType.months();
            case YEAR:
                return PeriodType.years();
            default:
                throw new IllegalArgumentException("Unknown unit " + unit);
        }
    }

    private static DurationFieldType getDurationFieldType(OrgInterval.Unit unit) {
        switch (unit) {
            case HOUR:
                return DurationFieldType.hours();
            case DAY:
                return DurationFieldType.days();
            case WEEK:
                return DurationFieldType.weeks();
            case MONTH:
                return DurationFieldType.months();
            case YEAR:
                return DurationFieldType.years();
            default:
                throw new IllegalArgumentException("Unknown unit " + unit);
        }
    }
}
