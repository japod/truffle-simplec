package com.japod.simplec;

import com.japod.simplec.antlr4.CLexer;
import com.japod.simplec.antlr4.CParser;
import com.japod.simplec.antlr4.SimplecVisitor;
import com.japod.simplec.truffle.CNode;
import com.japod.simplec.truffle.CRootNode;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import junit.framework.TestCase;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.junit.Assert;
import org.junit.Test;


public class SimpleTest extends TestCase {

    @Test
    public void testHelloWorld() throws Exception {
        CLexer l = new CLexer(new ANTLRInputStream(getClass().getResourceAsStream("/helloworld.c")));
        CParser p = new CParser(new CommonTokenStream(l));
        p.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
            }
        });
        SimplecVisitor sv = new SimplecVisitor();
        CNode n = sv.visit(p.compilationUnit());
        CRootNode cRoot = new CRootNode(new CNode[]{n});
        CallTarget target = Truffle.getRuntime().createCallTarget(cRoot);

        PrintStream originalOut = System.out;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));

            target.call();

            Assert.assertTrue(baos.toString().contains("Hello World"));
        } finally {
            System.setOut(originalOut);
        }
   }
}
