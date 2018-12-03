
package com.japod.simplec.truffle;

public class CGlobalDefinitionNode extends CNode {

    private final String name;
    @Child private CNode body;

    public CGlobalDefinitionNode(String name, CNode body) {
        this.name = name;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public CNode getExecutable() {
        return body;
    }

    @Override
    public void execute(GlobalSpace gs) {
        gs.bind(this);
    }

}
