package com.orgzly.org;

import java.util.Collection;

public class OrgStringUtils {
    public static String trimLines(String str) {
        return str.replaceFirst("\n+[\\s]*$", "").replaceFirst("^[\\s]*\n", "");
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static String join(Collection set, String d) {
        StringBuilder result = new StringBuilder();

        int i = 0;
        for (Object str: set) {
            result.append(str);

            if (i++ < set.size() - 1) { /* Not last. */
                result.append(d);
            }
        }

        return result.toString();
    }



    public static int stringWidth(String str) {
        int total = 0;

        for (int i = 0; i < str.length(); ) {
            int cp = str.codePointAt(i);

            int width = codePointWidth(cp);

            // System.out.printf("Code point %c width: %d\n", cp, width);

            total += width;

            i += Character.charCount(cp);
        }

        return total;
    }

    private static int codePointWidth(int cp) {
        return EastAsianWidth.CP.contains(cp) ? 2 : 1;
    }
}
