package com.orgzly.org.parser;

import com.orgzly.org.OrgFile;
import com.orgzly.org.OrgHead;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link OrgFile} with {@link OrgHead}s.
 */
public class OrgParsedFile { // TODO: Extend OrgFile instead?
    private OrgFile file;

    private List<OrgNodeInList> headsInList;

    public OrgParsedFile() {
        headsInList = new ArrayList<>();
    }

    public OrgFile getFile() {
        return file;
    }

    public void setFile(OrgFile file) {
        this.file = file;
    }

    public List<OrgNodeInList> getHeadsInList() {
        return headsInList;
    }

    public void addHead(OrgNodeInList nodeInList) {
        headsInList.add(nodeInList);
    }

    public String toString() {
        StringBuilder str = new StringBuilder();

        OrgParserWriter parserWriter = new OrgParserWriter();

        str.append(parserWriter.whiteSpacedFilePreface(file.getPreface()));

        for (OrgNodeInList nodeInList : headsInList) {
            str.append(parserWriter.whiteSpacedHead(nodeInList, file.getSettings().isIndented()));
        }

        return str.toString();
    }
}
