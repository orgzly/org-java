package com.orgzly.org.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class OrgDomyParserTest {
    private String ACTUAL =
            "Text on top!\n" +
            "\n" +
            "** Time\n" +
            "*** TODO Clock\n" +
            "    CLOCK: [2014-10-22 сре 23:02]\n" +
            "*** TODO Scheduled time with repeat, once completed\n" +
            "    SCHEDULED: <2014-10-24 пет +2d>\n" +
            "    - State \"DONE\"       from \"TODO\"       [2014-10-22 сре 21:21]\n" +
            "    :PROPERTIES:\n" +
            "    :LAST_REPEAT: [2014-10-22 сре 21:21]\n" +
            "    :END:\n" +
            "\n" +
            "*** TODO Scheduled with time range\n" +
            "    SCHEDULED: <2014-10-22 сре 01:00-04:00>\n" +
            "\n" +
            "*** TODO Scheduled repeat with time range, once completed\n" +
            "    SCHEDULED: <2014-10-23 чет 01:00-04:00 +1d>\n" +
            "    - State \"DONE\"       from \"TODO\"       [2014-10-22 сре 21:24]\n" +
            "    :PROPERTIES:\n" +
            "    :LAST_REPEAT: [2014-10-22 сре 21:24]\n" +
            "    :END:\n" +
            "    \n" +
            "*** TODO Two timestamps (range?)\n" +
            "    SCHEDULED: <2014-10-22 сре>--<2014-10-23 чет>\n" +
            "* This is the first line with tag at the end of the line                :tag1:\n" +
            "** [#A] Second heading with priority                                    :tag2:\n" +
            "*** TODO   [#B]Third heading with spaced priority                       :tag3:\n" +
            "**** DONE Heading number 4 :tag:todo:\n" +
            "CLOSED: [2012-12-29 Sat 08:05] DEADLINE: <2012-12-28 Fri> SCHEDULED: <2012-12-27 Thu>\n" +
            "***** This is 5 :tag:\n" +
            "      SCHEDULED: <2013-01-04 Fri 01:00>\n" +
            "      :PROPERTIES:\n" +
            "      :created_at: <2013-06-22 Sat 12:35>\n" +
            "      :END:\n" +
            "\n" +
            "| f1  |  f2 |\n" +
            "|-----+-----|\n" +
            "| asd | asd |\n" +
            "| 1   |   2 |\n" +
            "\n" +
            "****** [#F] OMG, this is 6, not more :tag:\n" +
            "\n" +
            "* Commonly used tags1 :tags:help:org:mode:\n" +
            "* Commonly used tags2                                    :tags:help:org:mode:\n" +
            "\n" +
            "* Commonly used tags3:tags:help:org:mode:\n" +
            "\n" +
            "* Scheduling\n" +
            "** TODO Multiple SCHEDULED for a note                             :multiple:\n" +
            "SCHEDULED: <2013-01-01>\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 14:53]\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 14:53]\n" +
            "\n" +
            "Some content before another scheduled.\n" +
            "SCHEDULED: <2013-01-17 Thu 12:00 +1w>\n" +
            ":PROPERTIES:\n" +
            ":LAST_REPEAT: [2013-06-22 Sat 14:53]\n" +
            ":END:\n" +
            "\n" +
            "** TODO Clocked item\n" +
            "SCHEDULED: <2013-06-28 Fri ++1d>\n" +
            "- State \"DONE\"       from \"\"           [2013-06-22 Sat 15:03]\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 15:03]\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 15:03]\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 15:03]\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 15:03]\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 15:03]\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 14:48]\n" +
            "CLOCK: [2013-06-22 Sat 14:17]--[2013-06-22 Sat 14:48] =>  0:31\n" +
            ":PROPERTIES:\n" +
            ":created_at: <2013-06-22 Sat 12:35>\n" +
            ":LAST_REPEAT: [2013-06-22 Sat 15:03]\n" +
            ":END:\n" +
            "\n" +
            "** Next note\n" +
            "Content with no new-line spacing\n" +
            "** Another note\n" +
            ":LOGBOOK:\n" +
            "- Blah\n" +
            ":END:\n" +
            "**** Grandchild with no parent\n" +
            "*** Note\n"
            ;

    private String EXPECTED =
            "Text on top!\n" +
            "\n" +
            "** Time\n" +
            "*** TODO Clock\n" +
            "    CLOCK: [2014-10-22 сре 23:02]\n" +
            "\n" +
            "*** TODO Scheduled time with repeat, once completed\n" +
            "    SCHEDULED: <2014-10-24 пет +2d>\n" +
            "    - State \"DONE\"       from \"TODO\"       [2014-10-22 сре 21:21]\n" +
            "    :PROPERTIES:\n" +
            "    :LAST_REPEAT: [2014-10-22 сре 21:21]\n" +
            "    :END:\n" +
            "\n" +
            "*** TODO Scheduled with time range\n" +
            "    SCHEDULED: <2014-10-22 сре 01:00-04:00>\n" +
            "\n" +
            "*** TODO Scheduled repeat with time range, once completed\n" +
            "    SCHEDULED: <2014-10-23 чет 01:00-04:00 +1d>\n" +
            "    - State \"DONE\"       from \"TODO\"       [2014-10-22 сре 21:24]\n" +
            "    :PROPERTIES:\n" +
            "    :LAST_REPEAT: [2014-10-22 сре 21:24]\n" +
            "    :END:\n" +
            "\n" +
            "*** TODO Two timestamps (range?)\n" +
            "    SCHEDULED: <2014-10-22 сре>--<2014-10-23 чет>\n" +
            "\n" +
            "* This is the first line with tag at the end of the line :tag1:\n" +
            "** [#A] Second heading with priority :tag2:\n" +
            "*** TODO [#B] Third heading with spaced priority :tag3:\n" +
            "**** DONE Heading number 4 :tag:todo:\n" +
            "     CLOSED: [2012-12-29 Sat 08:05] DEADLINE: <2012-12-28 Fri> SCHEDULED: <2012-12-27 Thu>\n" +
            "\n" +
            "***** This is 5 :tag:\n" +
            "      SCHEDULED: <2013-01-04 Fri 01:00>\n" +
            "      :PROPERTIES:\n" +
            "      :created_at: <2013-06-22 Sat 12:35>\n" +
            "      :END:\n" +
            "\n" +
            "| f1  |  f2 |\n" +
            "|-----+-----|\n" +
            "| asd | asd |\n" +
            "| 1   |   2 |\n" +
            "\n" +
            "****** [#F] OMG, this is 6, not more :tag:\n" +
            "* Commonly used tags1 :tags:help:org:mode:\n" +
            "* Commonly used tags2 :tags:help:org:mode:\n" +
            "* Commonly used tags3:tags:help:org:mode:\n" +
            "* Scheduling\n" +
            "** TODO Multiple SCHEDULED for a note :multiple:\n" +
            "   SCHEDULED: <2013-01-01>\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 14:53]\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 14:53]\n" +
            "\n" +
            "Some content before another scheduled.\n" +
            "SCHEDULED: <2013-01-17 Thu 12:00 +1w>\n" +
            ":PROPERTIES:\n" +
            ":LAST_REPEAT: [2013-06-22 Sat 14:53]\n" +
            ":END:\n" +
            "\n" +
            "** TODO Clocked item\n" +
            "   SCHEDULED: <2013-06-28 Fri ++1d>\n" +
            "- State \"DONE\"       from \"\"           [2013-06-22 Sat 15:03]\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 15:03]\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 15:03]\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 15:03]\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 15:03]\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 15:03]\n" +
            "- State \"DONE\"       from \"TODO\"       [2013-06-22 Sat 14:48]\n" +
            "CLOCK: [2013-06-22 Sat 14:17]--[2013-06-22 Sat 14:48] =>  0:31\n" +
            ":PROPERTIES:\n" +
            ":created_at: <2013-06-22 Sat 12:35>\n" +
            ":LAST_REPEAT: [2013-06-22 Sat 15:03]\n" +
            ":END:\n" +
            "\n" +
            "** Next note\n" +
            "\n" +
            "Content with no new-line spacing\n" +
            "\n" +
            "** Another note\n" +
            "   :LOGBOOK:\n" +
            "   - Blah\n" +
            "   :END:\n" +
            "\n" +
            "**** Grandchild with no parent\n" +
            "*** Note\n"
            ;

    @Test
    public void testParserHook() throws IOException {
        OrgParser parser = new OrgParser.Builder()
                .setTodoKeywords(new String[]{"TODO"})
                .setDoneKeywords(new String[]{"DONE"})
                .setInput(ACTUAL)
                .build();

        String actual = parser.parse().toString();

        Assert.assertEquals(EXPECTED, actual);
    }
}
