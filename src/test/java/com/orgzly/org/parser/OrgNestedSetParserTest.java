package com.orgzly.org.parser;

import com.orgzly.org.OrgFile;

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
                                "** A \n" +
                                "**** B\n" +
                                "*** C\n" +
                                "* D\n" +
                                "** E\n",
                        "" +
                                " 1 12 \n" +
                                " 2  7 ** A\n" +
                                " 3  4 **** B\n" +
                                " 5  6 *** C\n" +
                                " 8 11 * D\n" +
                                " 9 10 ** E\n"
                },
                {
                        "" +
                                "* A",
                        "" +
                                " 1  4 \n" +
                                " 2  3 * A\n"
                },
                {
                        "" +
                                "",
                        "" +
                                " 1  2 \n"
                },
                {
                        "" +
                                "* A\n" +
                                "* B\n",
                        "" +
                                " 1  6 \n" +
                                " 2  3 * A\n" +
                                " 4  5 * B\n"
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
