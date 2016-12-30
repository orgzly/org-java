package com.orgzly.org;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class OrgStatesWorkflowTest {
    @Test
    public void testWorkflow1() {
        OrgStatesWorkflow workflow = new OrgStatesWorkflow("  TODO   NEXT |DONE  ");

        assertEquals(2, workflow.getTodoKeywords().size());
        assertEquals("TODO", workflow.getTodoKeywords().get(0));
        assertEquals("NEXT", workflow.getTodoKeywords().get(1));
        assertEquals(1, workflow.getDoneKeywords().size());
        assertEquals("DONE", workflow.getDoneKeywords().get(0));
    }

    @Test
    public void testWorkflow2() {
        OrgStatesWorkflow workflow = new OrgStatesWorkflow("TODO DONE");

        assertEquals(1, workflow.getTodoKeywords().size());
        assertEquals("TODO", workflow.getTodoKeywords().get(0));
        assertEquals(1, workflow.getDoneKeywords().size());
        assertEquals("DONE", workflow.getDoneKeywords().get(0));
    }

    @Test
    public void testWorkflow3() {
        OrgStatesWorkflow workflow = new OrgStatesWorkflow("DONE");

        assertEquals(0, workflow.getTodoKeywords().size());
        assertEquals(1, workflow.getDoneKeywords().size());
        assertEquals("DONE", workflow.getDoneKeywords().get(0));
    }

    @Test
    public void testWorkflow4() {
        OrgStatesWorkflow workflow = new OrgStatesWorkflow("TODO|");

        assertEquals(1, workflow.getTodoKeywords().size());
        assertEquals("TODO", workflow.getTodoKeywords().get(0));
        assertEquals(0, workflow.getDoneKeywords().size());
    }

    @Test
    public void testWorkflow5() {
        OrgStatesWorkflow workflow = new OrgStatesWorkflow("|DONE");

        assertEquals(0, workflow.getTodoKeywords().size());
        assertEquals(1, workflow.getDoneKeywords().size());
        assertEquals("DONE", workflow.getDoneKeywords().get(0));
    }

    @Test
    public void testWorkflow6() throws IOException {
        OrgStatesWorkflow workflow = new OrgStatesWorkflow("|");

        assertEquals(0, workflow.getTodoKeywords().size());
        assertEquals(0, workflow.getDoneKeywords().size());
    }
}