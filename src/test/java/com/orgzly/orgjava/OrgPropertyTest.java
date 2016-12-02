package com.orgzly.orgjava;

import com.orgzly.orgjava.parser.OrgParsedFile;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;


public class OrgPropertyTest extends OrgTestParser {
    @Test
    public void testProperties() throws IOException {
        String fileContent =
                "** TODO Note\n" +
                        ":PROPERTIES:\n" +
                        "   :STYLE:    habit\n" +
                        "   :LAST_REPEAT: [2009-10-19 Mon 00:36]\n" +
                        "   :END:";

        OrgParsedFile file = parserBuilder.setInput(fileContent).build().parse();
        OrgHead head = file.getHeadsInList().get(0).getHead();

        Assert.assertTrue(head.hasProperties());
        Assert.assertEquals(2, head.getProperties().size());
    }
}