package com.orgzly.orgjava.parser;


import com.orgzly.orgjava.OrgFileSettings;
import com.orgzly.orgjava.OrgHead;
import com.orgzly.orgjava.OrgTestParser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;

public class OrgParserTest extends OrgTestParser {
    private OrgParserWriter parserWriter;

    @Before
    public void setUp() {
        parserWriter = new OrgParserWriter();
    }

    @Test
    public void testNoteWithMultipleParameters() throws IOException {
        Assert.assertEquals("* Note\n  DEADLINE: <2000-01-01 10:10> SCHEDULED: <2000-01-01 10:10>\n\n",
                parsed("* Note\n  SCHEDULED:  <2000-01-01 10:10>    DEADLINE: <2000-01-01 10:10>"));
    }

    @Test
    public void testNoteWithRange() throws IOException {
        Assert.assertEquals("* Note\nSCHEDULED: <2000-01-01 10:10>--<2000-01-01 10:10>\n\n",
                parsed("* Note\nSCHEDULED:<2000-01-01 10:10>--<2000-01-01 10:10>"));
    }

    @Test
    public void testNoteWithRangeAndMultipleParameters() throws IOException {
        Assert.assertEquals("* Note\nDEADLINE: <2010-10-10> SCHEDULED: <2000-01-01 10:10>--<2000-01-01 10:10>\n\n",
                parsed("* Note\nSCHEDULED:<2000-01-01 10:10>--<2000-01-01 10:10> DEADLINE: <2010-10-10>"));
    }

    @Test
    public void testNote() throws IOException {
        Assert.assertEquals("** Note\n", parsed("** Note"));
    }

    @Test
    public void testTodoWithTagScheduledAndContent() throws IOException {
        Assert.assertEquals(
                "** TODO Multiple SCHEDULED for a note :multiple:\n" +
                "SCHEDULED: <2013-01-01>\n" +
                "\n" +
                "Content.\n\n",
                parsed("** TODO Multiple SCHEDULED for a note :multiple:\n" +
                       "SCHEDULED: <2013-01-01>\n" +
                       "\n" +
                       "Content."));
    }

    @Test
    public void testTodoWithHourlyScheduled1() throws IOException {
        Assert.assertEquals(
                "** TODO Multiple SCHEDULED for a note :multiple:\n" +
                "SCHEDULED: <2013-01-01 01:00>\n" +
                "\n" +
                "Content.\n" +
                "\n",
                parsed("** TODO Multiple SCHEDULED for a note :multiple:\n" +
                       "SCHEDULED: <2013-01-01 01:00>\n" +
                       "\n" +
                       "Content."));
    }

    @Test
    public void testTodoWithHourlyScheduled2() throws IOException {
        Assert.assertEquals(
                "** TODO Multiple SCHEDULED for a note :multiple:\n" +
                "SCHEDULED: <2013-01-01 00:00>\n" +
                "\n" +
                "Content.\n" +
                "\n",
                parsed("** TODO Multiple SCHEDULED for a note :multiple:\n" +
                       "SCHEDULED: <2013-01-01 00:00>\n" +
                       "\n" +
                       "Content."));
    }

    @Test
    public void testTodoWithSingleTag() throws IOException {
        Assert.assertEquals("** TODO Simple todo note :simple:\n",
                parsed("** TODO Simple todo note :simple:"));
    }

    @Test
    public void testTodoWithSingleSpacedOutTag() throws IOException {
        Assert.assertEquals("** TODO Simple todo note :simple:\n",
                parsed("** TODO Simple todo note           :simple:"));
    }

    @Test
    public void testTagsCharacters1() throws IOException {
        OrgHead head = parserBuilder.setInput("* Note :سلامی:").build().parse().getHeadsInList().get(0).getHead();

        Assert.assertEquals(1, head.getTags().size());
        Assert.assertEquals("سلامی", head.getTags().get(0));
    }

