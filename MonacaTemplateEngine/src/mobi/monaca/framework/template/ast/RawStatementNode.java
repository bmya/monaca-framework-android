package mobi.monaca.framework.template.ast;

import mobi.monaca.framework.template.Token;

public class RawStatementNode extends Node {
    protected Token<String> raw;

    public RawStatementNode(Token<String> raw) {
        this.raw = raw;
    }

    public <T> void accept(
            mobi.monaca.framework.template.ASTNodeVisitor<T> visitor, T v) {
        visitor.visit(this, v);
    }

    @Override
    public String toString() {
        return "raw";
    }

    public String getRawString() {
        return raw.getVal();
    }
}
