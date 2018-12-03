package com.japod.simplec.truffle;

import com.oracle.truffle.api.CompilerDirectives;

public class CPrintf extends CNode {

    String formatString;
    Object[] args;

    public CPrintf(String formatString, Object[] args) {
        this.formatString = formatString;
        this.args = args;
    }

    @Override
    @CompilerDirectives.TruffleBoundary
    public void execute(GlobalSpace globalSpace) {
        System.out.printf(formatString, args);
    }

}
