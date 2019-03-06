package com.orgzly.org;

import com.orgzly.org.datetime.OrgRange;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class OrgActiveTimestamps {
    public static List<OrgRange> parse(String str) {
        List<OrgRange> timestamps = new ArrayList<>();

        if (str != null && str.length() > 0) {
            Matcher m = OrgPatterns.DT_OR_RANGE_P.matcher(str);

            while (m.find()) {
                OrgRange range = OrgRange.parse(m.group());

                if (range.getStartTime().isActive()) {
                    timestamps.add(range);
                }
            }
        }

        return timestamps;
    }
}
