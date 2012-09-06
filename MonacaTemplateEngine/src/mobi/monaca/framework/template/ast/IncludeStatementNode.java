package mobi.monaca.framework.template.ast;

import mobi.monaca.framework.template.Token;

public class IncludeStatementNode extends Node {
    protected Token<String> path;

    public IncludeStatementNode(Token<String> path) {
        this.path = path;
    }

    public String getPath() {
        return path.getVal();
    }

    public <T> void accept(
            mobi.monaca.framework.template.ASTNodeVisitor<T> visitor, T v) {
        visitor.visit(this, v);
    }

    @Override
    public String toString() {
        return "include";
    }
}
