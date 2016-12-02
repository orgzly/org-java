package com.orgzly.org;

/**
 * Org file with its preface (text before the first heading) and settings.
 */
public class OrgFile {

    /** In-buffer settings. */
    private OrgFileSettings settings;

    /** Text before the first heading. */
    private String preface;

    public OrgFile() {
	}

    public OrgFileSettings getSettings() {
        if (settings == null) {
            settings = new OrgFileSettings();
        }
        return settings;
    }

    /**
     * @return Text before first {@link OrgHead} in the file.
     */
    public String getPreface() {
        return preface != null ? preface : "";
    }

    public void setPreface(String str) {
        preface = str;
    }

    public String toString() {
		return OrgFile.class.getSimpleName() + "[" + preface.length() + "]";
	}
}
