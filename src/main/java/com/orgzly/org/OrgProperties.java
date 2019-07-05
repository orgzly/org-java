package com.orgzly.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * a
 * b
 * b+
 * b+
 * c
 * d+
 * e
 * d+
 */
public class OrgProperties {
    /**
     * All properties, including duplicates.
     * a  -> value
     * a+ -> value
     * a  -> value
     * b  -> value
     * c+ -> value
     */
    private List<OrgProperty> list = new ArrayList<>();

    /**
     * Full values by actual names (without + suffix).
     * a -> full-value
     * b -> full-value
     * c -> full-value
     */
    private Map<String, String> values = new HashMap<>();

    /**
     * Mapping of used appending names (with + suffix) to actual names
     * a+ -> a
     * c+ -> c
     */
    private Map<String, String> appendingKeys = new HashMap<>();

    public void put(String name, String value) {
        list.add(new OrgProperty(name, value));

        if (name.length() > 2 && name.endsWith("+")) {
            String realName = name.substring(0, name.length()-1);

            appendingKeys.put(name, realName);

            String prevValue = values.get(realName);

            if (prevValue != null) {
                if (prevValue.length() > 0) {
                    prevValue += " " + value;
                } else {
                    prevValue += value;
                }
            } else {
                prevValue = value;
            }

            values.put(realName, prevValue);

        } else {
            values.put(name, value);
        }
    }

    public void remove(String name) {
        if (name == null || !containsKey(name)) {
            return;
        }

        List<OrgProperty> toRemove = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            OrgProperty property = list.get(i);

            if (name.equals(property.getName()) || (name + "+").equals(property.getName())) {
                toRemove.add(property);
            }
        }

        list.removeAll(toRemove);

        values.remove(name);
    }

    public void set(String name, String value) {
        if (name == null) {
            return;
        }

        if (containsKey(name)) {
            boolean foundFirst = false;
            List<OrgProperty> newList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                OrgProperty property = list.get(i);

                if (name.equals(property.getName()) || (name + "+").equals(property.getName())) {
                    // Replace the value of first occurrence
                    if (!foundFirst) {
                        newList.add(new OrgProperty(name, value));
                        foundFirst = true;
                    }
                } else {
                    newList.add(property);
                }
            }

            list.clear();
            list.addAll(newList);

        } else {
            list.add(new OrgProperty(name, value));
        }

        values.put(name, value);
    }

    public List<OrgProperty> getAll() {
        return list;
    }

    public OrgProperty get(int index) {
        return list.get(index);
    }

    public String get(String name) {
        String realName = appendingKeys.get(name);

        if (realName == null) {
            realName = name;
        }

       return values.get(realName);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(String name) {
        return values.containsKey(name);
    }
}
