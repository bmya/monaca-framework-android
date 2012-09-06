package mobi.monaca.framework.template;

import mobi.monaca.framework.template.ast.BinaryOperationNode;
import mobi.monaca.framework.template.ast.BlockStatementNode;
import mobi.monaca.framework.template.ast.ConstantValueNode;
import mobi.monaca.framework.template.ast.EchoStatementNode;
import mobi.monaca.framework.template.ast.ExtendsStatementNode;
import mobi.monaca.framework.template.ast.IfStatementNode;
import mobi.monaca.framework.template.ast.IncludeStatementNode;
import mobi.monaca.framework.template.ast.ParentStatementNode;
import mobi.monaca.framework.template.ast.RawStatementNode;
import mobi.monaca.framework.template.ast.ShortCircuitBinaryOperationNode;
import mobi.monaca.framework.template.ast.StatementsNode;
import mobi.monaca.framework.template.ast.StringValueNode;
import mobi.monaca.framework.template.ast.TopStatementsNode;
import mobi.monaca.framework.template.ast.UnaryOperationNode;

public abstract class ASTNodeVisitor<T> {

    /* methods for expression nodes */
    abstract public void visit(StringValueNode node, T v);

    abstract public void visit(ConstantValueNode node, T v);

    abstract public void visit(BinaryOperationNode node, T v);

    abstract public void visit(ShortCircuitBinaryOperationNode node, T v);

    abstract public void visit(UnaryOperationNode node, T v);

    /* methods for statement nodes */
    abstract public void visit(BlockStatementNode node, T v);

    abstract public void visit(StatementsNode node, T v);

    abstract public void visit(TopStatementsNode node, T v);

    abstract public void visit(ExtendsStatementNode node, T v);

    abstract public void visit(IfStatementNode node, T v);

    abstract public void visit(IncludeStatementNode node, T v);

    abstract public void visit(ParentStatementNode node, T v);

    abstract public void visit(RawStatementNode node, T v);

    abstract public void visit(EchoStatementNode node, T v);

}
