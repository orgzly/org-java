package com.orgzly.org.parser;

import com.orgzly.org.OrgFile;
import com.orgzly.org.OrgHead;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

/**
 * Parser using {@link OrgSaxyParser}.
 *
 * Wraps {@link OrgHead} inside {@link OrgNodeInSet} which contains additional values
 * for nested set model (https://en.wikipedia.org/wiki/Nested_set_model).
 *
 */
public class OrgNestedSetParser extends OrgParser {

    public static final int STARTING_LEVEL = 0;
    public static final long STARTING_VALUE = 1;

    /**
     * Allow some space between values, for cheaper inserts.
     *
     * The only reason we do this now is to test that these do not *have* to be true:
     * - "lft + 1 == rgt" for a single node
     * - "rgt + 1 == lft" for two siblings
     *
     * TODO: Not used.
     */
    private static final int GAP = 5;


    private Reader reader;
    private OrgNestedSetParserListener listener;

    public OrgNestedSetParser(OrgParserSettings settings, Reader reader, OrgNestedSetParserListener listener) {
        this.settings = settings;
        this.reader = reader;
        this.listener = listener;
    }

    @Override
    public OrgParsedFile parse() throws IOException {
        final Stack<OrgNodeInSet> stack = new Stack<>();

        /* Add root. */
        stack.push(new OrgNodeInSet(STARTING_LEVEL, STARTING_VALUE, new OrgHead()));

        Builder builder = new Builder(settings)
                .setInput(reader)
                .setListener(new OrgSaxyParserListener() {

            int prevLevel = STARTING_LEVEL;
            long sequence = STARTING_VALUE;

            @Override
            public void onHead(OrgNodeInList thisNode) throws IOException {
                if (prevLevel < thisNode.getLevel()) {
                    /*
                     * This is a descendant of previous node.
                     *
                     *      *-----
                     *        *---  <--
                     */

                    /* Put the current thisNode on the stack. */
                    sequence += GAP;
                    stack.push(new OrgNodeInSet(thisNode.getLevel(), sequence, thisNode.getHead()));

                } else if (prevLevel == thisNode.getLevel()) {
                    /*
                     * This is a sibling, which means that the last node visited can be completed.
                     * Take it off the stack, update its rgt value and announce it.
                     *
                     *      *-----  onNode()
                     *      *-----  <--
                     */

                    OrgNodeInSet nodeFromStack = stack.pop();
                    sequence += GAP;
                    nodeFromStack.setRgt(sequence);
                    calculateAndSetDescendantsCount(nodeFromStack, GAP);
                    listener.onNode(nodeFromStack);

                    /* Put the current thisNode on the stack. */
                    sequence += GAP;
                    stack.push(new OrgNodeInSet(thisNode.getLevel(), sequence, thisNode.getHead()));

                } else {
                    /*
                     * Note has lower level then the previous one - we're out of the set.
                     * Start popping the stack, up to and including the thisNode with the same level.
                     *
                     *      *-----  onNode()
                     *        *---  onNode()
                     *      *-----  <--
                     */

                    while (!stack.empty()) {
                        OrgNodeInSet nodeFromStack = stack.peek();

                        if (nodeFromStack.getLevel() >= thisNode.getLevel()) {
                            stack.pop();

                            sequence += GAP;
                            nodeFromStack.setRgt(sequence);
                            calculateAndSetDescendantsCount(nodeFromStack, GAP);
                            listener.onNode(nodeFromStack);

                        } else {
                            break;
                        }
                    }

                    /* Put the current thisNode on the stack. */
                    sequence += GAP;
                    stack.push(new OrgNodeInSet(thisNode.getLevel(), sequence, thisNode.getHead()));
                }

                prevLevel = thisNode.getLevel();

                // printState(thisNode, position);
            }

//            private void printState(OrgNodeInList nodeInList, int position) {
//                System.out.printf("pos:%d  level:%d  sequence:%d  level:%d  stack:%d\n",
//                        position, nodeInList.getLevel(), sequence, prevLevel, stack.size());
//
//                for (OrgNodeInSet node : stack) {
//                    System.out.println(node);
//                }
//            }

            @Override
            public void onFile(OrgFile file) throws IOException {
                /* Pop remaining nodes. */
                while (! stack.empty()) {
                    OrgNodeInSet nodeFromStack = stack.pop();
                    sequence += GAP;
                    nodeFromStack.setRgt(sequence);
                    calculateAndSetDescendantsCount(nodeFromStack, GAP);
                    listener.onNode(nodeFromStack);
                }

                listener.onFile(file);
            }
        });

        builder.build().parse();

        return null;
    }

    private void calculateAndSetDescendantsCount(OrgNodeInSet node, int gap) {
        int n = (int) (node.getRgt() - node.getLft() - gap) / ( 2 * gap );

        node.setDescendantsCount(n);
    }
}
