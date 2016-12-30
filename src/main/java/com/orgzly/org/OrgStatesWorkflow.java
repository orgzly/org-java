package com.orgzly.org;

import com.orgzly.org.utils.ArrayListSpaceSeparated;

/**
 * State keywords, single workflow.
 *
 * "NEXT" "FEEDBACK" "VERIFY" "|" "DONE" "DELEGATED"
 */
public class OrgStatesWorkflow {
    private ArrayListSpaceSeparated todoKeywords;
    private ArrayListSpaceSeparated doneKeywords;

    public OrgStatesWorkflow(ArrayListSpaceSeparated t, ArrayListSpaceSeparated d) {
        todoKeywords = t;
        doneKeywords = d;
    }

    public OrgStatesWorkflow(String s) {
        String st = s.trim();

        if (st.length() == 0) {
            todoKeywords = new ArrayListSpaceSeparated();
            doneKeywords = new ArrayListSpaceSeparated();

        } else {
            int bar = st.indexOf('|');

            if (bar == -1) { // No vertical bar - use last keyword as done state
                todoKeywords = new ArrayListSpaceSeparated(st);
                String last = todoKeywords.remove(todoKeywords.size() - 1);

                doneKeywords = new ArrayListSpaceSeparated();
                doneKeywords.add(last);

            } else {
                todoKeywords = new ArrayListSpaceSeparated(st.substring(0, bar));
                doneKeywords = new ArrayListSpaceSeparated(st.substring(bar+1));
            }
        }
    }

    public ArrayListSpaceSeparated getTodoKeywords() {
        return todoKeywords;
    }

    public ArrayListSpaceSeparated getDoneKeywords() {
        return doneKeywords;
    }

    public String toString() {
        return todoKeywords.toString() + " | " + doneKeywords.toString();
    }
}