    @Test
    public void testTagsCharacters2() throws IOException {
        OrgHead head = parserBuilder.setInput("* Note :Αλφα:").build().parse().getHeadsInList().get(0).getHead();

        Assert.assertEquals(1, head.getTags().size());
        Assert.assertEquals("Αλφα", head.getTags().get(0));
    }

    @Test
    public void testTagsCharacters3() throws IOException {
        OrgHead head = parserBuilder.setInput("* Note :Σ:").build().parse().getHeadsInList().get(0).getHead();

        Assert.assertEquals(1, head.getTags().size());
        Assert.assertEquals("Σ", head.getTags().get(0));
    }

    @Test
    public void testTagsCharacters4() throws IOException {
        OrgHead head = parserBuilder.setInput("* Note :русский:").build().parse().getHeadsInList().get(0).getHead();

        Assert.assertEquals(1, head.getTags().size());
        Assert.assertEquals("русский", head.getTags().get(0));
    }

    @Test
    public void testTagsCharacters5() throws IOException {
        OrgHead head = parserBuilder.setInput("* Note :漢語:").build().parse().getHeadsInList().get(0).getHead();

        Assert.assertEquals(1, head.getTags().size());
        Assert.assertEquals("漢語", head.getTags().get(0));
    }

    @Test
    public void testTagsCharactersA() throws IOException {
        OrgHead head = parserBuilder.setInput("* Note :0aZ_#@%:").build().parse().getHeadsInList().get(0).getHead();

        Assert.assertEquals(1, head.getTags().size());
        Assert.assertEquals("0aZ_#@%", head.getTags().get(0));
    }

    @Test
    public void testTagsCharactersB() throws IOException {
        OrgHead head = parserBuilder.setInput("* Note :ру́сский:Αλφα:Σ:سلامی:漢語:").build().parse().getHeadsInList().get(0).getHead();

        Assert.assertEquals(5, head.getTags().size());
    }

    @Test
    public void testScheduledAndClosed() throws IOException {
        Assert.assertEquals("** TODO Multiple SCHEDULED for a note :multiple:\n" +
                            "CLOSED: [2012-12-29 Sat 08:05] DEADLINE: <2012-12-28 Fri> SCHEDULED: <2012-12-27 Thu>\n\n",
                parsed("** TODO Multiple SCHEDULED for a note :multiple:\n" +
                       "CLOSED: [2012-12-29 Sat 08:05] DEADLINE: <2012-12-28 Fri> SCHEDULED: <2012-12-27 Thu>"));
    }

    @Test
    public void testTodoWithWeekKay() throws IOException {
        Assert.assertEquals(
                "** TODO Multiple SCHEDULED for a note :multiple:\n"
                + "SCHEDULED: <2013-01-01 Tue>\n" +
                "\n" +
                "Content.\n" +
                "\n",
                parsed("** TODO Multiple SCHEDULED for a note :multiple:\n"
                       + "SCHEDULED: <2013-01-01 Tue>\n" +
                       "\n" +
                       "Content."));
    }

    @Test
    public void testTodoWithoutWeekKay() throws IOException {
        Assert.assertEquals(
                "** TODO Multiple SCHEDULED for a note :multiple:\n"
                + "SCHEDULED: <2013-01-01>\n" +
                "\n" +
                "Content.\n" +
                "\n",
                parsed("** TODO Multiple SCHEDULED for a note :multiple:\n"
                       + "SCHEDULED: <2013-01-01>\n" +
                       "\n" +
                       "Content."));
    }

    @Test
    public void testTrimmingContent() throws IOException {
        Assert.assertEquals(
                "** Note\n" +
                "\n" +
                "  Line with spaces prepended\n" +
                "  Line with spaces appended   \n" +
                "\n",
                parsed("** Note\n" +
                       "\n" +
                       "\n" +
                       "\n" +
                       "  Line with spaces prepended\n" +
                       "  Line with spaces appended   \n" +
                       "\n" +
                       "\n" +
                       "\n" +
                       "\n"));
    }

