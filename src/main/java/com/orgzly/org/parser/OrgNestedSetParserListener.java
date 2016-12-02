package com.orgzly.org.parser;

import com.orgzly.org.OrgFile;

import java.io.IOException;

public interface OrgNestedSetParserListener {
    void onNode(OrgNodeInSet node) throws IOException;

    void onFile(OrgFile file) throws IOException;
}
