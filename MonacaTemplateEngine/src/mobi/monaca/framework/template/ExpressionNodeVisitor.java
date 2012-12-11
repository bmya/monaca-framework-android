package mobi.monaca.framework.template;

import mobi.monaca.framework.template.ast.BlockStatementNode;
import mobi.monaca.framework.template.ast.EchoStatementNode;
import mobi.monaca.framework.template.ast.ExtendsStatementNode;
import mobi.monaca.framework.template.ast.IfStatementNode;
import mobi.monaca.framework.template.ast.IncludeStatementNode;
import mobi.monaca.framework.template.ast.ParentStatementNode;
import mobi.monaca.framework.template.ast.RawStatementNode;
import mobi.monaca.framework.template.ast.StatementsNode;
import mobi.monaca.framework.template.ast.TopStatementsNode;

public abstract class ExpressionNodeVisitor<T> extends ASTNodeVisitor<T> {

    final public void visit(BlockStatementNode node, T v) {
    }

    final public void visit(IfStatementNode node, T v) {
    }

    final public void visit(StatementsNode node, T v) {
    }

    final public void visit(TopStatementsNode node, T v) {
    }

    final public void visit(ExtendsStatementNode node, T v) {
    }

    final public void visit(IncludeStatementNode node, T v) {
    }

    final public void visit(ParentStatementNode node, T v) {
    }

    final public void visit(RawStatementNode node, T v) {
    }

    final public void visit(EchoStatementNode node, T v) {
    }

}
