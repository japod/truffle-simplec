
package com.japod.simplec.truffle;

import com.oracle.truffle.api.nodes.ExplodeLoop;

public class CBlockNode extends CNode {

    @Children private CNode[] children;

    public CBlockNode(CNode[] children) {
        this.children = children;
    }

    @Override
    @ExplodeLoop
    public void execute(GlobalSpace globalSpace) {
        for (CNode n : children) {
            n.execute(globalSpace);
        }
    }
}
