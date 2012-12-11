package mobi.monaca.framework.template.ast;

public class ParentStatementNode extends Node {

    public ParentStatementNode() {

    }

    public <T> void accept(
            mobi.monaca.framework.template.ASTNodeVisitor<T> visitor, T v) {
        visitor.visit(this, v);
    }

    @Override
    public String toString() {
        return "parent";
    }
}
