package com.orgzly.org;

import com.orgzly.org.parser.OrgParsedFile;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class OrgPropertiesTest extends OrgTestParser {
    @Test
    public void appendToValue() throws IOException {
        OrgProperties properties = new OrgProperties();

        properties.put("var", "foo=1");
        properties.put("var+", "bar=2");

        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("foo=1 bar=2", properties.get("var"));
    }

    @Test
    public void removeProperty() throws IOException {
        OrgProperties properties = new OrgProperties();

        properties.put("LAST_REPEAT", "[2009-10-19 Mon 00:36]");
        properties.remove("LAST_REPEAT");

        Assert.assertEquals(0, properties.size());
        Assert.assertNull(properties.get("LAST_REPEAT"));
    }

    @Test
    public void indented() throws IOException {
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
        Assert.assertEquals("habit", head.getProperties().get("STYLE"));
        Assert.assertEquals("[2009-10-19 Mon 00:36]", head.getProperties().get("LAST_REPEAT"));
    }

    @Test
    public void duplicates() throws IOException {
        String fileContent =
                "** TODO Note\n" +
                        ":PROPERTIES:\n" +
                        ":KEY: a\n" +
                        ":KEY: b\n" +
                        ":END:";

        OrgParsedFile file = parserBuilder.setInput(fileContent).build().parse();
        OrgHead head = file.getHeadsInList().get(0).getHead();

        Assert.assertTrue(head.hasProperties());
        Assert.assertEquals(2, head.getProperties().size());
        Assert.assertEquals("b", head.getProperties().get("KEY"));
    }

    @Test
    public void compositeFromSecond() throws IOException {
        String fileContent =
                "** TODO Note\n" +
                        ":PROPERTIES:\n" +
                        ":KEY:  a\n" +
                        ":KEY+: b\n" +
                        ":END:";

        OrgParsedFile file = parserBuilder.setInput(fileContent).build().parse();
        OrgHead head = file.getHeadsInList().get(0).getHead();

        Assert.assertTrue(head.hasProperties());
        Assert.assertEquals(2, head.getProperties().size());
        Assert.assertEquals("KEY", head.getProperties().get(0).getName());
        Assert.assertEquals("a", head.getProperties().get(0).getValue());
        Assert.assertEquals("KEY+", head.getProperties().get(1).getName());
        Assert.assertEquals("b", head.getProperties().get(1).getValue());
        Assert.assertEquals("a b", head.getProperties().get("KEY"));
    }

    @Test
    public void compositeAll() throws IOException {
        String fileContent =
                "** TODO Note\n" +
                        ":PROPERTIES:\n" +
                        ":KEY+: a\n" +
                        ":KEY+: b\n" +
                        ":END:";

        OrgParsedFile file = parserBuilder.setInput(fileContent).build().parse();
        OrgHead head = file.getHeadsInList().get(0).getHead();

        Assert.assertTrue(head.hasProperties());
        Assert.assertEquals(2, head.getProperties().size());
        Assert.assertEquals("KEY+", head.getProperties().get(0).getName());
        Assert.assertEquals("a", head.getProperties().get(0).getValue());
        Assert.assertEquals("KEY+", head.getProperties().get(1).getName());
        Assert.assertEquals("b", head.getProperties().get(1).getValue());
        Assert.assertEquals("a b", head.getProperties().get("KEY"));
    }

    @Test
    public void compositeWithBreak() throws IOException {
        String fileContent =
                "** TODO Note\n" +
                        ":PROPERTIES:\n" +
                        ":KEY+: a\n" +
                        ":KEY+: b\n" +
                        ":KEY:  c\n" +
                        ":KEY+: d\n" +
                        ":END:";

        OrgParsedFile file = parserBuilder.setInput(fileContent).build().parse();
        OrgHead head = file.getHeadsInList().get(0).getHead();

        Assert.assertTrue(head.hasProperties());
        Assert.assertEquals(4, head.getProperties().size());
        Assert.assertEquals("c d", head.getProperties().get("KEY"));
    }
}