    @Test
    public void testTodoInTitle() throws IOException {
        OrgHead head = parserBuilder
                .setInput("** TODO TODO in title")
                .build().parse().getHeadsInList().get(0).getHead();

        Assert.assertEquals("TODO in title", head.getTitle());
        Assert.assertEquals("TODO", head.getState());
    }

    @Test
    public void testBrokenTodoInTitle() throws IOException {
        OrgHead head = parserBuilder
                .setInput("** TODO:broken")
                .build().parse().getHeadsInList().get(0).getHead();

        Assert.assertEquals("TODO:broken", head.getTitle());
        Assert.assertNull(head.getState());
    }

    @Test
    public void testNonStandardStateWithDefaults() throws IOException {
        OrgHead head = parserBuilder
                .setInput("** DO_NOW Title")
                .build().parse().getHeadsInList().get(0).getHead();

        Assert.assertEquals("DO_NOW Title", head.getTitle());
        Assert.assertNull(head.getState());
    }

    @Test
    public void testNonStandardState1() throws IOException {
        OrgHead head = parserBuilder
                .setInput("** DO_NOW Title")
                .setTodoKeywords(new String[]{"TODO", "DO_NOW"})
                .build().parse().getHeadsInList().get(0).getHead();

        Assert.assertEquals("Title", head.getTitle());
        Assert.assertEquals("DO_NOW", head.getState());
    }

    @Test
    public void testNonStandardState2() throws IOException {
        OrgHead head = parserBuilder
                .setInput("** DO-NOW Title")
                .setTodoKeywords(new String[]{"TODO", "DO-NOW"})
                .build().parse().getHeadsInList().get(0).getHead();

        Assert.assertEquals("Title", head.getTitle());
        Assert.assertEquals("DO-NOW", head.getState());
    }

    @Test
    public void testNonStandardState3() throws IOException {
        OrgHead head = parserBuilder
                .setInput("** DO.NOW.1.+_)(*&^%$#@!~` Title")
                .setTodoKeywords(new String[]{"DO.NOW.1.+_)(*&^%$#@!~`"})
                .build().parse().getHeadsInList().get(0).getHead();

        Assert.assertEquals("Title", head.getTitle());
        Assert.assertEquals("DO.NOW.1.+_)(*&^%$#@!~`", head.getState());
    }

    @Test
    public void testNoteContent() throws IOException {
        OrgHead head = parserBuilder.setInput("** Note").build().parse().getHeadsInList().get(0).getHead();
        Assert.assertFalse("note should not have content", head.hasContent());
    }

    @Test
    public void testStateKeywordAndTitleEmpty() throws IOException {
        OrgNodeInList nodeInList = parserBuilder.setInput("** TODO").build().parse().getHeadsInList().get(0);
        Assert.assertEquals(2, nodeInList.getLevel());
        Assert.assertEquals("", nodeInList.getHead().getTitle());
        Assert.assertEquals("TODO", nodeInList.getHead().getState());
    }

    @Test
    public void testTitleEmpty() throws IOException {
        OrgNodeInList nodeInList = parserBuilder.setInput("** ").build().parse().getHeadsInList().get(0);
        Assert.assertEquals(2, nodeInList.getLevel());
        Assert.assertEquals("", nodeInList.getHead().getTitle());
        Assert.assertNull(nodeInList.getHead().getState());
    }

    @Test
    public void testStateKeywordAndTitleNotSeparated() throws IOException {
        OrgParsedFile file = parserBuilder.setInput("** TODONote\n").build().parse();
        OrgNodeInList nodeInList = file.getHeadsInList().get(0);
        Assert.assertEquals(2, nodeInList.getLevel());
        Assert.assertNull(nodeInList.getHead().getState());
        Assert.assertEquals("TODONote", nodeInList.getHead().getTitle());
    }

