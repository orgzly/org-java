package com.orgzly.org.datetime;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    public static List<DateTime> getTimesInInterval(
            @NotNull OrgDateTime orgDateTime,
            @NotNull ReadableInstant fromTime,
            @Nullable ReadableInstant beforeTime,
            boolean useRepeater,
            int limit) {
        return getTimesInInterval(orgDateTime, fromTime, beforeTime, useRepeater, null, limit);
    }

        /**
         * Returns list of {@link DateTime} that belong to specified {@link OrgDateTime}
         * and are within specified time interval.
         *
         * @param orgDateTime {@link OrgDateTime}
         * @param fromTime Inclusive
         * @param beforeTime Exclusive. Can be null in which case limit has to be specified
         * @param useRepeater Use repeater from {@link OrgDateTime}
         * @param warningPeriod Deadline warning period
         * @param limit When {@code orgTime} has a repeater, limit the number of results to this number
         *
         * @return List of times within specified interval
         */
    public static List<DateTime> getTimesInInterval(
            @NotNull OrgDateTime orgDateTime,
            @NotNull ReadableInstant fromTime,
            @Nullable ReadableInstant beforeTime,
            boolean useRepeater,
            @Nullable OrgInterval warningPeriod,
            int limit) {

        List<DateTime> result = new ArrayList<>();

        DateTime time = new DateTime(orgDateTime.getCalendar());

        // System.out.println(orgDateTime + " (" + time + ") " + fromTime + " - " + beforeTime);

        /* If Org time has no repeater or it should be ignored,
         * just check if time part is within the interval.
         */
        if (!useRepeater || !orgDateTime.hasRepeater() || orgDateTime.getRepeater().getValue() == 0) {
            time = applyWarningPeriod(time, warningPeriod);

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

            time = applyWarningPeriod(time, warningPeriod);

            if (time.isBefore(fromTime)) {
                /* How many periods between time and start of interval. */
                Interval gap = new Interval(time, fromTime);
                Period intervalPeriod = gap.toPeriod(OrgDateTimeUtils.getPeriodType(repeater.getUnit()));
                int units = intervalPeriod.getValue(0);

                /* How many units to add to get just after the start of interval.
                 * This is multiples of repeater's value.
                 */
                int repeatTimes = (units + repeater.getValue() - 1) / repeater.getValue(); // ceil
                int addUnits = repeater.getValue() * repeatTimes;

                /* Time just after the interval we are interested in. */
                time = time.withFieldAdded(OrgDateTimeUtils.getDurationFieldType(repeater.getUnit()), addUnits);

                System.out.println(
                        "gap: " + gap
                        + " intervalPeriod: " + intervalPeriod
                        + " units: " + units
                        + " repeatTimes: " + repeatTimes
                        + " addUnits: " + addUnits
                        + " time: " + time);
            }

            // Shift time until it's out of the specified interval or limit is reached
            while (beforeTime == null || time.isBefore(beforeTime)) {
                System.out.println("WIP " + time + ", " + beforeTime);
                result.add(time);

                /* Check if limit has been reached. */
                if (limit > 0 && result.size() >= limit) {
                    break;
                }

                /* Shift. */
                time = time.withFieldAdded(
                        OrgDateTimeUtils.getDurationFieldType(repeater.getUnit()),
                        repeater.getValue()
                );
            }
        }

        return result;
    }

    private static DateTime applyWarningPeriod(DateTime time, OrgInterval warningPeriod) {
        if (warningPeriod != null) {
            return time.withFieldAdded(
                    OrgDateTimeUtils.getDurationFieldType(warningPeriod.getUnit()),
                    -1 * warningPeriod.getValue());
        } else {
            return time;
        }
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
