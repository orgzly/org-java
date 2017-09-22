package com.orgzly.org.utils;

import com.orgzly.org.datetime.OrgDateTime;
import com.orgzly.org.datetime.OrgRange;

import java.util.Set;

/**
 * State setting logic.
 */
public class StateChangeLogic {
    private final Set<String> doneKeywords;

    private String state;

    private OrgRange scheduled;
    private OrgRange deadline;
    private OrgRange closed;

    public StateChangeLogic(Set<String> doneKeywords) {
        this.doneKeywords = doneKeywords;
    }

    public void setState(String targetState, String originalState, OrgRange scheduledTime, OrgRange deadlineTime) {
        if (targetState == null) {
            throw new IllegalArgumentException("Target state cannot be null");
        }

        this.scheduled = scheduledTime;
        this.deadline = deadlineTime;

        if (doneKeywords.contains(targetState)) {
            if (! doneKeywords.contains(originalState)) {
                /* From to-do-type to done-type state.
                 * Try to shift times. If successful (there was a repeater), keep the original
                 * state and remove closed time. If times were not shifted (there was no repeater)
                 * update the state and set closed time.
                 */

                boolean shifted = false;

                if (scheduled != null) {
                    if (scheduled.shift()) {
                        shifted = true;
                    }
                }

                if (deadline != null) {
                    if (deadline.shift()) {
                        shifted = true;
                    }
                }

                if (shifted) {
                    /* Keep the original state and remove the closed time */
                    state = originalState;
                    closed = null;
                } else {
                    /* Set state and closed time. */
                    state = targetState;
                    closed = new OrgRange(new OrgDateTime(false));
                }

            } else {
                /* From done-type to done-type state.
                 * Set the state and update the closed time.
                 */
                state = targetState;
                closed = new OrgRange(new OrgDateTime(false));
            }

        } else {
            /* Target keyword is a to-do-type state.
             * Set state and remove closed time.
             */
            state = targetState;
            closed = null;
        }
    }

    public String getState() {
        return state;
    }

    public OrgRange getScheduled() {
        return scheduled;
    }

    public OrgRange getDeadline() {
        return deadline;
    }

    public OrgRange getClosed() {
        return closed;
    }
}