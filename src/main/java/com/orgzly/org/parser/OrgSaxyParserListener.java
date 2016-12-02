package com.orgzly.org.parser;

import com.orgzly.org.OrgFile;

import java.io.IOException;

public interface OrgSaxyParserListener {
    /**
     * Called for each new heading found.
     *
     * @param node Node in list.
     */
    void onHead(OrgNodeInList node) throws IOException;

    /**
     * Called last, after everything has been parsed.
     *
     * @param file
     */
    void onFile(OrgFile file) throws IOException;
}