    @Test
    public void testSimpleNonASCIICharacters() throws IOException {
        String preface = parserBuilder.setInput("DIE PERSER (Ü: Andreas Röhler) Schauspiel 1 D 3 H Stand:").build().parse().getFile().getPreface();
        Assert.assertEquals("DIE PERSER (Ü: Andreas Röhler) Schauspiel 1 D 3 H Stand:", preface);
        Assert.assertEquals("DIE PERSER (Ü: Andreas Röhler) Schauspiel 1 D 3 H Stand:\n\n", parserWriter.whiteSpacedFilePreface(preface));
    }

    @Test
    public void testContentNote() {
        OrgHead head = new OrgHead();
        OrgNodeInList headElement = new OrgNodeInList(1, 1, head);
        head.setTitle("Title");

        String out = parserWriter.whiteSpacedFilePreface("Content.") + parserWriter.whiteSpacedHead(headElement, false);
        Assert.assertEquals("Content.\n\n* Title\n", out);
    }

    @Test
    public void testContentLineNote() {
        OrgHead head = new OrgHead();
        OrgNodeInList nodeInList = new OrgNodeInList(1, 1, head);
        head.setTitle("Title");

        String out = parserWriter.whiteSpacedFilePreface("Content.\n") + parserWriter.whiteSpacedHead(nodeInList, false);
        Assert.assertEquals("Content.\n\n* Title\n", out);
    }

    @Test
    public void testContentLineLineNote() {
        OrgHead head = new OrgHead();
        OrgNodeInList nodeInList = new OrgNodeInList(1, 1, head);
        head.setTitle("Title");

        String out = parserWriter.whiteSpacedFilePreface("Content.\n\n") + parserWriter.whiteSpacedHead(nodeInList, false);
        Assert.assertEquals("Content.\n\n* Title\n", out);
    }

    @Test
    public void testContentLineLineLineNote() {
        OrgHead head = new OrgHead();
        OrgNodeInList nodeInList = new OrgNodeInList(1, 1, head);
        head.setTitle("Title");

        String out = parserWriter.whiteSpacedFilePreface("Content.\n\n\n") + parserWriter.whiteSpacedHead(nodeInList, false);
        Assert.assertEquals("Content.\n\n* Title\n", out);
    }

    @Test
    public void testNoteNote() {
        OrgNodeInList nodeInList1 = new OrgNodeInList(1, 1, new OrgHead("Title 1"));
        OrgNodeInList nodeInList2 = new OrgNodeInList(2, 1, new OrgHead("Title 2"));

        String out = parserWriter.whiteSpacedFilePreface("") + parserWriter.whiteSpacedHead(nodeInList1, false) + parserWriter.whiteSpacedHead(nodeInList2, false);
        Assert.assertEquals("* Title 1\n* Title 2\n", out);
    }

    @Test
    public void testNoteBodyWithNewLinesNote() {
        OrgHead head1 = new OrgHead("Title 1");
        head1.setContent("\n\n");
        OrgNodeInList nodeInList1 = new OrgNodeInList(1, 1, head1);

        OrgHead head2 = new OrgHead("Title 2");
        OrgNodeInList nodeInList2 = new OrgNodeInList(2, 1, head2);


        String out = parserWriter.whiteSpacedFilePreface("") + parserWriter.whiteSpacedHead(nodeInList1, false) + parserWriter.whiteSpacedHead(nodeInList2, false);
        Assert.assertEquals("* Title 1\n\n\n\n\n\n* Title 2\n", out);
    }

    @Test
    public void testNoteBodyNote() {
        OrgHead head1 = new OrgHead("Title 1");
        head1.setContent("\n\nContent");
        OrgNodeInList nodeInList1 = new OrgNodeInList(1, 1, head1);

        OrgHead head2 = new OrgHead("Title 2");
        OrgNodeInList nodeInList2 = new OrgNodeInList(2, 1, head2);

        String out = parserWriter.whiteSpacedFilePreface("") + parserWriter.whiteSpacedHead(nodeInList1, false) + parserWriter.whiteSpacedHead(nodeInList2, false);
        Assert.assertEquals("* Title 1\n\n\n\nContent\n\n* Title 2\n", out);
    }

