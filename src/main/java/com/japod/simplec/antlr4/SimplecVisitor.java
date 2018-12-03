package com.japod.simplec.antlr4;

import com.japod.simplec.truffle.CBlockNode;
import com.japod.simplec.truffle.CCallNode;
import com.japod.simplec.truffle.CConstantNode;
import com.japod.simplec.truffle.CGlobalDefinitionNode;
import com.japod.simplec.truffle.CNode;
import com.japod.simplec.truffle.CPrintf;

import com.oracle.truffle.api.nodes.Node;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

public class SimplecVisitor extends CBaseVisitor<CNode>{

    @Override
    protected CNode aggregateResult(CNode aggregate, CNode nextResult) {
        return nextResult == null ? aggregate : nextResult;
    }

    @Override
    public CNode visitCompilationUnit(CParser.CompilationUnitContext ctx) {
        Deque<Node> defNodes = new LinkedList<>();
        CParser.TranslationUnitContext translationUnit = ctx.translationUnit();
        while (translationUnit != null) {
            CParser.ExternalDeclarationContext extDeclaration = translationUnit.externalDeclaration();
            if (extDeclaration != null) {
                Node n = visitExternalDeclaration(extDeclaration);
                if (n != null) {
                    defNodes.addFirst(n);
                }
            }
           translationUnit = translationUnit.translationUnit();
        }
        return new CBlockNode(defNodes.toArray(new CNode[]{}));
    }

    @Override
    public CNode visitFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
        String name = ctx.declarator().directDeclarator().directDeclarator().Identifier().getText();
        CNode body = visitCompoundStatement(ctx.compoundStatement());
        CGlobalDefinitionNode defNode = new CGlobalDefinitionNode(name, body);
        return defNode;
    }

    @Override
    public CNode visitCompoundStatement(CParser.CompoundStatementContext ctx) {
        Deque<CNode> items = new LinkedList<>();
        CParser.BlockItemListContext blockItemList = ctx.blockItemList();
        while (blockItemList != null) {
            CParser.BlockItemContext blockItem = blockItemList.blockItem();
            CNode in = visitBlockItem(blockItem);
            if (in != null) {
                items.addFirst(in);
            }
            blockItemList = blockItemList.blockItemList();
        }
        return new CBlockNode(items.toArray(new CNode[]{}));
    }

    @Override
    public CNode visitExternalDeclaration(CParser.ExternalDeclarationContext ctx) {
        final CParser.FunctionDefinitionContext functionDefinition = ctx.functionDefinition();
        if (functionDefinition != null) {
            return visitFunctionDefinition(functionDefinition);
        }
        return super.visitExternalDeclaration(ctx);
    }

    @Override
    public CNode visitPostfixExpression(CParser.PostfixExpressionContext ctx) {
        CParser.ArgumentExpressionListContext argumentExpressionList = ctx.argumentExpressionList();
        if (argumentExpressionList != null) {
            Deque<CNode> argNodes = new LinkedList<>();
            while (argumentExpressionList != null) {
                CParser.AssignmentExpressionContext assignmentExpression = argumentExpressionList.assignmentExpression();
                CNode an = visit(assignmentExpression);
                if (an != null) {
                    argNodes.addFirst(an);
                }
                argumentExpressionList = argumentExpressionList.argumentExpressionList();
            }
            String calledName = ctx.postfixExpression().primaryExpression().getText();
            if (calledName.equals("printf")) {
                return new CPrintf(((CConstantNode)argNodes.removeFirst()).getValue(), makePrintfArgs(argNodes));
            }
            return new CCallNode(calledName, argNodes.toArray(new CNode[]{}));
        }
        return super.visitPostfixExpression(ctx);
    }

    private Object[] makePrintfArgs(Deque<CNode> nodes) {
        Object[] result = new Object[nodes.size()];
        for (int i = 0; i<result.length; i++) {
            result[i] = ((CConstantNode)nodes.removeFirst()).getValue();
        }
        return result;
    }

    @Override
    public CNode visitPrimaryExpression(CParser.PrimaryExpressionContext ctx) {
        List<TerminalNode> literals = ctx.StringLiteral();
        if (literals != null) {
            return new CConstantNode(concatenate(literals));
        }
        return super.visitPrimaryExpression(ctx);
    }

    private String concatenate(List<TerminalNode> nodes) {
        StringBuilder result = new StringBuilder();
        for (TerminalNode tn : nodes) {
            String s = tn.getText();
            result.append(s.substring(1, s.length()-1));
        }
        return result.toString();
    }
}
