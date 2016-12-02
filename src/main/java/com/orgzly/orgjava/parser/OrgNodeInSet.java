package com.orgzly.orgjava.parser;

import com.orgzly.orgjava.OrgHead;

public class OrgNodeInSet extends OrgNode {
    private long lft;
    private long rgt;

    private int descendantsCount;

    public OrgNodeInSet(int level, long lft, OrgHead head) {
        this.level = level;
        this.lft = lft;
        this.head = head;
    }

    public long getLft() {
        return lft;
    }

    public long getRgt() {
        return rgt;
    }

    public void setRgt(long rgt) {
        this.rgt = rgt;
    }

    public int getDescendantsCount() {
        return descendantsCount;
    }

    public void setDescendantsCount(int count) {
        this.descendantsCount = count;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(String.format("%2d %2d ", lft, rgt));

        for (int i = 0; i < level; i++) {
            s.append("*");
        }

        if (level > 0) {
            s.append(" ");
        }

        s.append(head.getTitle());

        return s.toString();
    }
}
