package com.orgzly.org.datetime;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OrgDateTimeUtilsTest {
    @Test
    public void testTimesInInterval() {
        assertThat(
                toStringArray(OrgDateTimeUtils.getTimesInInterval(
                        OrgDateTime.parse("<2017-04-01 .+3d>"),
                        DateTime.parse("2017-04-15T05:39:58"),
                        DateTime.parse("2017-04-20T05:39:58"),
                        0,
                        true,
                        null,
                        0)),
                is(toStringArray(Arrays.asList(
                        DateTime.parse("2017-04-16"),
                        DateTime.parse("2017-04-19")))));
    }

    @Test
    public void testTimesInIntervalIgnoringRepeater() {
        assertThat(
                toStringArray(OrgDateTimeUtils.getTimesInInterval(
                        OrgDateTime.parse("<2017-04-01 .+3d>"),
                        DateTime.parse("2017-04-15T05:39:58"),
                        DateTime.parse("2017-04-20T05:39:58"),
                        0,
                        false,
                        null,
                        0)),
                is(Collections.<String>emptyList()));
    }

    @Test
    public void testTimesInIntervalWithoutUpperBound() {
        assertThat(
                toStringArray(OrgDateTimeUtils.getTimesInInterval(
                        OrgDateTime.parse("<2017-04-01 .+3d>"),
                        DateTime.parse("2017-04-15T05:39:58"),
                        null,
                        0,
                        true,
                        null,
                        3)),
                is(toStringArray(Arrays.asList(
                        DateTime.parse("2017-04-16"),
                        DateTime.parse("2017-04-19"),
                        DateTime.parse("2017-04-22")))));
    }

    @Test
    public void testTimeInFuture() {
        assertThat(
                toStringArray(OrgDateTimeUtils.getTimesInInterval(
                        OrgDateTime.parse("<2017-07-16 Sun ++1w>"),
                        DateTime.parse("2017-04-15T05:39:58"),
                        null,
                        0,
                        true,
                        null,
                        3)),
                is(toStringArray(Arrays.asList(
                        DateTime.parse("2017-07-16"),
                        DateTime.parse("2017-07-23"),
                        DateTime.parse("2017-07-30")))));
    }

    @Test
    public void testTimeInFutureIgnoringRepeater() {
        assertThat(
                toStringArray(OrgDateTimeUtils.getTimesInInterval(
                        OrgDateTime.parse("<2017-07-16 Sun ++1w>"),
                        DateTime.parse("2017-04-15T05:39:58"),
                        null,
                        0,
                        false,
                        null,
                        3)),
                is(toStringArray(Arrays.asList(
                        DateTime.parse("2017-07-16")))));
    }

    @Test
    public void testZeroRepeaterValue() {
        assertThat(
                toStringArray(OrgDateTimeUtils.getTimesInInterval(
                        OrgDateTime.parse("<2017-04-16 .+0d>"),
                        DateTime.parse("2017-04-15T05:39:58"),
                        DateTime.parse("2017-04-20T05:39:58"),
                        0,
                        true,
                        null,
                        0)),
                is(toStringArray(Arrays.asList(
                        DateTime.parse("2017-04-16")))));
    }

    @Test
    public void testCumulateOneDay() {
        assertThat(
                toStringArray(OrgDateTimeUtils.getTimesInInterval(
                        OrgDateTime.parse("<2017-04-16 +1d>"),
                        DateTime.parse("2017-04-17"),
                        DateTime.parse("2017-04-19"),
                        0,
                        true,
                        null,
                        0)),
                is(toStringArray(Arrays.asList(
                        DateTime.parse("2017-04-17"),
                        DateTime.parse("2017-04-18")))));
    }

    @Test
    public void testWarningPeriod() {
        assertThat(
                toStringArray(OrgDateTimeUtils.getTimesInInterval(
                        OrgDateTime.parse("<2017-04-16 -1d>"),
                        DateTime.parse("2017-04-10"),
                        null,
                        0,
                        true,
                        new OrgInterval(1, OrgInterval.Unit.DAY),
                        0)),
                is(toStringArray(Arrays.asList(
                        DateTime.parse("2017-04-15")))));
    }

    @Test
    public void testWarningPeriodWithRepeater() {
        assertThat(
                toStringArray(OrgDateTimeUtils.getTimesInInterval(
                        OrgDateTime.parse("<2017-04-16 +3d -2d>"),
                        DateTime.parse("2017-04-15"),
                        null,
                        0,
                        true,
                        new OrgInterval(2, OrgInterval.Unit.DAY),
                        5)),
                is(toStringArray(Arrays.asList(
                        DateTime.parse("2017-04-17"),
                        DateTime.parse("2017-04-20"),
                        DateTime.parse("2017-04-23"),
                        DateTime.parse("2017-04-26"),
                        DateTime.parse("2017-04-29")))));
    }

    private List<String> toStringArray(List<DateTime> times) {
        List<String> result = new ArrayList<>();

        for (DateTime time: times) {
            result.add(time.toString());
        }

        return result;
    }
}
