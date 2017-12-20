package com.orgzly.org.datetime;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrgEffortTimeTest {

    @Test
    public void getTotalMinutes() {
        OrgEffortTime t = new OrgEffortTime(5, 10);
        assertEquals(310, t.getTotalMinutes());
    }

    @Test
    public void parse() {
        OrgEffortTime t = OrgEffortTime.parse("1:00");
        assertEquals(60, t.getTotalMinutes());

        t = OrgEffortTime.parse("0:45");
        assertEquals(45, t.getTotalMinutes());

        t = OrgEffortTime.parse(":45");
        assertEquals(45, t.getTotalMinutes());
    }
}