    /*
     * TODO: Simplify data used for tests like these.
     * (no need for "- State" crap if we're just testing timestamp)
     */
    @Test
    public void testComplicatedTimestamp() throws IOException {
        String fileContent =
                "** TODO Shave\n" +
                "SCHEDULED: <2009-10-17 Sat .+2d/4d>\n" +
                "   - State \"DONE\"       from \"TODO\"       [2009-10-15 Thu]\n" +
                "   - State \"DONE\"       from \"TODO\"       [2009-10-12 Mon]\n" +
                "   :PROPERTIES:\n" +
                "   :STYLE:    habit\n" +
                "   :LAST_REPEAT: [2009-10-19 Mon 00:36]\n" +
                "   :END:";

        OrgParsedFile file = parserBuilder.setInput(fileContent).build().parse();

        Assert.assertEquals("", file.getFile().getPreface());
        Assert.assertEquals("Shave", file.getHeadsInList().get(0).getHead().getTitle());
        Assert.assertEquals("<2009-10-17 Sat .+2d/4d>", file.getHeadsInList().get(0).getHead().getScheduled().getStartTime().toString());

        Assert.assertEquals("** TODO Shave\n" +
                            "SCHEDULED: <2009-10-17 Sat .+2d/4d>\n" +
                            "   - State \"DONE\"       from \"TODO\"       [2009-10-15 Thu]\n" +
                            "   - State \"DONE\"       from \"TODO\"       [2009-10-12 Mon]\n" +
                            "   :PROPERTIES:\n" +
                            "   :STYLE:    habit\n" +
                            "   :LAST_REPEAT: [2009-10-19 Mon 00:36]\n" +
                            "   :END:\n\n", file.toString());
    }

    @Test
    public void testContentBeforeFirstEntry() throws IOException {
        String fileContent =
                "Text on top!\n" +
                "\n" +
                "* This is the first line with tag at the end of the line                :tag1:\n" +
                "** [#A] Second heading with priority                                    :tag2:\n" +
                "*** TODO   [#B]Third heading with spaced priority                       :tag3:\n" +
                "**** DONE Heading number 4 :tag:todo:\n";

        OrgParsedFile file = parserBuilder.setInput(fileContent).build().parse();

        Assert.assertEquals("Text on top!", file.getFile().getPreface());

        Assert.assertEquals("This is the first line with tag at the end of the line", file.getHeadsInList().get(0).getHead().getTitle());
        Assert.assertEquals("Second heading with priority", file.getHeadsInList().get(1).getHead().getTitle());
        Assert.assertEquals("Third heading with spaced priority", file.getHeadsInList().get(2).getHead().getTitle());
        Assert.assertEquals("Heading number 4", file.getHeadsInList().get(3).getHead().getTitle());
    }

    @Test
    public void testTags() throws IOException {
        OrgParsedFile file = parserBuilder.setInput("* TODO Test title parsing for tags:     lala : lala :t1:t2: lala :t3:t4:\n").build().parse();

        Assert.assertEquals(2, file.getHeadsInList().get(0).getHead().getTags().size());
        Assert.assertTrue(file.getHeadsInList().get(0).getHead().getTags().contains("t3"));
        Assert.assertTrue(file.getHeadsInList().get(0).getHead().getTags().contains("t4"));
    }


//    @Test
//    public void testClockIndent1() throws IOException {
//        Assert.assertEquals("* TODO Note\n  CLOCK: [2014-12-04 чет 16:50]--[2014-12-04 чет 16:56]\n",
//                parsed("* TODO Note\n  CLOCK: [2014-12-04 чет 16:50]--[2014-12-04 чет 16:56]"));
//    }
//
//    @Test
//    public void testClockIndent2() throws IOException {
//        Assert.assertEquals("* TODO Note\n  CLOCK: [2015-01-31 Sat 14:07]--[2015-01-31 Sat 14:08] =>  0:01\n",
//                parsed("* TODO Note\n  CLOCK: [2015-01-31 Sat 14:07]--[2015-01-31 Sat 14:08] =>  0:01"));
//    }

