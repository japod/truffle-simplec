package com.japod.simplec.truffle;

import com.oracle.truffle.api.nodes.Node;

public class CCallNode extends CNode {

    private final String name;
    @Children Node[] argNodes;

    public CCallNode(String name, Node[] argNodes) {
        this.name = name;
        this.argNodes = argNodes;
    }

    @Override
    public void execute(GlobalSpace globalSpace) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
