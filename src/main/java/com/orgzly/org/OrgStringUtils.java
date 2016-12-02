package com.orgzly.org;

import java.util.Set;

public class OrgStringUtils {
    public static String trimLines(String str) {
        return str.replaceFirst("\n+[\\s]*$", "").replaceFirst("^[\\s]*\n", "");
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static String join(Set<String> set, String d) {
        StringBuilder result = new StringBuilder();

        int i = 0;
        for (String str: set) {
            result.append(str);

            if (i++ < set.size() - 1) { /* Not last. */
                result.append(d);
            }
        }

        return result.toString();
    }
}
