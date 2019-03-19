package com.orgzly.org.utils;

import com.orgzly.org.datetime.OrgDateTime;
import com.orgzly.org.datetime.OrgRange;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * State setting logic.
 */
public class StateChangeLogic {
    private final Collection<String> doneKeywords;

    private String state;

    private OrgRange scheduled;
    private OrgRange deadline;
    private List<OrgRange> timestamps;
    private OrgRange closed;

    private boolean shifted = false;

    public StateChangeLogic(Collection<String> doneKeywords) {
        this.doneKeywords = doneKeywords;
    }

    public void setState(
            String targetState,
            String originalState,
            OrgRange scheduledTime,
            OrgRange deadlineTime) {

        setState(targetState, originalState, scheduledTime, deadlineTime, new ArrayList<>());
    }

    public void setState(
            String targetState,
            String originalState,
            OrgRange scheduledTime,
            OrgRange deadlineTime,
            List<OrgRange> timestamps) {

        this.scheduled = scheduledTime;
        this.deadline = deadlineTime;
        this.timestamps = timestamps;

        if (targetState != null && doneKeywords.contains(targetState)) {
            if (! doneKeywords.contains(originalState)) { // to-do -> done
                /*
                 * Try to shift times. If successful (there was a repeater), keep the original
                 * state and remove closed time. If times were not shifted (there was no repeater)
                 * update the state and set closed time.
                 */

                if (scheduled != null) {
                    if (scheduled.shift()) {
                        shifted = true;
                    }
                }

                if (deadline != null) {
                    if (deadline.shift()) {

                        // Scheduled time exists but has no repeater - remove it
                        if (scheduled != null && !shifted) {
                            scheduled = null;
                        }

                        shifted = true;
                    }
                }

                for (OrgRange timestamp: timestamps) {
                    if (timestamp.shift()) {
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

            } else { // done -> done
                /*
                 * Set the state and update the closed time.
                 */
                state = targetState;
                closed = new OrgRange(new OrgDateTime(false));
            }

        } else { // -> to-do
            /*
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

    public List<OrgRange> getTimestamps() {
        return timestamps;
    }

    public OrgRange getClosed() {
        return closed;
    }

    public boolean isShifted() {
        return shifted;
    }
}