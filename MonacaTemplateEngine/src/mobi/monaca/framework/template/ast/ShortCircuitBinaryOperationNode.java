package mobi.monaca.framework.template.ast;

import mobi.monaca.framework.template.ASTNodeVisitor;
import mobi.monaca.framework.template.EvaluateUtil;
import mobi.monaca.framework.template.value.FalseValue;
import mobi.monaca.framework.template.value.Value;

public abstract class ShortCircuitBinaryOperationNode extends Node {

    protected Node left, right;

    public static ShortCircuitBinaryOperationNode buildAnd(Node left, Node right) {
        return new ShortCircuitBinaryOperationNode.And(left, right);
    }

    public static ShortCircuitBinaryOperationNode buildOr(Node left, Node right) {
        return new ShortCircuitBinaryOperationNode.Or(left, right);
    }

    private ShortCircuitBinaryOperationNode(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public <T> void accept(ASTNodeVisitor<T> visitor, T v) {
        visitor.visit(this, v);
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    abstract public boolean willEvaluateRight(Value left);

    abstract public Value operate(Value left, Value right);

    public static class And extends ShortCircuitBinaryOperationNode {

        public And(Node left, Node right) {
            super(left, right);
        }

        @Override
        public boolean willEvaluateRight(Value left) {
            return EvaluateUtil.canCastAsTrue(left);
        }

        @Override
        public Value operate(Value left, Value right) {
            return EvaluateUtil.canCastAsTrue(right) ? right : FalseValue
                    .getInstance();
        }

    }

    public static class Or extends ShortCircuitBinaryOperationNode {

        public Or(Node left, Node right) {
            super(left, right);
        }

        @Override
        public boolean willEvaluateRight(Value left) {
            return !EvaluateUtil.canCastAsTrue(left);
        }

        @Override
        public Value operate(Value left, Value right) {
            return EvaluateUtil.canCastAsTrue(right) ? right : FalseValue
                    .getInstance();
        }

    }

}
