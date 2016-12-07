package com.orgzly.org.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class OrgParser {
    protected OrgParserSettings settings;

    public abstract OrgParsedFile parse() throws IOException;

    public static class Builder {
        private OrgParserSettings settings;

        private Reader reader;

        private OrgSaxyParserListener listener;
        private OrgNestedSetParserListener nestedSetListener;

        public Builder() {
            this.settings = new OrgParserSettings();
        }

        /**
         * Copy settings as some of the parsers will use other parsers and
         * changing settings by setting the listener.
         *
         * @param settings Parser settings
         */
        public Builder(OrgParserSettings settings) {
            this.settings = new OrgParserSettings(settings);
        }

        public Builder setTodoKeywords(String[] keywords) {
            settings.todoKeywords = new HashSet<>(Arrays.asList(keywords));
            return this;
        }

        public Builder setTodoKeywords(Set<String> keywords) {
            settings.todoKeywords = keywords;
            return this;
        }

        public Builder setDoneKeywords(String[] keywords) {
            settings.doneKeywords = new HashSet<>(Arrays.asList(keywords));
            return this;
        }

        public Builder setDoneKeywords(Set<String> keywords) {
            settings.doneKeywords = keywords;
            return this;
        }

        public Builder setListener(OrgSaxyParserListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setListener(OrgNestedSetParserListener listener) {
            this.nestedSetListener = listener;
            return this;
        }

        public Builder setInput(Reader reader) {
            this.reader = reader;
            return this;
        }

        public Builder setInput(String data) {
            this.reader = new StringReader(data);
            return this;
        }

        public OrgParser build() {
            if (reader == null) {
                throw new IllegalStateException("Reader not set. Use setInput() before building the parser.");
            }

            if (listener != null) {
                return new OrgSaxyParser(settings, reader, listener);

            } else if (nestedSetListener != null) {
                return new OrgNestedSetParser(settings, reader, nestedSetListener);

            } else {
                return new OrgDomyParser(settings, reader);
            }
        }
    }
}
