package com.japod.simplec;

import com.japod.simplec.antlr4.CLexer;
import com.japod.simplec.antlr4.CParser;
import com.japod.simplec.antlr4.SimplecVisitor;
import com.japod.simplec.truffle.CNode;
import com.japod.simplec.truffle.CRootNode;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import java.io.FileInputStream;
import java.io.IOException;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class CLI {

    public static void main( String[] args ) throws IOException {
        if (args.length == 0) {
            System.err.printf("filename expected.\n");
            System.exit(1);
        }

        CNode n = parseProgram(args[0]);
        CRootNode cRoot = new CRootNode(new CNode[]{n});
        CallTarget target = Truffle.getRuntime().createCallTarget(cRoot);
        target.call();
    }

    private static CNode parseProgram(String fileName) throws RecognitionException, IOException {
        CLexer l = new CLexer(new ANTLRInputStream(new FileInputStream(fileName)));
        CParser p = new CParser(new CommonTokenStream(l));
        p.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
            }
        });
        SimplecVisitor sv = new SimplecVisitor();
        CNode n = sv.visit(p.compilationUnit());
        return n;
    }
}
