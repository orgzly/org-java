package com.orgzly.org.datetime;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OrgDateTimeUtilsTest {
    @Test
    public void testTimesInInterval() {
        assertThat(
                toStringArray(OrgDateTimeUtils.getTimesInInterval(
                        OrgDateTime.getInstance("<2017-04-01 .+3d>"),
                        DateTime.parse("2017-04-15T05:39:58"),
                        DateTime.parse("2017-04-20T05:39:58"),
                        0)),
                is(toStringArray(Arrays.asList(
                        DateTime.parse("2017-04-16"),
                        DateTime.parse("2017-04-19")))));
    }

    @Test
    public void testTimesInIntervalWithoutUpperBound() {
        assertThat(
                toStringArray(OrgDateTimeUtils.getTimesInInterval(
                        OrgDateTime.getInstance("<2017-04-01 .+3d>"),
                        DateTime.parse("2017-04-15T05:39:58"),
                        null,
                        3)),
                is(toStringArray(Arrays.asList(
                        DateTime.parse("2017-04-16"),
                        DateTime.parse("2017-04-19"),
                        DateTime.parse("2017-04-22")))));
    }

    @Test
    public void testTimeInFutureWithRepeater() {
        assertThat(
                toStringArray(OrgDateTimeUtils.getTimesInInterval(
                        OrgDateTime.getInstance("<2017-07-16 Sun ++1w>"),
                        DateTime.parse("2017-04-15T05:39:58"),
                        null,
                        3)),
                is(toStringArray(Arrays.asList(
                        DateTime.parse("2017-07-16"),
                        DateTime.parse("2017-07-23"),
                        DateTime.parse("2017-07-30")))));
    }

    private List<String> toStringArray(List<DateTime> times) {
        List<String> result = new ArrayList<>();

        for (DateTime time: times) {
            result.add(time.toString());
        }

        return result;
    }
}
