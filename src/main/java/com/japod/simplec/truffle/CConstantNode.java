package com.japod.simplec.truffle;

public class CConstantNode extends CNode {
    final String value;

    public CConstantNode(String v) {
        this.value = v;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void execute(GlobalSpace globalSpace) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
