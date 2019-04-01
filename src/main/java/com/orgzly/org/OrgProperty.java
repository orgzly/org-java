package com.orgzly.org;

public class OrgProperty {
    private String name;
    private String value;

    public OrgProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
