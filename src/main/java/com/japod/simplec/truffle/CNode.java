package com.japod.simplec.truffle;

import com.oracle.truffle.api.nodes.Node;

public abstract class CNode extends Node {

    public abstract void execute(GlobalSpace globalSpace);
}
