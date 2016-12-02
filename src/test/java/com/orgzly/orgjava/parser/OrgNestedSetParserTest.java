package com.orgzly.orgjava.parser;

import com.orgzly.orgjava.OrgFile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

@RunWith(value = Parameterized.class)
public class OrgNestedSetParserTest {
    private String data;
    private String expected;


    public OrgNestedSetParserTest(String data, String expected) {
        this.data = data;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{index}: Parsing {0} should return {1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {
                        "" +
                                "** A\n" +
                                "**** B\n" +
                                "*** C\n" +
                                "* D\n" +
                                "** E\n",
                        "" +
                                " 1 56 \n" +
                                " 6 31 ** A\n" +
                                "11 16 **** B\n" +
                                "21 26 *** C\n" +
                                "36 51 * D\n" +
                                "41 46 ** E\n"
                },
                {
                        "" +
                                "* A",
                        "" +
                                " 1 16 \n" +
                                " 6 11 * A\n"
                },
                {
                        "" +
                                "",
                        "" +
                                " 1  6 \n"
                },
                {
                        "" +
                                "* A\n" +
                                "* B\n",
                        "" +
                                " 1 26 \n" +
                                " 6 11 * A\n" +
                                "16 21 * B\n"
                }
        });
    }

    @Test
    public void testNestedSetModelParser() throws IOException {
        final TreeMap<Long, OrgNodeInSet> map = new TreeMap<>();

        OrgParser.Builder parserBuilder = new OrgParser.Builder()
                .setTodoKeywords(new String[]{"TODO"})
                .setDoneKeywords(new String[]{"DONE"})
                .setInput(data)
                .setListener(new OrgNestedSetParserListener() {
                    @Override
                    public void onNode(OrgNodeInSet node) {
                        map.put(node.getLft(), node);
                    }

                    @Override
                    public void onFile(OrgFile file) {
                    }
                });

        OrgParser parser = parserBuilder.build();

        parser.parse();

        StringBuilder s = new StringBuilder();
        for (OrgNodeInSet node: map.values()) {
            s.append(node).append("\n");
        }
        String actual = s.toString();

        assertEquals(expected, actual);
    }
}