package com.orgzly.orgjava.datetime;

import com.orgzly.orgjava.datetime.OrgRange;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

@RunWith(value = Parameterized.class)
public class OrgRangeShiftTest {
    private String timestamp;
    private String expected;
    private boolean isShifted;
    private Calendar now;

    public OrgRangeShiftTest(String timestamp, String expected, boolean isShifted, Calendar now) {
        this.timestamp = timestamp;
        this.expected = expected;
        this.isShifted = isShifted;
        this.now = now != null ? now : Calendar.getInstance();
    }

    @Parameterized.Parameters(name= "{index}: Shifting ({2}) {0} should return {1} on {3}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "<2009-10-17 13:15>", "2009-10-17 Sat 13:15", false, null },
                { "<2009-10-17 13:15 +1d>", "2009-10-18 Sun 13:15 +1d", true, null },

                // Not supported or broken in Org mode (last checked: 8.2.10 @ 24.4.1)
                { "<2009-10-17 13:15 ++1h>", "2015-01-27 Tue 12:15 ++1h", true, new GregorianCalendar(2015, 0, 27, 12, 5) },
                { "<2009-10-17 13:15 .+1h>", "2015-01-27 Tue 10:15 .+1h", true, new GregorianCalendar(2015, 0, 27, 9, 0, 0) },
                { "<2015-07-27 Mon 10:15 ++1h>", "2015-08-03 Mon 18:15 ++1h", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                { "<2015-07-27 Mon 10:15 ++3h>", "2015-08-03 Mon 19:15 ++3h", true, new GregorianCalendar(2015, 7, 3, 18, 8) },

                { "<2014-10-17 13:15 ++1h>", "2015-01-27 Tue 21:15 ++1h", true, new GregorianCalendar(2015, 0, 27, 20, 15, 0) },
                { "<2009-10-17 13:15 ++1y>", "2011-10-17 Mon 13:15 ++1y", true, new GregorianCalendar(2011, 1, 1) },
                { "<2009-10-17 13:15 ++1y>", "2012-10-17 Wed 13:15 ++1y", true, new GregorianCalendar(2011, 11, 1)},
                { "<2009-10-17 13:15 .+1d>", "2015-01-28 Wed 13:15 .+1d", true, new GregorianCalendar(2015, 0, 27) },
                { "<2009-10-17 13:15 .+1w>", "2015-02-03 Tue 13:15 .+1w", true, new GregorianCalendar(2015, 0, 27) },
                { "<2009-10-17 13:15 .+1m>", "2015-02-27 Fri 13:15 .+1m", true, new GregorianCalendar(2015, 0, 27) },
                { "<2009-10-17 13:15 .+1y>", "2016-01-27 Wed 13:15 .+1y", true, new GregorianCalendar(2015, 0, 27) },

                { "<2009-10-17 13:15-14:30 +1y --2d>--<2015-01-18>", "2010-10-17 Sun 13:15-14:30 +1y --2d--2015-01-18 Sun", true, null },
                { "<2015-01-13 уто 13:00-14:14 +2d>--<2015-01-14 сре 14:10-15:20>", "2015-01-15 Thu 13:00-14:14 +2d--2015-01-14 Wed 14:10-15:20", true, null },

                // { "<2015-07-27 Mon 10:15 ++1s>", "2015-07-27 Mon 10:15 ++1s", false, new GregorianCalendar(2015, 7, 3, 18, 8) }

                { "<2015-07-27 Mon 10:15 ++1d>", "2015-08-04 Tue 10:15 ++1d", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                { "<2015-07-27 Mon 10:15 ++1w>", "2015-08-10 Mon 10:15 ++1w", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                { "<2015-07-27 Mon 10:15 ++1m>", "2015-08-27 Thu 10:15 ++1m", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                { "<2015-07-27 Mon 10:15 ++1y>", "2016-07-27 Wed 10:15 ++1y", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                { "<2015-07-27 Mon 10:15 ++3d>", "2015-08-05 Wed 10:15 ++3d", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                { "<2015-07-27 Mon 10:15 ++3w>", "2015-08-17 Mon 10:15 ++3w", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                { "<2015-07-27 Mon 10:15 ++3m>", "2015-10-27 Tue 10:15 ++3m", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                { "<2015-07-27 Mon 10:15 ++3y>", "2018-07-27 Fri 10:15 ++3y", true, new GregorianCalendar(2015, 7, 3, 18, 8) },

                { "<2015-08-04 Tue 10:15 ++3h>", "2015-08-04 Tue 13:15 ++3h", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                { "<2015-08-04 Tue 10:15 ++3d>", "2015-08-07 Fri 10:15 ++3d", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                { "<2015-08-04 Tue 10:15 ++3w>", "2015-08-25 Tue 10:15 ++3w", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                { "<2015-08-04 Tue 10:15 ++3m>", "2015-11-04 Wed 10:15 ++3m", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                { "<2015-08-04 Tue 10:15 ++3y>", "2018-08-04 Sat 10:15 ++3y", true, new GregorianCalendar(2015, 7, 3, 18, 8) }
                // { "<2015-08-04 Tue 10:15 ++3h>", "2015-08-04 Tue 13:15 ++3h", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                // { "<2015-08-04 Tue 10:15 ++3d>", "2015-08-07 Fri 10:15 ++3d", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                // { "<2015-08-04 Tue 10:15 ++3w>", "2015-08-25 Tue 10:15 ++3w", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                // { "<2015-08-04 Tue 10:15 ++3m>", "2015-11-04 Wed 10:15 ++3m", true, new GregorianCalendar(2015, 7, 3, 18, 8) },
                // { "<2015-08-04 Tue 10:15 ++3y>", "2018-08-04 Sat 10:15 ++3y", true, new GregorianCalendar(2015, 7, 3, 18, 8) }
        });
    }

    @Test
    public void testTimeShift() {
        OrgRange range = OrgRange.getInstance(timestamp);

        Assert.assertEquals(isShifted, range.shift(now));

        Assert.assertEquals(expected, range.toStringWithoutBrackets());
    }
}
