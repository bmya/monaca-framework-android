package mobi.monaca.framework.template.ast;

import mobi.monaca.framework.template.ASTNodeVisitor;
import mobi.monaca.framework.template.Token;
import mobi.monaca.framework.template.value.StringValue;
import mobi.monaca.framework.template.value.Value;

public class StringValueNode extends Node {

    protected Token<String> token;
    protected Value val;

    public StringValueNode(Token<String> token) {
        this.token = token;
        this.val = new StringValue(token.getVal());
    }

    public Value getValue() {
        return val;
    }

    @Override
    public <T> void accept(ASTNodeVisitor<T> visitor, T v) {
        visitor.visit(this, v);
    }

}
