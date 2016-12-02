package com.orgzly.orgjava.parser;

import com.orgzly.orgjava.OrgFile;

import java.io.IOException;

public interface OrgNestedSetParserListener {
    void onNode(OrgNodeInSet node) throws IOException;

    void onFile(OrgFile file) throws IOException;
}