    @Test
    public void testFileSettingsAlwaysNonNull() throws IOException {
        OrgParsedFile file = parserBuilder.setInput("* TODO Note").build().parse();
        Assert.assertNotNull(file.getFile().getSettings());
        Assert.assertNull(file.getFile().getSettings().getTitle());
    }

    @Test
    public void testFileSettingsTitle() throws IOException {
        OrgParsedFile file = parserBuilder.setInput(OrgFileSettings.TITLE + " Org Title\n\n* TODO Note\n").build().parse();
        Assert.assertNotNull(file.getFile().getSettings());
        Assert.assertNotNull(file.getFile().getSettings().getTitle());
        Assert.assertEquals("Org Title", file.getFile().getSettings().getTitle());
    }

    @Test
    public void testIndented() throws IOException {
        OrgParsedFile file = parserBuilder.setInput("* TODO Title\n  SCHEDULED: <2015-02-11 Wed +1d>").build().parse();
        Assert.assertTrue(file.getFile().getSettings().isIndented());
    }

    @Test
    public void testIndentedAfterCounting() throws IOException {
        OrgParsedFile file = parserBuilder.setInput("* TODO Title\n  SCHEDULED: <2015-02-11 Wed +1d>\nDEADLINE: <2015-02-08 Sun>\n  CLOSED: [2015-02-08 Sun]").build().parse();
        Assert.assertTrue(file.getFile().getSettings().isIndented());
    }

    @Test
    public void testNotIndented() throws IOException {
        OrgParsedFile file = parserBuilder.setInput("* TODO Title\nSCHEDULED: <2015-02-11 Wed +1d>").build().parse();
        Assert.assertFalse(file.getFile().getSettings().isIndented());
    }

    @Test
    public void testNotIndentedAfterCounting() throws IOException {
        OrgParsedFile file = parserBuilder.setInput("* TODO Title\nSCHEDULED: <2015-02-11 Wed +1d>\nDEADLINE: <2015-02-08 Sun>\n  CLOCK: [2015-02-07 Sat 20:47]--[2015-02-07 Sat 20:47] =>  0:00").build().parse();
        Assert.assertFalse(file.getFile().getSettings().isIndented());
    }

