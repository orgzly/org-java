package com.orgzly.orgjava.parser;

import com.orgzly.orgjava.OrgFile;

import java.io.IOException;
import java.io.Reader;

/**
 * Parse complete input and return it as {@link OrgParsedFile}.
 */
class OrgDomyParser extends OrgParser {
    private Reader reader;

    public OrgDomyParser(OrgParserSettings settings, Reader reader) {
        this.settings = settings;
        this.reader = reader;
    }

    @Override
    public OrgParsedFile parse() throws IOException {
        final OrgParsedFile parsedFile = new OrgParsedFile();

        OrgParser parser = new Builder(settings)
                .setInput(reader)
                .setListener(new OrgSaxyParserListener() {
                    @Override
                    public void onHead(OrgNodeInList nodeInList) throws IOException {
                        parsedFile.addHead(nodeInList);
                    }

                    @Override
                    public void onFile(OrgFile file) throws IOException {
                        parsedFile.setFile(file);
                    }
                })
                .build();

        parser.parse();

        return parsedFile;
    }
}
