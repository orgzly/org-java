package com.orgzly.orgjava.parser;

import com.orgzly.orgjava.OrgHead;

public class OrgNodeInList extends OrgNode {
    private int position;

    public OrgNodeInList(int position, int level, OrgHead head) {
        this.level = level;
        this.position = position;
        this.head = head;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(String.format("%2d ", position));

        for (int i = 0; i < level; i++) {
            s.append("*");
        }

        if (level > 0) {
            s.append(" ");
        }

        s.append(head != null ? head.getTitle() : "-");

        return s.toString();
    }
}
