package com.orgzly.orgjava.parser;

import com.orgzly.orgjava.OrgHead;

public class OrgNode { // TODO: Extend OrgHead instead?

    /** Heading's level, starting from 1 (number of stars). */
    protected int level;

    protected OrgHead head;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public OrgHead getHead() {
        return head;
    }

    public void setHead(OrgHead head) {
        this.head = head;
    }
}
