package mobi.monaca.framework.template.ast;

import mobi.monaca.framework.template.ASTNodeVisitor;
import mobi.monaca.framework.template.EvaluateUtil;
import mobi.monaca.framework.template.value.FalseValue;
import mobi.monaca.framework.template.value.TrueValue;
import mobi.monaca.framework.template.value.Value;

public class UnaryOperationNode extends Node {

    interface Operator {
        public Value operate(Value val);
    }

    protected Node node;

    protected Operator operator;

    private static Operator NOT = new Operator() {
        @Override
        public Value operate(Value val) {
            return EvaluateUtil.canCastAsTrue(val) ? FalseValue.getInstance()
                    : TrueValue.getInstance();
        }
    };

    public static UnaryOperationNode buildNot(Node node) {
        return new UnaryOperationNode(node, NOT);
    }

    private UnaryOperationNode(Node node, Operator operator) {
        this.node = node;
        this.operator = operator;
    }

    @Override
    public <T> void accept(ASTNodeVisitor<T> visitor, T v) {
        visitor.visit(this, v);
    }

    public Node getValueNode() {
        return node;
    }

    public Value operate(Value val) {
        return this.operator.operate(val);
    }
}
