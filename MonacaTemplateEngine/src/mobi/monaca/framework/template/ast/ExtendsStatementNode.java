package mobi.monaca.framework.template.ast;

import mobi.monaca.framework.template.Token;

public class ExtendsStatementNode extends Node {
    protected Token<String> path;

    public ExtendsStatementNode(Token<String> path) {
        this.path = path;
    }

    public <T> void accept(
            mobi.monaca.framework.template.ASTNodeVisitor<T> visitor, T v) {
        visitor.visit(this, v);
    }

    public String getPath() {
        return path.getVal();
    }

    @Override
    public String toString() {
        return "extends";
    }
}
