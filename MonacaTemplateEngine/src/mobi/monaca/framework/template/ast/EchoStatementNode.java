package mobi.monaca.framework.template.ast;

import mobi.monaca.framework.template.ASTNodeVisitor;

public class EchoStatementNode extends Node {

    protected Node expression;
    protected PipeModifiers modifier;

    public EchoStatementNode(Node expression) {
        this.expression = expression;
        this.modifier = new PipeModifiers();
    }

    public EchoStatementNode(Node expression, PipeModifiers modifier) {
        this.expression = expression;
        this.modifier = modifier;
    }

    public Node getExpression() {
        return expression;
    }

    @Override
    public <T> void accept(ASTNodeVisitor<T> visitor, T v) {
        visitor.visit(this, v);
    }

    public boolean hasRawModifier() {
        for (String modifierId : modifier.getModifierIds()) {
            if (modifierId.equals("raw")) {
                return true;
            }
        }

        return false;
    }

}
