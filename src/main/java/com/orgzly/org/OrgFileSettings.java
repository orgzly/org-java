package com.orgzly.org;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

/**
 * In-buffer settings.
 * http://orgmode.org/manual/In_002dbuffer-settings.html
 */
public class OrgFileSettings {
    public static final String TITLE = "TITLE";

    private String title;

    private HashMap<String, List<String>> otherKeywords;

    public OrgFileSettings() {
        otherKeywords = new HashMap<>();
    }

    /**
     * Are planning, property drawers etc. indented with spaced or not.
     * This is *not* org-indent-mode, it's *detected* while parsing.
     * It's usually true if org-indent-mode is off, false if org-indent-mode is on.
     */
    private boolean indented;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isIndented() {
        return indented;
    }

    public void setIndented(boolean indented) {
        this.indented = indented;
    }

    /**
     * Parse line looking for in-buffer setting.
     *
     * @param line line to parse
     * @return true if setting is found on the line, false otherwise
     */
    public boolean parseLine(String line) {
        boolean settingFound = false;

        Matcher matcher = OrgPatterns.KEYWORD_VALUE.matcher(line);
        if (matcher.matches()) {
            addKeywordSetting(matcher.group(1), matcher.group(2));
            settingFound = true;
        }

        return settingFound;
    }

    public List<String> getKeywordValues(String keyword) {
        if (!otherKeywords.containsKey(keyword))
            return null;
        return otherKeywords.get(keyword);
    }

    public String getLastKeywordValue(String keyword) {
        List<String> values = getKeywordValues(keyword);
        if (values == null || values.isEmpty())
            return null;
        return values.get(values.size() - 1);
    }

    private void addKeywordSetting(String keyword, String value) {
        if (keyword.equalsIgnoreCase(TITLE)) {
            title = value;

        } else {
            if (!otherKeywords.containsKey(keyword)) {
                otherKeywords.put(keyword, new ArrayList<>());
            }
            if (!value.isEmpty()) {
                otherKeywords.get(keyword).add(value);
            }
        }
    }

    public static OrgFileSettings fromPreface(String preface) {
        OrgFileSettings settings = new OrgFileSettings();


        BufferedReader reader = new BufferedReader(new StringReader(preface));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                settings.parseLine(line);
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return settings;
    }

}