    @Test
    public void testRich1() throws IOException {
        String str = "* TODO Rich markup :tag:\n" +
                     "  DEADLINE: <2015-02-08 Sun> SCHEDULED: <2015-02-11 Wed +1d>\n" +
                     "  :LOGBOOK:  \n" +
                     "  - Note taken on [2015-02-07 Sat 20:58] \\\\\n" +
                     "    This is a multi\n" +
                     "    line note.\n" +
                     "  - State \"DONE\"       from \"TODO\"       [2015-02-07 Sat 20:47]\n" +
                     "  CLOCK: [2015-02-07 Sat 20:47]--[2015-02-07 Sat 20:47] =>  0:00\n" +
                     "  - State \"DONE\"       from \"TODO\"       [2015-02-07 Sat 20:47]\n" +
                     "  :END:      \n" +
                     "  - Note taken on [2015-02-07 Sat 20:43] \\\\\n" +
                     "    note in logbook\n" +
                     "  - State \"DONE\"       from \"TODO\"       [2015-02-07 Sat 20:42]\n" +
                     "  - New deadline from \"2015-02-07 Sat\" on [2015-02-07 Sat 20:42]\n" +
                     "  - Rescheduled from \"2015-02-07 Sat +1d\" on [2015-02-07 Sat 20:41]\n" +
                     "  CLOCK: [2015-02-07 Sat 18:16]--[2015-02-07 Sat 20:42] =>  2:26\n" +
                     "  - State \"DONE\"       from \"TODO\"       [2015-02-07 Sat 17:24]\n" +
                     "  CLOCK: [2015-02-07 Sat 17:24]--[2015-02-07 Sat 17:24] =>  0:00\n" +
                     "  - State \"DONE\"       from \"TODO\"       [2015-02-07 Sat 17:24]\n" +
                     "  - State \"DONE\"       from \"TODO\"       [2015-02-07 Sat 17:24]\n" +
                     "  - State \"DONE\"       from \"TODO\"       [2015-02-07 Sat 17:24]\n" +
                     " CLOCK: [2015-02-07 Sat 17:24]--[2015-02-07 Sat 17:24] =>  0:00\n" +
                     "  CLOCK: [2015-02-07 Sat 17:24]--[2015-02-07 Sat 17:24] =>  0:00\n" +
                     "  - State \"DONE\"       from \"TODO\"       [2015-02-07 Sat 17:23]\n" +
                     "  - State \"DONE\"       from \"TODO\"       [2015-02-07 Sat 17:23]\n" +
                     "  CLOCK: [2015-02-07 Sat 17:23]--[2015-02-07 Sat 17:23] =>  0:00\n" +
                     "  - State \"DONE\"       from \"TODO\"       [2015-02-07 Sat 17:22]\n" +
                     "  :PROPERTIES:\n" +
                     "  :LAST_REPEAT: [2015-02-07 Sat 20:49]\n" +
                     "  :blah:     blah-value\n" +
                     "  :END:\n" +
                     "\n" +
                     "Logging within LOGBOOK or not, depending on org-log-into-drawer.\n";

        Assert.assertEquals(str + "\n",  parsed(str));

//        OrgFileWithHeads file = builder.setData(str).build().parse();
////        Assert.assertNotNull(file.getFile().getSettings());
////        Assert.assertNotNull(file.getFile().getSettings().getTitle());
////        Assert.assertEquals("Org Title", file.getFile().getSettings().getTitle());
//        Assert.assertEquals(str + "\n", file.toString());
    }

    @Test
    public void testClockInLogbook() throws IOException {
        String str = "*** TODO Go to gym\n" +
                     "SCHEDULED: <2016-06-06 Mon 10:00 .+2d/3d>\n" +
                     ":PROPERTIES:\n" +
                     ":STYLE:    habit\n" +
                     ":LAST_REPEAT: [2016-05-30 Mon 13:25]\n" +
                     ":END:\n" +
                     ":LOGBOOK:\n" +
                     "- State \"DONE\"       from \"TODO\"       [2016-05-30 Mon 13:25]\n" +
                     "CLOCK: [2016-05-30 Mon 12:12]--[2016-05-30 Mon 13:25] =>  1:13\n" +
                     "- State \"DONE\"       from \"TODO\"       [2015-12-21 Mon 14:45]\n" +
                     ":END:\n\n";

        Assert.assertEquals(str,  parsed(str));

    }

    @Test
    public void testNoLogbook() throws IOException {
        String str = "* Note 1\n" +
                     "  SCHEDULED: <2016-06-04 Sat>\n" +
                     "  - Note taken on [2016-06-04 Sat 17:18] \\\\\n" +
                     "    Note\n" +
                     "  CLOCK: [2016-06-04 Sat 17:18]\n" +
                     "  CLOCK: [2016-06-04 Sat 17:17]--[2016-06-04 Sat 17:18] =>  0:01\n" +
                     "\n" +
                     "This is Note 1's content\n\n";

        Assert.assertEquals(str,  parsed(str));
    }

    @Test
    public void testPropertyAlign() throws IOException {
        String str = "* Note\n" +
                     ":PROPERTIES:\n" +
                     ":STYLE:    habit\n" +
                     ":LAST_REPEAT: [2016-05-30 Mon 13:25]\n" +
                     ":END:\n\n";

        Assert.assertEquals(str, parsed(str));
    }

    private String parsed(String original) throws IOException {
        return parserBuilder.setInput(original).build().parse().toString();
    }
}
