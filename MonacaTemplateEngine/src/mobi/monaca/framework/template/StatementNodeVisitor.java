package mobi.monaca.framework.template;

import mobi.monaca.framework.template.ast.BinaryOperationNode;
import mobi.monaca.framework.template.ast.ConstantValueNode;
import mobi.monaca.framework.template.ast.ShortCircuitBinaryOperationNode;
import mobi.monaca.framework.template.ast.StringValueNode;
import mobi.monaca.framework.template.ast.UnaryOperationNode;

public abstract class StatementNodeVisitor<T> extends ASTNodeVisitor<T> {

    final public void visit(StringValueNode node, T v) {
    }

    final public void visit(ConstantValueNode node, T v) {
    }

    final public void visit(BinaryOperationNode node, T v) {
    }

    final public void visit(ShortCircuitBinaryOperationNode node, T v) {
    }

    final public void visit(UnaryOperationNode node, T v) {
    }

}
