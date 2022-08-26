package com.orgzly.org;

public class OrgWriter {
    OrgWriter() {
    }

    public String str(OrgBlock block) {
        String name = block.getName();
        String content = block.getContent();

        return "#+begin_" + name + "\n" + content + "\n#+end_" + name;
    }
}
