package com.japod.simplec.truffle;

import java.util.HashMap;
import java.util.Map;

public class GlobalSpace {

    private final Map<String, CGlobalDefinitionNode> functions = new HashMap<>();

    public void bind(CGlobalDefinitionNode node) {
        functions.put(node.getName(), node);
    }

    public CNode getMain() {
        CGlobalDefinitionNode mainDef = functions.get("main");
        return mainDef.getExecutable();
    }
}