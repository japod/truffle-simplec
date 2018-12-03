package com.japod.simplec.truffle;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.RootNode;

public class CRootNode extends RootNode {

    @Children CNode[] declNodes;

    public CRootNode(CNode[] declNodes) {
        super(null);
        this.declNodes = declNodes;
    }

    @Override
    @ExplodeLoop
    public Object execute(VirtualFrame frame) {
        GlobalSpace global = new GlobalSpace();

        for (CNode n : declNodes) {
            n.execute(global);
        }

        CNode mainNode = global.getMain();
        mainNode.execute(global);
        return null;
    }

}
