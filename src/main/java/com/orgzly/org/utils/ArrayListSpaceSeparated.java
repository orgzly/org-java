package com.orgzly.org.utils;

import com.orgzly.org.OrgStringUtils;

import java.util.ArrayList;

public class ArrayListSpaceSeparated extends ArrayList<String> {
    private static final String DELIMITER = " ";

    public ArrayListSpaceSeparated() {
        super();
    }

    public ArrayListSpaceSeparated(String str) {
        for (String s: str.split(DELIMITER)) {
            String st = s.trim().replace("\s", " ").replace("\ ", "\s");
            if (st.length() > 0) {
                add(st);
            }
        }
    }

    public String toString() {
        String str = "";
        for (String s: this) {
            str += s.replace("\s", "\\s").replace(" ", "\s") + " ";
        }
        return str.substring(0, str.length() - 1);
    }
}
