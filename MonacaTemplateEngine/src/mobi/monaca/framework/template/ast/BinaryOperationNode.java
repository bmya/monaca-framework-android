package mobi.monaca.framework.template.ast;

import mobi.monaca.framework.template.ASTNodeVisitor;
import mobi.monaca.framework.template.value.FalseValue;
import mobi.monaca.framework.template.value.TrueValue;
import mobi.monaca.framework.template.value.Value;

public class BinaryOperationNode extends Node {

    interface Operator {
        public Value operate(Value left, Value right);
    }

    protected Node left, right;

    protected Operator operator;

    private static Operator EQUAL = new Operator() {
        @Override
        public Value operate(Value left, Value right) {
            return left.eq(right) ? TrueValue.getInstance() : FalseValue
                    .getInstance();
        }
    };

    private static Operator NOT_EQUAL = new Operator() {
        @Override
        public Value operate(Value left, Value right) {
            return left.eq(right) ? FalseValue.getInstance() : TrueValue
                    .getInstance();
        }
    };

    public static BinaryOperationNode buildEqual(Node left, Node right) {
        return new BinaryOperationNode(left, right, EQUAL);
    }

    public static BinaryOperationNode buildNotEqual(Node left, Node right) {
        return new BinaryOperationNode(left, right, NOT_EQUAL);
    }

    private BinaryOperationNode(Node left, Node right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
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

    public Value operate(Value left, Value right) {
        return this.operator.operate(left, right);
    }
}
