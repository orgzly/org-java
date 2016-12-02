package com.orgzly.org;

/**
 * In-buffer settings.
 * http://orgmode.org/manual/In_002dbuffer-settings.html
 */
public class OrgFileSettings {
    public static final String TITLE = "#+TITLE:";
//    public static final String FILE_TAGS = "#+FILETAGS:";

    private String title;
//    private List<String> fileTags = new ArrayList<>();


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

        if (line.startsWith(TITLE)) {
            String val = line.substring(TITLE.length()).trim();

            if (val.length() > 0) {
                setTitle(val);
            }

            settingFound = true;
        }

        return settingFound;
    }
}
